

import org.repodriller.domain.Commit;
import org.repodriller.domain.Modification;
import org.repodriller.persistence.PersistenceMechanism;
import org.repodriller.scm.CommitVisitor;
import org.repodriller.scm.SCMRepository;

public class MonthlyCommitsVisitor implements CommitVisitor {

	@Override
	public void process(SCMRepository repo, Commit commit, PersistenceMechanism writer) {
		
		for(Modification m : commit.getModifications()) {
			writer.write(
					commit.getHash(),
					m.getFileName()
			);
			
		}
	}

}