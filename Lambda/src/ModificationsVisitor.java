

import java.io.IOException;

import org.repodriller.domain.Commit;
import org.repodriller.domain.Modification;
import org.repodriller.persistence.PersistenceMechanism;
import org.repodriller.scm.CommitVisitor;
import org.repodriller.scm.SCMRepository;

public class ModificationsVisitor implements CommitVisitor {

    private Parse parser;
    
    public ModificationsVisitor() {
        this.parser = new Parse();
    }
    
    @Override
    public void process(SCMRepository repo, Commit commit, PersistenceMechanism writer) {
        for(Modification m : commit.getModifications()) {
            String path = m.getNewPath() !=null && !m.getNewPath().equals("/dev/null") ? m.getNewPath() : m.getOldPath();
            String source = m.getSourceCode();
            int lambdas = 0;
            try {
                lambdas = parser.countLambdas(source);
            } catch (IOException e) {
                e.printStackTrace();
            }
            writer.write(
                    commit.getDate().getTimeInMillis(),
                    m.getType(),
                    path,
                    lambdas
            );
            
        }
    }
}