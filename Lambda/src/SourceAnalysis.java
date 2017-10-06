import java.io.IOException;

public class SourceAnalysis {
	public interface MyInterface {//define Functional Interface (SAM)
	    public int someMethod(int a);
	}


	public static void main(String[] args) throws IOException {

		MyInterface myInterface = (int a) -> a +5;//assign the expression to SAM
        int output = myInterface.someMethod(20); //returns 25
		System.out.println(output);

	}

}


