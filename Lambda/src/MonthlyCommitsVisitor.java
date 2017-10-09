

import java.io.File;
import java.util.List;

import org.repodriller.domain.Commit;
import org.repodriller.domain.Modification;
import org.repodriller.persistence.PersistenceMechanism;
import org.repodriller.scm.CommitVisitor;
import org.repodriller.scm.RepositoryFile;
import org.repodriller.scm.SCMRepository;

public class MonthlyCommitsVisitor implements CommitVisitor {

	@Override
	public void process(SCMRepository repo, Commit commit, PersistenceMechanism writer) {

		try {
			repo.getScm().checkout(commit.getHash());
			List<RepositoryFile> files = repo.getScm().files();
			for(RepositoryFile file : files) {
				if(!file.fileNameEndsWith("java")) continue;	
				File soFile = file.getFile();
				System.out.println(soFile.getPath());				
			}

		} finally {
			repo.getScm().reset();
		}
	}

}