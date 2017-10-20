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
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.TreeWalk;

import java.io.File;
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
public class GetCurrentLambdas {

	static String repoLoc;
	static String repoName;

	public static void curLambdasRepo(String reposDir,String githubRepoName) throws IOException, RefAlreadyExistsException, RefNotFoundException, InvalidRefNameException, CheckoutConflictException, GitAPIException {
		repoLoc = reposDir;
		repoName = githubRepoName;
		try (Repository repository = getRepository(repoLoc)) {
			walkCommit(repository,githubRepoName);
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
	private static void walkCommit(Repository repository, String githubRepoName) throws IOException, RefAlreadyExistsException, RefNotFoundException, InvalidRefNameException, CheckoutConflictException, GitAPIException {
		Ref head = null;
		Collection<Ref> allRefs = repository.getAllRefs().values();
		HashMap<String,ArrayList<String>> lambdasInFiles = new HashMap<String, ArrayList<String>>();
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
			PrintWriter pw = new PrintWriter(new File(repoLoc+"allLambdas.csv"));
			pw.write("sep=#\n");
			pw.write("toString#Filename\n");
			RevCommit commit = revWalk.parseCommit(head.getObjectId());
			git.checkout().setName(commit.name()).call();
			lambdasInFiles = lambdasInFile(commit,treeWalk);
			Set<String> keys = lambdasInFiles.keySet();
			int count = 0;
			for(String k : keys){
				ArrayList<String> lambdas = lambdasInFiles.get(k);
				count += lambdas.size();
				for(int i = 0; i<lambdas.size();i++) {
					pw.write(lambdas.get(i) + "#" + k + "\n");
				}
				
			}
			git.close();
			pw.close();
			PrintWriter counter = new PrintWriter(new File(repoLoc+ "count.txt"));
			counter.print(repoName + " " + count);
			counter.close();
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
}