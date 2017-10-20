import java.io.IOException;

import org.eclipse.jgit.api.errors.CheckoutConflictException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRefNameException;
import org.eclipse.jgit.api.errors.RefAlreadyExistsException;
import org.eclipse.jgit.api.errors.RefNotFoundException;

public class Main {

	static String reposDir = "C:/Users/install/Repos/";
	//static String githubRepoName = "gumtree/";
 
	public static void main(String[] args) throws RefAlreadyExistsException, RefNotFoundException, InvalidRefNameException, CheckoutConflictException, IOException, GitAPIException {
		GetCurrentLambdas.curLambdasAllRepos(reposDir);
	}
}