package AlertInfo;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;

/* This class takes user input until a valid Email id and Product name/Id are entered
 * Stores the details entered by user into a file whose location can be changed using
 * config.properties file. User details are stored in the format "email:product".
 * They are separated using :
 * */

public class SetAlert {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
    	System.out.println("Please enter your Email Id:");
    	String email = sc.next();
    	
    	// email validation is done.
    	while(!email.matches("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}")){
    		System.out.println("Please enter a valid Email Id:");
    		email = sc.next();
    	}
    	
    	System.out.println("Please enter the Product Id or name:");
    	String nameOrId = sc.next();
    	StringBuilder sb = new StringBuilder(email);
    	// colon : is appended between email and product Id/name
    	sb.append(":").append(nameOrId).append("\n");
    	
    	SetAlert alert = new SetAlert();
    	if(!alert.storeDetails(sb.toString())){
    		System.out.println("There is an error in processing the request. Please try again later.");
    	}    	
	}
	
	// method responsible for storing user details into a file
	public boolean storeDetails(String details){
		FileWriter fwrite = null;
		Properties props = new Properties();		
    	try {
    		props.load(new FileInputStream("config.properties"));
    		String file = props.getProperty("user.info.file");
    		fwrite = new FileWriter(file, true);
    		fwrite.write(details);
    		fwrite.close();
		} catch (IOException e) {
			return false;
		}
    	return true;
	}

}
