import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.repodriller.RepoDriller;

//Uses ModificationMain, ModificationVisitor to preprocess all the repos from the start
public class Main {
	   
		public static void main(String[] args) {
			List<String> repos = new ArrayList<>();

			try {
				File file = new File("repos_names_all.csv");
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
				System.out.println(repo_path);
				new RepoDriller().start(new ModificationsMain("./repos_all/" + repo_path, "./preprocess/" + repo_path + "_mod.csv"));
			}			
		}

}
