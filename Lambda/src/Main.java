import java.io.IOException;

import org.eclipse.jgit.api.errors.CheckoutConflictException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRefNameException;
import org.eclipse.jgit.api.errors.RefAlreadyExistsException;
import org.eclipse.jgit.api.errors.RefNotFoundException;

public class Main {
	
	static String reposDir = "C:/Users/Justin/SA/";
	static String repoName = "gumtree";
	static String gitHubRepoName = "GumTreeDiff/gumtree/";
	static int n = 5;
 
	public static void main(String[] args) throws RefAlreadyExistsException, RefNotFoundException, InvalidRefNameException, CheckoutConflictException, IOException, GitAPIException {
//		WalkAllCommits.walkRepo(reposDir, repoName);
		FindNLambdas.walkRepo(reposDir,gitHubRepoName, reposDir+repoName+"/LambdaCount.csv",reposDir+repoName+"/FirstAndLastLambdas.csv",n);
	}
}