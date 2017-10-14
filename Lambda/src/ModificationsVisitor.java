

import java.io.IOException;
import java.util.List;

import org.repodriller.domain.Commit;
import org.repodriller.domain.Modification;
import org.repodriller.persistence.PersistenceMechanism;
import org.repodriller.scm.CommitVisitor;
import org.repodriller.scm.RepositoryFile;
import org.repodriller.scm.SCMRepository;

public class ModificationsVisitor implements CommitVisitor {

    private Parse parser;
    
    public ModificationsVisitor() {
        this.parser = new Parse();
    }
    
    @Override
    public void process(SCMRepository repo, Commit commit, PersistenceMechanism writer) {
        
    	for(Modification m : commit.getModifications()) {
            String source = m.getSourceCode();
            int lambdas = 0;
            try {
                lambdas = parser.countLambdas(source);
            } catch (IOException e) {
                e.printStackTrace();
            }
            writer.write(
            		"MOD",
                    commit.getDate().getTimeInMillis(),
                    m.getType(),
                    m.getOldPath(),
                    m.getNewPath(),
                    lambdas
            );
    	}
    	
        }        
}