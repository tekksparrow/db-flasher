package dbf;

import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.net.URL;
import java.net.URLConnection;

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
import java.io.FileWriter;
import java.nio.charset.Charset;


public class DollarBankFlasher {

	private static String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}

	public static JSONArray readJsonFromUrl(String url) throws IOException, ParseException{

		InputStream is = new URL(url).openStream();
		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
			String jsonText = readAll(rd);
			JSONParser parser = new JSONParser();
			JSONArray json = (JSONArray) parser.parse(jsonText);
			// JSONObject json = new JSONObject(jsonText);
			return json;
		} finally {
			is.close();
		}
	}

	public static void main(String[] args) {

		Config cfg = new Config();
		BufferedReader reader1 = null;

		int prevFileNum = 0;
		int currFileNum = 0;

		boolean firstRun = false;

		try {
			// Get JSONArray
			String url = cfg.getProperty("mInputJson");
			System.out.println("URL");
			System.out.println(url);
			JSONArray json = readJsonFromUrl(url);

			// Create new file name
			StringBuilder sb = new StringBuilder();
			String counterloc = cfg.getProperty("mInputCounterLoc");

			// Extract counter value
			File cntFile = new File(counterloc);
			reader1 = new BufferedReader(new FileReader(cntFile));
			String fileNumber = reader1.readLine();
			
			// Determine if this is first run
			if (Integer.valueOf(fileNumber) == -1) {
				System.out.println("First run, using current as previous.");
				firstRun = true;
				prevFileNum = 0;
				currFileNum = 0;
			} else {
				prevFileNum += Integer.parseInt(fileNumber);
				currFileNum += prevFileNum + 1;
			}

			// Create current file
			sb.append("../res/dbJobs.");			
			sb.append(String.format("%03d", currFileNum)).append(".json");
			
			FileWriter f = new FileWriter(sb.toString());
			f.write(json.toString());
			f.close();

			// Update counter
			FileWriter cntWrite = new FileWriter(counterloc);
			cntWrite.write(Integer.toString(currFileNum));
			cntWrite.close();

		}catch(IOException e) {
			System.out.println("IOException");
			System.out.println(e.getMessage());
			System.out.println(e.getStackTrace());
		}catch(ParseException e) {

		}catch(Exception e) {
			System.out.println("General Exception");
			System.out.println(e.getMessage());
			System.out.println(e.getStackTrace());
			e.printStackTrace();
		}

		StringBuilder currJobFile = new StringBuilder("../res/dbJobs.");
		currJobFile.append(String.format("%03d", currFileNum)).append(".json");

		StringBuilder prevJobFile = new StringBuilder("../res/dbJobs.");
		if (firstRun) {
			prevJobFile = currJobFile;
		} else {
			prevJobFile.append(String.format("%03d", prevFileNum)).append(".json");
		}

		System.out.println("current");
		System.out.println(currFileNum);
		System.out.println("previous");
		System.out.println(prevFileNum);

		ArrayList<String> jobFiles = new ArrayList<String>();
		jobFiles.add(currJobFile.toString());
		jobFiles.add(prevJobFile.toString());

		ArrayList<String> curIdList = new ArrayList<String>();
		ArrayList<String> priIdList = new ArrayList<String>();
		ArrayList<String> compList = new ArrayList<String>();

		// for (String idNum : compList) {
		// 	System.out.print(idNum + "\n");
		// }


		for (int i = 0; i < jobFiles.size(); i++) {

			System.out.println("going " + i);

			JsonNavigator jsonReader = new JsonNavigator(jobFiles.get(i));

			switch (i) {
				case 0:	 jsonReader.loadIds(curIdList);
						 break;
				case 1:	 jsonReader.loadIds(priIdList);
						 compList = priIdList;
						 
						 break;
				default: System.out.println("===========Error===========");
			}

			// for (String idNum : curIdList) {
			// 	System.out.print(idNum + "\n");
			// }
		}

		// Are the json job files the same?
		if (compList.isEmpty()) {
			System.out.println("comp is empty before check");
		}
		compList.removeAll(curIdList);
		if (compList.isEmpty()) {
			System.out.println("Same");
		} else {
			System.out.println("Different");
		}

	}
}