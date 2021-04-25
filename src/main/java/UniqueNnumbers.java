import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class UniqueNnumbers {

	public static void main(String[] args) throws IOException {
		File doneFile = new File("/Users/CS93/ExcelParser/ExcelParser/contacts.txt");
		File newFile = new File("/Users/CS93/ExcelParser/ExcelParser/contacts_foodprovidermobile.txt");
			
		Set<String> doneNumbers = new HashSet<>();
		Scanner myReader = new Scanner(doneFile);
		while (myReader.hasNextLine()) {
			String data = myReader.nextLine();
			doneNumbers.add(data);
		}
		myReader.close();
		System.out.println(doneNumbers);
		
		Set<String> newList = new HashSet<>();
		myReader = new Scanner(newFile);
		while (myReader.hasNextLine()) {
			String data = myReader.nextLine();
			newList.add(data);
		}
		myReader.close();
		System.out.println(newList);
		
		System.out.println(newList.size());
		System.out.println(newList.removeAll(doneNumbers));
		System.out.println(newList.size());
		
		File contacts = new File("contacts_new1.txt");
		contacts.createNewFile();
		FileWriter writer = new FileWriter(contacts);
		for (String phoneNumber : newList) {
			writer.write(phoneNumber + System.lineSeparator());
		}
		writer.close();
	}
	
}
