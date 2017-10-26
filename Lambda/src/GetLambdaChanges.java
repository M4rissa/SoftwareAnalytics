/*
   Copyright 2016 Dominik Stadler
   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at
     http://www.apache.org/licenses/LICENSE-2.0
   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.CheckoutConflictException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRefNameException;
import org.eclipse.jgit.api.errors.RefAlreadyExistsException;
import org.eclipse.jgit.api.errors.RefNotFoundException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.RawTextComparator;
import org.eclipse.jgit.errors.CorruptObjectException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.DepthWalk.Commit;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.treewalk.EmptyTreeIterator;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.util.io.DisabledOutputStream;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;


/**
 * Simple snippet which shows how to use RevWalk to iterate over all commits
 * across all branches/tags/remotes in the given repository
 *
 * See the original discussion at http://stackoverflow.com/a/40803945/411846
 */
public class GetLambdaChanges {

	static String reposDir;
	static String repoName;
	static FileWriter pw;

	public static void walkRepo(String reposDir,String repoName,String gitHubRepoName,HashMap<String,ArrayList<String>> selectedLambdas) throws IOException, RefAlreadyExistsException, RefNotFoundException, InvalidRefNameException, CheckoutConflictException, GitAPIException {
		GetLambdaChanges.reposDir = reposDir;
		GetLambdaChanges.repoName = reposDir + repoName;
		pw = new FileWriter(new File(GetLambdaChanges.repoName+"RQ2Lambdas.csv"));
		pw.write("sep=#\n");
		//pw.write("Github_diffs#Github_file_after#Github_file_before#Hash_After#Hash_Before#Filename#toString\n");
		pw.write("Change_type#Github_diffs#Github_file_after#Github_file_before#Filename#toString#Hash_Before#Hash_After\n");
		try (Repository repository = getRepository(GetLambdaChanges.repoName)) {
			walkFiles(repository,gitHubRepoName,selectedLambdas);
		}
		pw.close();
	}

	public static void walkFiles(Repository repository, String githubRepoName,HashMap<String,ArrayList<String>> selectedLambdas) throws RefAlreadyExistsException, RefNotFoundException, InvalidRefNameException, CheckoutConflictException, IOException, GitAPIException {
		walkCommits(repository,githubRepoName,selectedLambdas);
	}


