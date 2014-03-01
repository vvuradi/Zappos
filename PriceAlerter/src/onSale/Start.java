package onSale;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

import org.apache.http.client.ClientProtocolException;
import org.json.simple.parser.ParseException;

public class Start {
	public static void main(String[] args) throws ClientProtocolException,
			IOException, IllegalStateException, ParseException,
			InterruptedException {
		
		List<String> emailIds = new ArrayList<String>();
		List<String> productName = new ArrayList<String>();
		Product pro = new Product();
		// used to store the time when the file has to be read again
		long nextFileRead = 0; 
		// used to store the last line no
		int lastReadLine = 0;

		Properties props = new Properties();
		props.load(new FileInputStream("config.properties"));
		String userFile = props.getProperty("user.info.file");
		int pollFile = Integer.parseInt(props.getProperty("poll.file.sec"))*1000;
		int pollInterval = Integer.parseInt(props.getProperty("poll.interval.sec"))*1000;
		
		// this will run always. Can always have a condition for the while loop to stop.
		while(true){			
			long startTime = System.currentTimeMillis();
			
			// user details are read from the user.info.file at the start of the
			// program (nextFileRead is 0) and after every poll.file.sec from the
			// previous read time.
			if (nextFileRead == 0 || startTime > nextFileRead) {				
				File userPref = new File(userFile);
				Scanner sc = new Scanner(userPref);
				
				// skipping already read user data.
				for(int i=0; i<lastReadLine; i++){
					sc.nextLine();
				}
				
				// adding the newly added user info to the array lists
				while (sc.hasNextLine()) {	
					lastReadLine++;
					String[] line = sc.nextLine().split(":");
					emailIds.add(line[0]);
					productName.add(line[1]);
				}
				
				sc.close();
				nextFileRead = System.currentTimeMillis() + pollFile;
			}

			// will look for the price in this loop and sleep for 
			// poll.interval.sec before checking again.
			for (int i = 0; i < emailIds.size(); i++) {
				// api call to search for product using ID or Name
				pro.searchByIdOrName(productName.get(i), emailIds.get(i));
			}
			Thread.sleep(pollInterval);
		}		
	}
}