

import java.io.IOException;
import java.util.List;

import org.repodriller.domain.Commit;
import org.repodriller.domain.Modification;
import org.repodriller.persistence.PersistenceMechanism;
import org.repodriller.scm.CommitVisitor;
import org.repodriller.scm.RepositoryFile;
import org.repodriller.scm.SCMRepository;

public class ModificationsVisitorNew implements CommitVisitor {

    private Parse parser;
    private int commit_count;
    
    public ModificationsVisitorNew() {
        this.parser = new Parse();
        this.commit_count = 0; 
    }
    
    @Override
    public void process(SCMRepository repo, Commit commit, PersistenceMechanism writer) {
       
    	commit_count++;
        
        if (commit_count % 100 == 0){
        	try {
    			repo.getScm().checkout(commit.getHash());
    			List<RepositoryFile> files = repo.getScm().files();    			
    			for(RepositoryFile file : files) {
    				if(!file.fileNameEndsWith("java")){
    	  				writer.write(
        	    				"UPD",
        	                    commit.getDate().getTimeInMillis(),
        	    				file.getFullName(),
        	    				0,
        	    				"",
        	    				""
        	    			);
    				}
    				else{
    	   				String source = file.getSourceCode();
        				int lambdas = 0;
						try {
							lambdas = parser.countLambdas(source);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
        				writer.write(
        	    				"UPD",
        	                    commit.getDate().getTimeInMillis(),
        	    				file.getFullName(),
        	    				lambdas,
        	    				"",
        	    				""
        	    			);
    				}

    			}
    				
    		}  finally {
    			repo.getScm().reset();
    		}	
        }
        
        else{
        
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
        
}