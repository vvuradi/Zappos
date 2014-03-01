package onSale;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.Properties;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


@SuppressWarnings("deprecation")
public class Product {
	
	private String searchPre = "http://api.zappos.com/Search/term/";
	private String searchPost = "?&key=52ddafbe3ee659bad97fcce7c53592916a6bfd73";
	public boolean searchByIdOrName(String nameOrId, String to) throws ClientProtocolException, IOException, IllegalStateException, ParseException{
		
		// variable used to indicate the limit of 20%
		boolean isLimitReached = false;
		nameOrId = URLEncoder.encode(nameOrId);
		
		StringBuilder sb = new StringBuilder(searchPre);
		sb.append(nameOrId).append(searchPost);
		
		Properties props = new Properties();
		props.load(new FileInputStream("config.properties"));
		
		// using http client to call API
		HttpClient client = new DefaultHttpClient();		
        HttpGet request = new HttpGet(sb.toString());
        
        HttpResponse response = client.execute(request);
        
        JSONParser parser=new JSONParser();
        JSONObject jsonObj = (JSONObject)parser.parse(new InputStreamReader(response.getEntity().getContent()));
        
        // parsing through the response for the key "results"
        if (jsonObj != null && jsonObj.containsKey("results")) {
			JSONArray results = (JSONArray) jsonObj.get("results");
			// for each product in the result condition of 20% is checked
			for(Object product : results){
				JSONObject item = (JSONObject)product;
				
				// using the percentOff field in the response
				String off = item.get("percentOff").toString();
				int percentOff = Integer.parseInt(off.split("%")[0]);
				
				if(percentOff >= 20){
					isLimitReached = true;
					StringBuilder message = new StringBuilder();
					message.append("Hello User, <br><br>The product you have been looking has a drop in price of atleast 20%");
					message.append("<br>Product ID/Name is ").append(item.get("productName"));
					message.append("<br>Here is the link to it ").append(item.get("productUrl"));
					
					// calling the sendmail method
					Mail.sendMail(props.getProperty("senderEmail"), "Drop in Price", message.toString(), to);
					break;
				}					
			}						
		}		
		return isLimitReached;
	}	
}
