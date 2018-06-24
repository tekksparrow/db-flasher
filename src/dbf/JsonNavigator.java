package dbf;
import java.io.*;
import java.lang.StringBuffer;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.*;


public class JsonNavigator {

	public JSONArray   jFile;
	public JSONObject  jObject;

	public JsonNavigator(String pathString) {

		JSONParser jParse = new JSONParser();

		try {

			jFile = (JSONArray) jParse.parse(new FileReader(pathString));

		}catch(FileNotFoundException e) {

		}catch(IOException e) {

		}catch(ParseException e) {

		}catch(Exception e) {
			System.out.println(e.getMessage());
			System.out.println(e.getStackTrace());
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

	public void loadIds(ArrayList<String> listToLoad) {

		for (int i = 0; i < jFile.size(); i++) {

			JSONObject jobby = (JSONObject) jFile.get(i);
			String guid = jobby.get("guid").toString();
			listToLoad.add(guid);			
		}

	}
}