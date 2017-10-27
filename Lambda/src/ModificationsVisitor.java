

import java.io.IOException;

import org.repodriller.domain.Commit;
import org.repodriller.domain.Modification;
import org.repodriller.persistence.PersistenceMechanism;
import org.repodriller.scm.CommitVisitor;
import org.repodriller.scm.SCMRepository;

public class ModificationsVisitor implements CommitVisitor {

    private Parse parser;
    private int count;
    
    public ModificationsVisitor() {
        this.parser = new Parse();
    }
    
    @Override
    public void process(SCMRepository repo, Commit commit, PersistenceMechanism writer) {
//    	writer.write(
//    			commit.getDate().getTimeInMillis(),
//    			commit.getHash()
//    			);
    	for(Modification m : commit.getModifications()) {
            String source = m.getSourceCode();
            int lambdas = 0;
            try {
                lambdas = parser.countLambdas(source);
                System.out.println(lambdas);
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