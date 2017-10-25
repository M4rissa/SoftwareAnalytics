import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//Counts lambda and loc on current state of repo
public class MainBroadMining {
	private FileWriter fw;
	private BufferedWriter bw;
    private Parse parser;

	public MainBroadMining() {
		try {
	        this.parser = new Parse();
			this.fw = new FileWriter("./broad_mining.csv");
			this.bw = new BufferedWriter(fw);
		} 
		catch (final IOException e) {
			throw new ExceptionInInitializerError(e.getMessage());
		}
	}

	public static void main(String[] args) throws IOException {
		MainBroadMining main = new MainBroadMining();
		List<String> repos = new ArrayList<>();
		try {
			File file = new File("repo_names.csv");
			FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				repos.add(line);
			}
			fileReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		for (String repo_path : repos) {
			int lambdas = main.parser.LambdasInDir("./repos_all/" + repo_path);
			int loc = main.parser.LOCInDir("./repos_all/" + repo_path);
			System.out.println(repo_path);
			main.bw.write(repo_path + "," + lambdas + "," + loc + '\n');
		}
		main.bw.close();
		main.fw.close();
	
	}
}
