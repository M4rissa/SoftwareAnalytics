import java.awt.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class Test {
	public static void main(String[] args) {
		Iterable<Person> roster = (Iterable<Person>) new List();
//		test(1);
		processElements(
			    roster,
			    p -> p.getGender().equals("MALE")
			        && p.getAge() >= 18
			        && p.getAge() <= 25,
			    p -> p.getEmailAddress(),
			    email -> System.out.println(email)
			);
	}
	
	public static void test(int a) {
		
	}
	
	public static <X, Y> void processElements(
		    Iterable<X> source,
		    Predicate<X> tester,
		    Function <X, Y> mapper,
		    Consumer<Y> block) {
		    for (X p : source) {
		        if (tester.test(p)) {
		            Y data = mapper.apply(p);
		            block.accept(data);
		        }
		    }
		}
}
