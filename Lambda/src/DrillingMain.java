

import java.util.Calendar;

import org.repodriller.RepoDriller;
import org.repodriller.RepositoryMining;
import org.repodriller.Study;
import org.repodriller.filter.range.Commits;
import org.repodriller.persistence.csv.CSVFile;
import org.repodriller.scm.GitRepository;

public class DrillingMain implements Study{

	public static void main(String[] args) {
		new RepoDriller().start(new DrillingMain());
	}


	@Override
	public void execute() {
		Calendar date = Calendar.getInstance();
		date.set(2014, 0, 1);
		String input = "./git-1";
		CSVFile csv = new CSVFile("./diffs.csv");
		new RepositoryMining()
		.in(GitRepository.singleProject(input))
		.through(Commits.since(date))
		.process(new DiffVisitor(), csv)
		.mine();
	}

}
