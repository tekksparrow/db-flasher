package dbf;
import java.lang.StringBuffer;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.FileReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.io.*;


public class JsonNavigator {

	public JSONArray   jFile;
	public JSONObject  jObject;

	public JsonNavigator(String pathString) {

		JSONParser jParse = new JSONParser();

		try {

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

	public void loadIds(ArrayList<String> listToLoad) {

		for (int i = 0; i < jFile.size(); i++) {

			JSONObject jobby = (JSONObject) jFile.get(i);
			String guid = jobby.get("guid").toString();
			listToLoad.add(guid);			
		}

	}
}