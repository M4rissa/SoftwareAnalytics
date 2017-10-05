

import org.repodriller.domain.Commit;
import org.repodriller.domain.Modification;
import org.repodriller.persistence.PersistenceMechanism;
import org.repodriller.scm.CommitVisitor;
import org.repodriller.scm.SCMRepository;

public class DiffVisitor implements CommitVisitor {

	@Override
	public void process(SCMRepository repo, Commit commit, PersistenceMechanism writer) {
		
		for(Modification m : commit.getModifications()) {
			writer.write(
					commit.getHash(),
					commit.getCommitterDate().getTime(),
					commit.getAuthor().getName(),
					commit.getCommitter().getName(),
					m.getFileName(),
					m.getType(),
					m.getDiff()
			);
			
		}
	}

}