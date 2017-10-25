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

import org.eclipse.jgit.api.errors.CheckoutConflictException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRefNameException;
import org.eclipse.jgit.api.errors.RefAlreadyExistsException;
import org.eclipse.jgit.api.errors.RefNotFoundException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;


/**
 * Simple snippet which shows how to use RevWalk to iterate over all commits
 * across all branches/tags/remotes in the given repository
 *
 * See the original discussion at http://stackoverflow.com/a/40803945/411846
 */
public class FindNLambdas {

	static String repoLoc;

	public static void walkRepo(String reposDir, String repoName, String input,String output,int n) throws IOException, RefAlreadyExistsException, RefNotFoundException, InvalidRefNameException, CheckoutConflictException, GitAPIException {
		repoLoc = reposDir;
		try (Repository repository = getRepository(repoLoc)) {
			walkCommits(repository, repoName, input, output,n);
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
	private static void walkCommits(Repository repository, String repoName, String lambdaCountFileName, String outputFileName,int n) throws IOException, RefAlreadyExistsException, RefNotFoundException, InvalidRefNameException, CheckoutConflictException, GitAPIException {
		Scanner sc = new Scanner(new File(lambdaCountFileName));
		PrintWriter pw = new PrintWriter(new File(outputFileName));
		pw.write("sep=,\n");
		pw.write("Hash,Old/New\n");
		sc.nextLine();
		sc.nextLine();
		String oldhash = "";
		int oldcount = 0;
		ArrayList<String> newestHashes = new ArrayList<String>();
		ArrayList<String> oldestHashes = new ArrayList<String>();
		int newlambdacount = 0;
		int firstcount = -1;
		while(sc.hasNextLine()){
			String entry = sc.nextLine();
			String[] split = entry.split(",");
			String hash = split[0];
			int count = Integer.valueOf(split[1]);
			if(firstcount == -1){
				firstcount = count;
			}
			if(count < oldcount){
				newestHashes.add(oldhash);
				newlambdacount = firstcount-count;
				if(newlambdacount >= n){
					break;
				}
			}
			oldhash = hash;
			oldcount = count;
		}	
		oldcount = n+1;
		while(sc.hasNextLine()){
			String entry = sc.nextLine();
			String[] split = entry.split(",");
			String hash = split[0];
			int count = Integer.valueOf(split[1]);
			if(count < oldcount){
				oldestHashes.add(oldhash);
				oldcount = count;
			}
			oldhash = hash;
		}
		for(String s : newestHashes){
			pw.println("=HYPERLINK(\"https://github.com/"+ repoName + "commit/" + s + "\" )"+",new");
		}
		for(String s : oldestHashes){
			pw.println("=HYPERLINK(\"https://github.com/"+ repoName + "commit/" + s + "\" )"+",old");
		}
		pw.close();
		sc.close();
	}
}