	/**
	 * Walks over the commits of a repository.
	 * @param repository
	 * @param githubRepoName 
	 * @throws IOException
	 * @throws GitAPIException 
	 * @throws CheckoutConflictException 
	 * @throws InvalidRefNameException 
	 * @throws RefNotFoundException 
	 * @throws RefAlreadyExistsException 
	 */
	private static void walkCommits(Repository repository, String githubRepoName,HashMap<String,ArrayList<String>> lambdasPerFile) throws IOException, RefAlreadyExistsException, RefNotFoundException, InvalidRefNameException, CheckoutConflictException, GitAPIException {
		Ref head = null;
		Collection<Ref> allRefs = repository.getAllRefs().values();
		int c = 0;
		for(Ref r: allRefs){
			c++;
			if(c==2){
				head = r;
			}
		}
		DiffFormatter df = new DiffFormatter(DisabledOutputStream.INSTANCE);
		df.setRepository(repository);
        df.setDiffComparator(RawTextComparator.DEFAULT);
        df.setDetectRenames(true);
		try (RevWalk revWalk = new RevWalk( repository )) {
			System.out.println(head);
			revWalk.markStart( revWalk.parseCommit(head.getObjectId() ));
			Git git = new Git(repository);
			//TreeWalk treeWalk = new TreeWalk(repository);
			RevCommit commitAfter = null;
			RevCommit thisCommit = revWalk.parseCommit(head.getObjectId());
			boolean commitBeforeJ8 = false;
			int nrFiles = lambdasPerFile.keySet().size();
			while((thisCommit.getCommitTime() > 1394233200 || !commitBeforeJ8) && nrFiles>0){
				git.checkout().setName(thisCommit.name()).call();
				List<DiffEntry> diffs = null;
				if(commitAfter==null) {
					  diffs = new ArrayList<DiffEntry>();
				}
				else {
					diffs = df.scan(thisCommit,commitAfter);
				}
				for(int i = 0; i<diffs.size();i++) {
					DiffEntry diff = diffs.get(i);
					HashMap<String,ArrayList<String>> newLambdasPerFile = (HashMap<String, ArrayList<String>>) lambdasPerFile.clone();
					for(String fileName : newLambdasPerFile.keySet())
						if(diff.getNewPath().equals(fileName)) {
							for(String lambdaToString : newLambdasPerFile.get(fileName)) {
								if(diff.getChangeType() == DiffEntry.ChangeType.MODIFY) {
									//System.out.println(getDiffText(repository,diff));
									//System.out.println(fileName + " has changed");
									pw.write("CHANGEFILE"
											+ "#" + "=HYPERLINK(\"https://github.com/"+ githubRepoName + "commit/" + commitAfter.getName() + "\" )"
											+ "#" + "=HYPERLINK(\"https://github.com/"+ githubRepoName + "blob/" + commitAfter.getName() + "/" + fileName + "\" )"
											+ "#" + "=HYPERLINK(\"https://github.com/"+ githubRepoName + "blob/" + thisCommit.getName() + "/" + fileName + "\" )"
											+ "#" + fileName
											+ "#" + lambdaToString
											+ "#" + thisCommit.getName()
											+ "#" + commitAfter.getName()
											+ "\n");
								}else if (diff.getChangeType()== DiffEntry.ChangeType.RENAME) {				
									pw.write("RENAME"
											+ "#" + "=HYPERLINK(\"https://github.com/"+ githubRepoName + "commit/" + commitAfter.getName() + "\" )"
											+ "#" + "=HYPERLINK(\"https://github.com/"+ githubRepoName + "blob/" + commitAfter.getName() + "/" + fileName + "\" )"
											+ "#" + "=HYPERLINK(\"https://github.com/"+ githubRepoName + "blob/" + thisCommit.getName() + "/" + diff.getOldPath() + "\" )"
											+ "#" + fileName
											+ "#" + lambdaToString 
											+ "#" + thisCommit.getName()
											+ "#" + commitAfter.getName()
											+ "\n");
									ArrayList<String> lambdasInFile = lambdasPerFile.get(fileName);
									lambdasPerFile.remove(fileName);
									fileName = diff.getOldPath();
									lambdasPerFile.put(fileName, lambdasInFile);
								}
								else if(diff.getChangeType()==DiffEntry.ChangeType.ADD) {
									pw.write("ADDFILE"
											+ "#" + "=HYPERLINK(\"https://github.com/"+ githubRepoName + "commit/" + commitAfter.getName() + "\" )"
											+ "#" + "=HYPERLINK(\"https://github.com/"+ githubRepoName + "blob/" + commitAfter.getName() + "/" + fileName + "\" )"
											+ "#" + ""
											+ "#" + fileName
											+ "#" + lambdaToString
											+ "#" + thisCommit.getName()
											+ "#" + commitAfter.getName()
											+ "\n");
									lambdasPerFile.remove(fileName);
									nrFiles = nrFiles-1;
								}try {
									if(lambdasPerFile.containsKey(fileName) && lambdasInFile(thisCommit,fileName).size()==0) {
										lambdasPerFile.remove(fileName);
										nrFiles = nrFiles-1;
									}
								}catch(FileNotFoundException e) {
									System.out.println(thisCommit.getName()+ " " + fileName);
									throw new FileNotFoundException();
								}
							}
						}
				}
				commitAfter = thisCommit;
				thisCommit = revWalk.parseCommit(thisCommit.getParent(0).getId());
				if(thisCommit.getCommitTime() <= 1394233200){
					commitBeforeJ8 = true;
				}
				git.close();
			}
			
			
		}
	}
	
	private static String getDiffText(Repository repo, DiffEntry diff) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (DiffFormatter df2 = new DiffFormatter(out)) {
            String diffText;
            df2.setRepository(repo);
			df2.format(diff);
			diffText = out.toString("UTF-8");
			return diffText;
		} catch (Throwable e) {
			return "";
		}
	}


	private static ArrayList<String> lambdasInFile(RevCommit commit,String fileName) throws FileNotFoundException, MissingObjectException, IncorrectObjectTypeException, CorruptObjectException, IOException{
		Parse parser = new Parse();
		return parser.saveLambdas(parser.readFileToString(repoName+fileName));
	}


	/**
	 * Gets repository at given fileLocation
	 * @param repLocation The location of the repository
	 * @return
	 * @throws IOException
	 */
	private static Repository getRepository(String repLocation) throws IOException {
		FileRepositoryBuilder builder = new FileRepositoryBuilder();
		//        builder.setGitDir(new File("C:/Users/Justin/SA/PocketHub"));
		return builder
				.readEnvironment() 
				.findGitDir(new File(repLocation))
				.build();
	}
}