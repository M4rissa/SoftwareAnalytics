import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;
import java.util.List;
import org.apache.commons.io.FileUtils;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.LambdaExpression;



public class Parse {
	public int count = 0;
	public int count_total = 0;
	
	// counts lambdas on file
	public int countLambdas(String str) throws IOException {
		count = 0;
		ASTParser parser = ASTParser.newParser(AST.JLS8);
		parser.setSource(str.toCharArray());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		final CompilationUnit cu = (CompilationUnit) parser.createAST(null);
		cu.accept(new ASTVisitor() {
			public boolean visit(LambdaExpression node) {
				count+=1;
				return false; 
			}
		});
		return count;
	}
	
	//read file content into a string
		public String readFileToString(String filePath) throws IOException {
			StringBuilder fileData = new StringBuilder(1000);
			BufferedReader reader = new BufferedReader(new FileReader(filePath));
			char[] buf = new char[10];
			int numRead = 0;
			while ((numRead = reader.read(buf)) != -1) {
				//			System.out.println(numRead);
				String readData = String.valueOf(buf, 0, numRead);
				fileData.append(readData);
				buf = new char[1024];
			}

			reader.close();
			return  fileData.toString();	
		}
	
		// gives total amount of lambdas in directory
		public int LambdasInDir(String directory) throws IOException{
			count_total = 0;
			File dir = new File(directory);
			String[] extensions = new String[] { "java" };
			List<File> files = (List<File>) FileUtils.listFiles(dir, extensions, true);
			String filePath = null;
			for (File f : files ) {
				filePath = f.getAbsolutePath();
				if(f.isFile()){
					int lambdas_number = countLambdas(readFileToString(filePath));
					count_total = count_total + lambdas_number;
				}
			}
			return count_total;
		}


}

