
public class Person {
	public static Enum Sex;
	int age;
	String email;
	
	public Person(int age) {
		age = age;
		email = "";
	}

	public String printPerson() {
		return "";
	}
	
	public int getAge() {
		return age;
	}

	public String getEmailAddress() {
		return email;
	}
	
	public String getGender() {
		return "MALE";
	}
}
