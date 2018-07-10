package dbf;
import java.lang.StringBuffer;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.FileReader;
import java.io.*;


public class JsonNavigator {

	public JSONArray    	 jFile;
	public JSONObject   	 jObject;
	public File         	 keywordFile;  
	public ArrayList<String> jobTitleKeys;
	

	public JsonNavigator(String pathString) {

		JSONParser jParse = new JSONParser();

		try {

			System.out.println("path string " + pathString);
			jFile = (JSONArray) jParse.parse(new FileReader(pathString));

		}catch(FileNotFoundException e) {
			System.out.println("General Exception");
			System.out.println(e.getMessage());
			System.out.println(e.getStackTrace());
			e.printStackTrace();

		}catch(IOException e) {
			System.out.println("General Exception");
			System.out.println(e.getMessage());
			System.out.println(e.getStackTrace());
			e.printStackTrace();

		}catch(ParseException e) {
			System.out.println("General Exception");
			System.out.println(e.getMessage());
			System.out.println(e.getStackTrace());
			e.printStackTrace();

		}catch(Exception e) {
			System.out.println("General Exception");
			System.out.println(e.getMessage());
			System.out.println(e.getStackTrace());
			e.printStackTrace();
		}

	}

	public String toString() {

		int counter = 1;
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < jFile.size(); i++) {

			JSONObject jobby = (JSONObject) jFile.get(i);
			String title = jobby.get("title").toString();
			sb.append(Integer.toString(counter) + ": " + title.toString() + "\n");
			counter ++;
			
		}

		return sb.toString();
	}
	
	public void toStringJob(String id) {

		for (int i = 0; i < this.jFile.size(); i++) {

			JSONObject jobby = (JSONObject) jFile.get(i);
			String guid = jobby.get("guid").toString();
			if (guid.equalsIgnoreCase(id)) {
				System.out.println(jobby.get("title").toString());
			}
			
		}

	}
	
	public JSONObject getObject(int idx) {

		JSONObject jobby = (JSONObject) jFile.get(idx);
		return jobby;

	}
	
	public String getElement(String element) {

		String result = null;
		JSONObject jobby = (JSONObject) jFile.get(0);
		result = jobby.get(element).toString();
		return result;
	}

	public void loadIds(ArrayList<String> listToLoad) {

		for (int i = 0; i < jFile.size(); i++) {

			JSONObject jobby = (JSONObject) jFile.get(i);
			String guid = jobby.get("guid").toString();
			listToLoad.add(guid);			
		}

	}
	
	
	public ArrayList<String> initPostData(File keywordFile, ArrayList<String> jobTitleKeys) {
		ArrayList<String> result = null;
		
		try (BufferedReader br = new BufferedReader(new FileReader(keywordFile))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		       jobTitleKeys.add(line.trim());
		    }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for (String x : jobTitleKeys) {
			System.out.println(x);
		}
		
		return result;
	}
	
	public String postGenerator(ArrayList<String> listToLoad) {
		String result = null;
		
//		JSONObject jobby = (JSONObject) jFile.get(i);
//		String guid = jobby.get("guid").toString();
//		listToLoad.add(guid);
		
		return result;
	}
	
	public boolean contains(String target) {
		
		for (int i = 0; i < this.jFile.size(); i++) {
			JSONObject jobEntry = (JSONObject) jFile.get(i);

			if (jFile.get(i).toString().equals(target)) {
				System.out.println(jFile.get(i).toString());
				return true;
			}
					
		}
		
		return false;
	}
	
}