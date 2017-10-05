import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.LambdaExpression;
import org.eclipse.jdt.core.dom.SimpleName;
 
public class Main {
 
	//use ASTParse to parse string
//	public static void parse(String str) {
//		ASTParser parser = ASTParser.newParser(AST.JLS8);
//		parser.setSource(str.toCharArray());
//		//parser.setKind(ASTParser.K_COMPILATION_UNIT);
//		parser.setKind(ASTParser.K_STATEMENTS);
// 
//		final CompilationUnit cu = (CompilationUnit) parser.createAST(null);
//		cu.accept(new ASTVisitor() {
// 
//			Set names = new HashSet();
//			
//			public boolean visit(LambdaExpression node) {
//				System.out.println(node.toString());
//				return false;
//			}
//		});
// 
//	}
	
	public static void parse(String str) {
		ASTParser parser = ASTParser.newParser(AST.JLS8);
		parser.setSource(str.toCharArray());
		parser.setKind(ASTParser.K_STATEMENTS);
		Block block = (Block) parser.createAST(null);
		 
		//here can access the first element of the returned statement list
		String string = block.statements().get(0).toString();
 
		System.out.println(string);
 
		block.accept(new ASTVisitor() {
 
			public boolean visit(LambdaExpression node) {
 
				System.out.println(node.toString());
 
				return true;
			}
 
		});
	}
 
	//read file content into a string
	public static String readFileToString(String filePath) throws IOException {
		StringBuilder fileData = new StringBuilder(1000);
		BufferedReader reader = new BufferedReader(new FileReader(filePath));
 
		char[] buf = new char[10];
		int numRead = 0;
		while ((numRead = reader.read(buf)) != -1) {
			//System.out.println(numRead);
			String readData = String.valueOf(buf, 0, numRead);
			fileData.append(readData);
			buf = new char[1024];
		}
 
		reader.close();
 
		return  fileData.toString();	
	}
 
	//loop directory to get file list
	public static void ParseFilesInDir() throws IOException{
		File dirs = new File(".");
		String dirPath = dirs.getCanonicalPath() + File.separator+"src"+File.separator;
 
		File root = new File(dirPath);
		//System.out.println(rootDir.listFiles());
		File[] files = root.listFiles ( );
		String filePath = null;
 
		 for (File f : files ) {
			 filePath = f.getAbsolutePath();
			 if(f.isFile()){
				 parse(readFileToString(filePath));
			 }
		 }
	}
 
	public static void main(String[] args) throws IOException {
		//ParseFilesInDir();
		parse(readFileToString("test2.txt"));
	}
}