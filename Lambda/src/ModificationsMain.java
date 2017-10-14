import java.util.Calendar;

import org.repodriller.RepoDriller;
import org.repodriller.RepositoryMining;
import org.repodriller.Study;
import org.repodriller.filter.commit.OnlyInMainBranch;
import org.repodriller.filter.range.Commits;
import org.repodriller.persistence.csv.CSVFile;
import org.repodriller.scm.GitRepository;

public class ModificationsMain implements Study{

	public static void main(String[] args) {
		new RepoDriller().start(new ModificationsMain());
	}


	@Override
	public void execute() {
		Calendar date = Calendar.getInstance();
		date.set(2017, 0, 1);
		String input = "./repos/RxJava";
		CSVFile csv = new CSVFile("./RxJava_commits.csv");
		new RepositoryMining()
		.in(GitRepository.singleProject(input))
		 .through(Commits.all())
//		.through(Commits.since(date))
		.filters(new OnlyInMainBranch())
		.process(new ModificationsVisitor(), csv)
		.mine();
	}

}
