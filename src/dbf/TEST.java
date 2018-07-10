package dbf;

import java.util.ArrayList;
import java.util.Properties;
import java.io.File;
import java.net.URL;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.charset.Charset;

public class TEST {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
		
		JsonNavigator jT = new JsonNavigator("./res/dbJobs.000.json");
		
		File tkFile = null;
		
		try {
			tkFile = new File("./res/title-keys.txt");
		} catch(Exception e) {
			System.out.println(e.getMessage());
			System.out.println(e.getStackTrace());
			e.printStackTrace();
		}
		
		ArrayList<String> jobTitleKeys = new ArrayList<String>();
		jT.initPostData(tkFile, jobTitleKeys);

	}

}


