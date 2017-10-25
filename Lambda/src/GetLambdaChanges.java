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
import org.eclipse.jgit.treewalk.TreeWalk;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
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

	public static void walkRepo(String reposDir,String repoName,HashMap<String,ArrayList<String>> selectedLambdas) throws IOException, RefAlreadyExistsException, RefNotFoundException, InvalidRefNameException, CheckoutConflictException, GitAPIException {
		GetLambdaChanges.reposDir = reposDir;
		GetLambdaChanges.repoName = reposDir + repoName;
		pw = new FileWriter(new File(GetLambdaChanges.repoName+"RQ2Lambdas.csv"),true);
		pw.write("sep=#\n");
		pw.write("Github_diffs#Github_file_after#Github_file_before#Hash_After#Hash_Before#Filename#toString\n");
		try (Repository repository = getRepository(GetLambdaChanges.repoName)) {
			walkFiles(repository,"GumTreeDiff/gumtree/",selectedLambdas);
		}
		pw.close();
	}

	public static void walkFiles(Repository repository, String githubRepoName,HashMap<String,ArrayList<String>> selectedLambdas) throws RefAlreadyExistsException, RefNotFoundException, InvalidRefNameException, CheckoutConflictException, IOException, GitAPIException {
		for(String fileName : selectedLambdas.keySet()) {
			walkCommits(repository,githubRepoName,fileName,selectedLambdas.get(fileName));
		}
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
	private static void walkCommits(Repository repository, String githubRepoName,String fileName, ArrayList<String> lambdasToString) throws IOException, RefAlreadyExistsException, RefNotFoundException, InvalidRefNameException, CheckoutConflictException, GitAPIException {
		Ref head = null;
		Collection<Ref> allRefs = repository.getAllRefs().values();
		ArrayList<String> lambdasInFile = lambdasToString;
		ArrayList<String> lambdasInFileBefore = new ArrayList<String>();
		int c = 0;
		for(Ref r: allRefs){
			c++;
			if(c==2){
				head = r;
			}
		}
		try (RevWalk revWalk = new RevWalk( repository )) {
			System.out.println(head);
			revWalk.markStart( revWalk.parseCommit(head.getObjectId() ));
			Git git = new Git(repository);
			//TreeWalk treeWalk = new TreeWalk(repository);

			RevCommit oldcommit = null;
			RevCommit commit = revWalk.parseCommit(head.getObjectId());
			boolean commitBefore = false;
			boolean noLambdas = false;
			while((commit.getCommitTime() > 1394233200 || !commitBefore)&&!noLambdas){
				git.checkout().setName(commit.name()).call();
				try {
					lambdasInFileBefore = lambdasInFile(commit,fileName);
				}catch(FileNotFoundException e) {
					pw.write("RENAME/INSERT"
							+ "#" + "=HYPERLINK(\"https://github.com/"+ githubRepoName + "blob/" + oldcommit.getName() + "/" + fileName + "\" )"
							+ "#" 
							+ "#" + oldcommit.getName() 
							+ "#" + commit.getName() 
							+ "#" + fileName 
							+ "#" + "\n");
					noLambdas = true;
				}
				int changeCount = 0;
				ArrayList<String> changedLambdas = new ArrayList<String>();
				for(String lambda:lambdasInFile) {
					if(!lambdasInFileBefore.contains(lambda)) {
						changeCount +=1;
						changedLambdas.add(lambda);
					}
				}
				if(changeCount>0){
					for(String changedLambda : changedLambdas) {
						pw.write("=HYPERLINK(\"https://github.com/"+ githubRepoName + "commit/" + oldcommit.getName() + "\" )"
								+ "#" + "=HYPERLINK(\"https://github.com/"+ githubRepoName + "blob/" + oldcommit.getName() + "/" + fileName + "\" )"
								+ "#" + "=HYPERLINK(\"https://github.com/"+ githubRepoName + "blob/" + commit.getName() + "/" + fileName + "\" )"
								+ "#" + oldcommit.getName() 
								+ "#" + commit.getName() 
								+ "#" + fileName 
								+ "#" + changedLambda + "\n");
						//						System.out.println("=HYPERLINK(\"https://github.com/"+ githubRepoName + "commit/" + oldcommit.getName() + "\" )"
						//						+ "#" + "=HYPERLINK(\"https://github.com/"+ githubRepoName + "blob/" + oldcommit.getName() + "/" + fileName + "\" )"
						//						+ "#" + "=HYPERLINK(\"https://github.com/"+ githubRepoName + "blob/" + commit.getName() + "/" + fileName + "\" )"
						//						+ "#" + oldcommit.getName() 
						//						+ "#" + commit.getName() 
						//						+ "#" + fileName 
						//						+ "#" + changedLambda + "\n");
					}
					if(lambdasInFileBefore.size()==0) {
						noLambdas = true;
					}
					else if((lambdasInFile.size()-changeCount)==lambdasInFileBefore.size()) {
						lambdasInFile = lambdasInFileBefore;
					}

				}
				oldcommit = commit;
				commit = revWalk.parseCommit(commit.getParent(0).getId());
				if(commit.getCommitTime() <= 1394233200){
					commitBefore = true;
				}
			}
			git.close();
			
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