import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.jgit.api.errors.CheckoutConflictException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRefNameException;
import org.eclipse.jgit.api.errors.RefAlreadyExistsException;
import org.eclipse.jgit.api.errors.RefNotFoundException;

public class Main {

	static String reposDir = "C:/Users/install/Repos2/";
	static int nrOfRandomLambdas = 385;
	static String randomFile = "random.txt";

	public static void main(String[] args) throws RefAlreadyExistsException, RefNotFoundException,
			InvalidRefNameException, CheckoutConflictException, IOException, GitAPIException {
		//GetCurrentLambdas.curLambdasAllRepos(reposDir);
		HashMap<String,ArrayList<Integer>> selectedLambdasPerRepo = getSelectedRandomLambdas();
		HashMap<String,String> gitHubRepoNames = readGitHubRepoNames(reposDir);
		for (String repoName : selectedLambdasPerRepo.keySet()) {
			ArrayList<Integer> nrsOfLambdas = selectedLambdasPerRepo.get(repoName);
			if (nrsOfLambdas.size() != 0) {
				//System.out.println(reposDir + " " + new File(reposDir+repoName));
				GetCurrentLambdas.curLambdasRepo(reposDir, new File(reposDir+repoName));
				HashMap<String,ArrayList<String>> selectedLambdasInRepo = selectedLambdasInRepo(reposDir + repoName, selectedLambdasPerRepo.get(repoName));
				GetLambdaChanges.walkRepo(reposDir, repoName,gitHubRepoNames.get(repoName), selectedLambdasInRepo);
			}
		}
		System.out.println(selectedLambdasPerRepo);
	}
	
	private static void selectLambdas() throws IOException {
		HashMap<String, ArrayList<Integer>> selectedLambdasPerRepo = selectLambdasPerRepo(reposDir);
		createRandomLambdasFile(selectedLambdasPerRepo);
	}
	
	private static HashMap<String, ArrayList<Integer>> getSelectedRandomLambdas() throws FileNotFoundException{
		HashMap<String, ArrayList<Integer>> selectedLambdasPerRepo = new HashMap<String,ArrayList<Integer>>();
		Scanner sc = new Scanner(new File(reposDir+randomFile));
		while(sc.hasNextLine()) {
			String repoName = sc.next();
			ArrayList<Integer> lambdas = new ArrayList<Integer>();
			while(sc.hasNextInt()) {
				lambdas.add(sc.nextInt());
			}
			sc.nextLine();
			selectedLambdasPerRepo.put(repoName, lambdas);
		}
		return selectedLambdasPerRepo;
	}
	
	private static void createRandomLambdasFile(HashMap<String,ArrayList<Integer>> selectedLambdasPerRepo) throws IOException {
		FileWriter fw = new FileWriter(new File(reposDir+randomFile));
		for(String repoName : selectedLambdasPerRepo.keySet()) {
			ArrayList<Integer> selectedLambdas = selectedLambdasPerRepo.get(repoName);
			String out = repoName;
			for(Integer i : selectedLambdas) {
				out += " " + i;
			}
			fw.write(out + "\n");
		}
		fw.close();
	}

	private static HashMap<String, String> readGitHubRepoNames(String reposDir) throws FileNotFoundException {
		HashMap<String,String> repoNames = new HashMap<String,String>();
		File gitHubRepoNames = new File(reposDir + "GitHubRepoNames.txt");
		Scanner sc = new Scanner(new FileReader(gitHubRepoNames)); 
		while(sc.hasNextLine()){
			String line = sc.nextLine();
			String[] split = line.split(" ");
			repoNames.put(split[0],split[1]+"/"+split[0]);
		}
		sc.close();
		return repoNames;
	}

	private static HashMap<String,ArrayList<String>> selectedLambdasInRepo(String repoName, ArrayList<Integer> lambdaNrs)
			throws IOException {
		HashMap<String,ArrayList<String>> selectedLambdasInRepoPerFile = new HashMap<String,ArrayList<String>>();
		ArrayList<String> selectedLambdas = new ArrayList<String>();
		File lambdasInRepo = new File(repoName + "allLambdas.csv");
		BufferedReader reader = new BufferedReader(new FileReader(lambdasInRepo));
		int lambdaNrCount = 0;
		int curLambda = lambdaNrs.get(lambdaNrCount);
		int lastLambda = lambdaNrs.get(lambdaNrs.size()-1);
		int count = 0;
		reader.readLine();
		reader.readLine();
		String nextLine = "";
		while (count < lastLambda) {
			nextLine = reader.readLine();
			if (count == curLambda) {
				selectedLambdas.add(nextLine);
				lambdaNrCount += 1;
				curLambda = lambdaNrs.get(lambdaNrCount);
			}
			count += 1;
		}
		nextLine = reader.readLine();
		selectedLambdas.add(nextLine);
		for(String selected: selectedLambdas) {
			try {
			String[] entry = selected.split("#");
			String fileName = entry[1];
			ArrayList<String> inFile = new ArrayList<String>();
			if(selectedLambdasInRepoPerFile.containsKey(fileName)) {
				inFile = selectedLambdasInRepoPerFile.get(fileName);
				
			}
			inFile.add(entry[0]);
			selectedLambdasInRepoPerFile.put(fileName, inFile);
			}
			catch(NullPointerException e) {
				System.out.println("Couldn't find lambda in allLamdas file for " + repoName);
			}
		}
		reader.close();
		return selectedLambdasInRepoPerFile;
	}

	private static HashMap<String, ArrayList<Integer>> selectLambdasPerRepo(String reposDir)
			throws NumberFormatException, IOException {
		HashMap<String, ArrayList<Integer>> selectedLambdas = new HashMap<String, ArrayList<Integer>>();
		File reposDirectory = new File(reposDir);
		File countFile = new File(reposDir + "count.txt");
		BufferedReader read = new BufferedReader(new FileReader(countFile));
		int totalCount = 0;
		if (reposDirectory.isDirectory()) {
			for (File repoDir : reposDirectory.listFiles()) {
				if (repoDir.isDirectory()) {
					selectedLambdas.put(repoDir.getName() + "/", new ArrayList<Integer>());
					totalCount += Integer.parseInt(read.readLine().split("\\s")[1]);
				}
			}
		}
		read = new BufferedReader(new FileReader(countFile));
		Random randomGenerator = new Random();
		TreeSet<Integer> generatedRandoms = new TreeSet<Integer>();
		while (generatedRandoms.size() < nrOfRandomLambdas) {
			generatedRandoms.add(randomGenerator.nextInt(totalCount));
		}
		int count = 0;
		Integer[] randomArray = (Integer[]) generatedRandoms.toArray(new Integer[nrOfRandomLambdas]);
		String[] line = read.readLine().split("\\s");
		int add = Integer.parseInt(line[1]);
		for (int i = 0; i < nrOfRandomLambdas; i++) {
			int random = randomArray[i];
			while (count + add < random) {
				count += add;
				line = read.readLine().split("\\s");
				add = Integer.parseInt(line[1]);
			}
			String repo = line[0];
			ArrayList<Integer> newRandom = selectedLambdas.get(repo);
			newRandom.add(random - count);
			selectedLambdas.put(repo, newRandom);
		}
		read.close();
		return selectedLambdas;
	}
}