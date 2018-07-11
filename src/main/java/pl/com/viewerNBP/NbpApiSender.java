package pl.com.viewerNBP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;




public class NbpApiSender {
	public NbpApiSender(String baseURL) {
		super();
		this.baseURL = baseURL;
	}
	private String baseURL;
	
	public String getStringCurrenciesFromDates(String startDate, String endDate) {
		String response = "";

		URL url;
		try {
			url = new URL(baseURL + "/" + startDate + "/" + endDate);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			
			BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line + "\n");
			}
			br.close();
			response = sb.toString();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return response;
	}
	
	public JSONArray getJsonCurrenciesFromDates(String startDate, String endDate) {

		
		return new JSONArray(getStringCurrenciesFromDates(startDate, endDate));
		
	}
	
	
	
}
