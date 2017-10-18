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
public class WalkAllCommits {

	static String repoLoc;

	public static void walkRepo(String reposDir,String githubRepoName) throws IOException, RefAlreadyExistsException, RefNotFoundException, InvalidRefNameException, CheckoutConflictException, GitAPIException {
		repoLoc = reposDir;
		try (Repository repository = getRepository(repoLoc)) {
			walkCommits(repository,githubRepoName);
		}
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
	private static void walkCommits(Repository repository, String githubRepoName) throws IOException, RefAlreadyExistsException, RefNotFoundException, InvalidRefNameException, CheckoutConflictException, GitAPIException {
		Ref head = null;
		Collection<Ref> allRefs = repository.getAllRefs().values();
		HashMap<String,ArrayList<String>> lambdasInFiles = new HashMap<String, ArrayList<String>>();
		HashMap<String,ArrayList<String>> newlambdasInFiles = new HashMap<String, ArrayList<String>>();
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
			TreeWalk treeWalk = new TreeWalk(repository);
			PrintWriter pw = new PrintWriter(new File(repoLoc+"NewLambdas.csv"));
			pw.write("sep=,\n");
			pw.write("Github_diffs,Github_file_after,Github_file_before,Hash_After,Hash_Before Filename\n");
			boolean commitBefore = false;
			RevCommit oldcommit = null;
			RevCommit commit = revWalk.parseCommit(head.getObjectId());
				while(commit.getCommitTime() > 1394233200 || !commitBefore){
					git.checkout().setName(commit.name()).call();
					newlambdasInFiles = lambdasInFile(commit,treeWalk);
					Set<String> keys = lambdasInFiles.keySet();
					for(String k : keys){
						ArrayList<String> newlambdas = newlambdasInFiles.get(k);
						ArrayList<String> oldlambdas = lambdasInFiles.get(k);
						if(newlambdas == null){
							pw.write("=HYPERLINK(\"https://github.com/"+ githubRepoName + "commit/" + oldcommit.getName() + "\" )"
									+ "," + "=HYPERLINK(\"https://github.com/"+ githubRepoName + "blob/" + oldcommit.getName() + "/" + k + "\" )"
									+ "," + " "
									+ "," + oldcommit.getName() 
									+ "," + commit.getName() 
									+ "," + k + "\n");
						}
						else if(oldlambdas.size() != newlambdas.size()){
							pw.write("=HYPERLINK(\"https://github.com/"+ githubRepoName + "commit/" + oldcommit.getName() + "\" )"
									+ "," + "=HYPERLINK(\"https://github.com/"+ githubRepoName + "blob/" + oldcommit.getName() + "/" + k + "\" )"
									+ "," + "=HYPERLINK(\"https://github.com/"+ githubRepoName + "blob/" + commit.getName() + "/" + k + "\" )"
									+ "," + oldcommit.getName() 
									+ "," + commit.getName() 
									+ "," + k + "\n");
						}
					}
					lambdasInFiles = newlambdasInFiles;
					if(commit.getCommitTime() <= 1394233200){
						commitBefore = true;
					}
					oldcommit = commit;
					commit = revWalk.parseCommit(commit.getParent(0).getId());
				}
			
			git.close();
			pw.close();
		}
	}

	private static HashMap<String,ArrayList<String>> lambdasInFile(RevCommit commit,TreeWalk treeWalk) throws MissingObjectException, IncorrectObjectTypeException, CorruptObjectException, IOException{
		HashMap<String,ArrayList<String>> lambdasInFiles = new HashMap<String, ArrayList<String>>();
		RevTree tree = commit.getTree();
		Parse parser = new Parse();
		treeWalk.reset();
		treeWalk.addTree(tree);
		treeWalk.setRecursive(true);
		while (treeWalk.next()) {
			String fileName = treeWalk.getPathString();
			if(fileName.contains(".java")){
				ArrayList<String> lambdas = parser.saveLambdas(parser.readFileToString(repoLoc+fileName));
				if(!lambdas.isEmpty()){
					lambdasInFiles.put(fileName, lambdas);
				}
			}
		}
		return lambdasInFiles;
	}




	private static int lambdasInCommit(RevCommit commit,TreeWalk treeWalk) throws MissingObjectException, IncorrectObjectTypeException, CorruptObjectException, IOException{
		int lambdaCount = 0;
		RevTree tree = commit.getTree();
		//System.out.println("Having tree: " + tree);

		Parse parser = new Parse();
		// now use a TreeWalk to iterate over all files in the Tree recursively
		// you can set Filters to narrow down the results if needed

		treeWalk.reset();
		treeWalk.addTree(tree);
		treeWalk.setRecursive(true);
		while (treeWalk.next()) {
			String fileName = treeWalk.getPathString();
			if(fileName.contains(".java")){
				lambdaCount = parser.countLambdas(parser.readFileToString(repoLoc+fileName));

			}
		}
		return lambdaCount;
	}




}