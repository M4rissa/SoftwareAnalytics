import java.util.Calendar;

import org.repodriller.RepositoryMining;
import org.repodriller.Study;
import org.repodriller.filter.commit.OnlyInMainBranch;
import org.repodriller.filter.range.Commits;
import org.repodriller.persistence.csv.CSVFile;
import org.repodriller.scm.GitRepository;

public class ModificationsMain implements Study{

	   private String repo_path;
	   private String csv_path;

	   public ModificationsMain(String repo_path, String csv_path) {
		       this.repo_path = repo_path;
		       this.csv_path = csv_path;
	   }


	@Override
	public void execute() {
		Calendar date = Calendar.getInstance();
		date.set(2014, 1, 1);
		String input = repo_path;
		CSVFile csv = new CSVFile(csv_path);
		new RepositoryMining()
		.in(GitRepository.singleProject(input))
		 .through(Commits.all())
//		.through(Commits.since(date))
		.filters(new OnlyInMainBranch())
		.process(new ModificationsVisitor(), csv)
		.mine();
	}

}
