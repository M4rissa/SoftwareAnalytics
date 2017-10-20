import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.jgit.api.errors.CheckoutConflictException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRefNameException;
import org.eclipse.jgit.api.errors.RefAlreadyExistsException;
import org.eclipse.jgit.api.errors.RefNotFoundException;

public class Main {

	static String reposDir = "C:/Users/install/Repos/";
	static int nrOfRandomLambdas = 365;
	//static String githubRepoName = "gumtree/";
 
	public static void main(String[] args) throws RefAlreadyExistsException, RefNotFoundException, InvalidRefNameException, CheckoutConflictException, IOException, GitAPIException {
		//GetCurrentLambdas.curLambdasAllRepos(reposDir);
		HashMap<String,ArrayList<Integer>> selectedLambdas = selectLambdas(reposDir);
		System.out.println(selectedLambdas);
	}
	
	private static HashMap<String,ArrayList<Integer>> selectLambdas(String reposDir) throws NumberFormatException, IOException{
		HashMap<String,ArrayList<Integer>> selectedLambdas = new HashMap<String,ArrayList<Integer>>();
		File reposDirectory = new File(reposDir);
		File countFile = new File(reposDir+"count.txt");
		BufferedReader read = new BufferedReader(new FileReader(countFile));
		int totalCount = 0;
		if(reposDirectory.isDirectory()) {
			for(File repoDir : reposDirectory.listFiles()) {
				if(repoDir.isDirectory()) {
					selectedLambdas.put(repoDir.getName()+"/",new ArrayList<Integer>());
					totalCount += Integer.parseInt(read.readLine().split("\\s")[1]);
				} 
			}
		}
		read = new BufferedReader(new FileReader(countFile));
		Random randomGenerator = new Random();
		TreeSet<Integer> generatedRandoms = new TreeSet<Integer>();
		while(generatedRandoms.size()<nrOfRandomLambdas){
			generatedRandoms.add(randomGenerator.nextInt(totalCount));
		}
		int count = 0;
		Integer[] randomArray = (Integer[])generatedRandoms.toArray(new Integer[nrOfRandomLambdas]);
		String[] line = read.readLine().split("\\s");
		int add = Integer.parseInt(line[1]);
		for(int i = 0; i<nrOfRandomLambdas;i++) {
			int random = randomArray[i];
			while(count+add<random) {
				count+=add;
				line = read.readLine().split("\\s");
				add = Integer.parseInt(line[1]);
			}
			String repo = line[0];
			ArrayList<Integer> newRandom = selectedLambdas.get(repo);
			newRandom.add(random-count);
			selectedLambdas.put(repo, newRandom);
		}
		return selectedLambdas;
	}
}