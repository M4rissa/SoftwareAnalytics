import java.io.IOException;

import org.eclipse.jgit.api.errors.CheckoutConflictException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRefNameException;
import org.eclipse.jgit.api.errors.RefAlreadyExistsException;
import org.eclipse.jgit.api.errors.RefNotFoundException;

public class Main {
	
	//Directory where the repo directory is located
	static String reposDir = "C:/Users/Justin/SA/";
	//Name of the repo directory
	static String repoName = "gumtree";
	//Just for the gitHub link, this shouldn't crash anything
	static String gitHubRepoName = "GumTreeDiff/gumtree/";
	//Number of first and last lambdas you want to aim for
	static int n = 5;
 
	public static void main(String[] args) throws RefAlreadyExistsException, RefNotFoundException, InvalidRefNameException, CheckoutConflictException, IOException, GitAPIException {
		WalkAllCommits.walkRepo(reposDir, repoName);
		FindNLambdas.walkRepo(reposDir,gitHubRepoName, reposDir+repoName+"/LambdaCount.csv",reposDir+repoName+"/FirstAndLastLambdas.csv",n);
	}
}