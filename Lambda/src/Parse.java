import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.LambdaExpression;


public class Parse {
	// static counts and writer, im sure this can be improved
	public int count = 0;
	private FileWriter fw;
	private BufferedWriter bw;

	public Parse() {
//		try {
//			fw = new FileWriter("C:/Users/Justin/SA/results.txt");
//			bw = new BufferedWriter(fw);
//		} 
//		catch (final IOException e) {
//			throw new ExceptionInInitializerError(e.getMessage());
//		}
	}

	//use ASTParse to parse string of files sourcecode
	public void parse(String str) throws IOException {
		ASTParser parser = ASTParser.newParser(AST.JLS8);
		parser.setSource(str.toCharArray());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
	
		final CompilationUnit cu = (CompilationUnit) parser.createAST(null);
		cu.accept(new ASTVisitor() {

			public boolean visit(LambdaExpression node) {
				count+=1;
				// still need to explore the documentation to see what information we can extract from the node
				// ChildPropertyDescriptor body = LambdaExpression.BODY_PROPERTY;
//				boolean l = node.hasParentheses();
//				try {
//					bw.write("Declaration of LAMBDA EXPRESSION, parentheses:" + l + ", at line"
//							+ cu.getLineNumber(node.getStartPosition()) + "\n");
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
				return false; // do not continue 
			}

		});
//		System.out.println(count);
//		bw.write(count + " lambda expressions in "  + "\n");
//		bw.flush();
		// Parse.count = 0;
	}
	
	//use ASTParse to parse string of files sourcecode
	public int countLambdas(String str) throws IOException {
		ASTParser parser = ASTParser.newParser(AST.JLS8);
		parser.setSource(str.toCharArray());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
	
		final CompilationUnit cu = (CompilationUnit) parser.createAST(null);
		cu.accept(new ASTVisitor() {

			public boolean visit(LambdaExpression node) {
				count+=1;
				// still need to explore the documentation to see what information we can extract from the node
				// ChildPropertyDescriptor body = LambdaExpression.BODY_PROPERTY;
				return false; // do not continue 
			}

		});
		return count;
		// Parse.count = 0;
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

	//loop directory of repository to get java files list and parse it
	public void ParseFilesInDir(String directory) throws IOException{
		File dir = new File(directory);
		String[] extensions = new String[] { "java" };
		bw.write("Getting all .java files in " + dir.getCanonicalPath()
		+ " including those in subdirectories \n");
		List<File> files = (List<File>) FileUtils.listFiles(dir, extensions, true);
		bw.write(files.size() + " java files in " + directory + "\n");		
		String filePath = null;
		for (File f : files ) {
			filePath = f.getAbsolutePath();
			if(f.isFile()){
				parse(readFileToString(filePath));
			}
		}
		bw.close();
		fw.close();
	}

	public static void main(String[] args) throws IOException {
		// WHOLE PROJECT
		Parse parse = new Parse();
		parse.ParseFilesInDir("./spring-framework");
		System.out.println(parse.count);
		// SINGLE FILE
		//		parse(readFileToString("./src/SourceAnalysis.java"), "S.java");
	}
}

//Set names = new HashSet();

//public boolean visit(VariableDeclarationFragment node) {
//	SimpleName name = node.getName();
//	this.names.add(name.getIdentifier());
//	System.out.println("Declaration of '" + name + "' at line"
//			+ cu.getLineNumber(name.getStartPosition()));
//	return false; // do not continue 
//}

//public boolean visit(SimpleName node) {
//	if (this.names.contains(node.getIdentifier())) {
//		System.out.println("Usage of '" + node + "' at line "
//				+ cu.getLineNumber(node.getStartPosition()));
//	}
//	return true;
//}