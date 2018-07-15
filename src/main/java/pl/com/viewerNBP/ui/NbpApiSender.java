package pl.com.viewerNBP.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.vaadin.spring.annotation.SpringComponent;



//@Service
public class NbpApiSender {
//	public NbpApiSender(String baseURL) {
//		super();
//		this.baseURL = baseURL;
//	}
//	private String baseURL;
	
	public NbpApiSender() {
		super();
	}
	
	private static String baseURL = "http://api.nbp.pl/api/exchangerates/tables/a/";
	
	private String sendRequest(String reqAttr) {
		String response = "";

		URL url;
		try {
			url = new URL(baseURL + reqAttr);
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
	
	private String getCurrenciesFromNbp(String startDate, String endDate) {
		return sendRequest("/" + startDate + "/" + endDate);
	}
	
	public JSONArray getJsonArrayCurrencies(String startDate, String endDate) {

		
		return new JSONArray(getCurrenciesFromNbp(startDate, endDate));
		
	}
	
	public List<String> getCurrenciesList(){
		
		List<String> currencyList = new ArrayList<String>();

		JSONArray responseJsonArray = new JSONArray(sendRequest(""));
		JSONArray currencyNbpArray = responseJsonArray.getJSONObject(0).getJSONArray("rates");
		for (int i = 0; i < currencyNbpArray.length(); i++) {
			currencyList.add(currencyNbpArray.getJSONObject(i).getString("currency"));
			}
		
		return currencyList;
	}
	
	public List<CurrenciesModel> getAllFromNbp(LocalDate startDate, LocalDate endDate) {
		
//		if(endDate.isBefore(startDate) && )
		
		
		
		List<CurrenciesModel> result = new LinkedList();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MMM-dd");
		
		JSONArray resultJsonArray = getJsonArrayCurrencies(startDate.toString(),endDate.toString());
		for(int i=0; i<resultJsonArray.length(); i++) {
			JSONArray ratesJsonArray = resultJsonArray.getJSONObject(i).getJSONArray("rates");
			LocalDate day = null;
//			try {
//				day = sdf.parse(resultJsonArray.getJSONObject(i).getString("effectiveDate"));
				day = LocalDate.parse(resultJsonArray.getJSONObject(i).getString("effectiveDate"), formatter);
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			for(int j=0; j<ratesJsonArray.length(); j++) {
				
				result.add(new CurrenciesModel(day, ratesJsonArray.getJSONObject(j).getString("currency"), ratesJsonArray.getJSONObject(j).getDouble("mid")));
			}
		}
		
		return result;
	}

	public static String getBaseURL() {
		return baseURL;
	}

	public static void setBaseURL(String baseURL) {
		NbpApiSender.baseURL = baseURL;
	}
	
	
	
}
