package dbf;

import java.util.ArrayList;
import java.util.Properties;
import java.io.File;
import java.net.URL;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.charset.Charset;

import java.lang.Integer;


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
	
	public static JSONArray readJsonFromLocal(String path) throws IOException, ParseException{

		File initJSON = new File("./res/test-input.json");
		InputStream is = new FileInputStream(initJSON);
		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
			String jsonText = readAll(rd);
			JSONParser parser = new JSONParser();
			JSONArray json = (JSONArray) parser.parse(jsonText);
			return json;
		} finally {
			is.close();
		}
	}

	public static void main(String[] args) {

		System.out.println("TEST RUNS PROD ???");

		// Config cfg = new Config();
		BufferedReader reader1 = null;

		int prevFileNum = 0;
		int currFileNum = 0;

		boolean firstRun = false;
		
		InputStream in = null;
		Properties p = new Properties();
		ArrayList<String> keywords = null;
		
		File file = new File(".");
		for(String fileNames : file.list()) System.out.println(fileNames);

		try {
			
			in = new FileInputStream("ntrack/config.properties");			
			p.load(in);
			
			// Check run mode and Get JSONArray
			String runMode = p.getProperty("runMode");
			
			JSONArray json = null;

			System.out.println("++++++++" + p.getProperty("testFileP"));
			
			switch (runMode) {
				case "Offline": 
							
							json = readJsonFromLocal(p.getProperty("testFileP"));
			                break;
			                
				case "Online": json = readJsonFromUrl(p.getProperty("inputURL"));
            				break;			
			}
			
			keywords = new ArrayList<String>();
			String kwPath = p.getProperty("keywordsPath");
			File f1 = new File(kwPath);
			FileReader fr = new FileReader(f1);
			BufferedReader br = new BufferedReader(fr);
			String line;
			
			int kCount = 0;
			while ((line = br.readLine()) != null) {	
				System.out.println("adding " + line);
				kCount ++;
				keywords.add(line);
			}
			System.out.println(Integer.toString(kCount));
			fr.close();
			
			// Create new file name
			System.out.println("Creating counter path file");
			StringBuilder sb = new StringBuilder();
			String counterloc = p.getProperty("counterPath");
			System.out.println(counterloc);

			// Extract counter value
			File cntFile = new File(counterloc);
			reader1 = new BufferedReader(new FileReader(cntFile));
			String fileNumber = reader1.readLine();
			System.out.println(fileNumber);
			
			// Determine if this is first run
			if (Integer.valueOf(fileNumber) > -1) {
				prevFileNum += Integer.parseInt(fileNumber);
				currFileNum += prevFileNum + 1;
			} else {
				System.out.println("First run, using current as previous.");
				firstRun = true;
				prevFileNum = 0;
				currFileNum = 0;
			}

// *********************************************************************//
//																		//
//	CREAT THE DBJOBS## CURRENT FILE 						 			//
//																		//
// *********************************************************************//

			// Create current file
			System.out.println ("firstRun=" + firstRun);
			sb.append("./res/dbJobs.");			
			sb.append(String.format("%03d", currFileNum)).append(".json");
			
			FileWriter f = new FileWriter(sb.toString());
			f.write(json.toString());
			f.close();
			System.out.println ("curr file result" + sb.toString());


			// Update counter
			FileWriter cntWrite = new FileWriter(counterloc);
			cntWrite.write(Integer.toString(currFileNum));
			cntWrite.close();

		}catch(IOException e) {
			System.out.println("IOException dbf");
			System.out.println(e.getMessage());
			System.out.println(e.getStackTrace());
			e.printStackTrace();
		}catch(ParseException e) {
			e.printStackTrace();
		}catch(Exception e) {
			System.out.println("General Exception");
			System.out.println(e.getMessage());
			System.out.println(e.getStackTrace());
			e.printStackTrace();
		}

		StringBuilder currJobFile = new StringBuilder("./res/dbJobs.");
		currJobFile.append(String.format("%03d", currFileNum)).append(".json");

		StringBuilder prevJobFile = new StringBuilder("./res/dbJobs.");
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


		JsonNavigator jsonReader;
		for (int i = 0; i < jobFiles.size(); i++) {

			System.out.println("going " + i);

			jsonReader = new JsonNavigator(jobFiles.get(i));

			switch (i) {
				case 0:	 jsonReader.loadIds(curIdList);
						 break;
				case 1:	 jsonReader.loadIds(priIdList);
						 compList = priIdList;
						 break;
				default: System.out.println("===========Error===========");
			}
		}

// *********************************************************************//
//																		//
//	ARE THE JSON FILES THE SAME ????									//
//																		//
// *********************************************************************//
		if (compList.isEmpty()) {
			System.out.println("comp is empty before check");
		}
		if (!firstRun) {
			compList.removeAll(curIdList);
		}
		if (compList.isEmpty()) {
			System.out.println("Same");
		} else {
			System.out.println("Different");
			StringBuilder sb = new StringBuilder();
			JsonNavigator jobReader = new JsonNavigator(jobFiles.get(0));
			JsonNavigator jobReader1 = new JsonNavigator(jobFiles.get(1));
			
// *********************************************************************//
//																		//
//	FILES ARE DIFFERENT, PRINT CONTENTS									//
//																		//
// *********************************************************************//			
			System.out.println("CURRENT");
			System.out.println(jobFiles.get(0));
			System.out.println(jobReader.toString());
			System.out.println("-------------------------------------------------");
			System.out.println("PREVIOUS");
			System.out.println(jobReader1.toString());
			System.out.println(jobReader1.jFile.size());
			
			
// *********************************************************************//
//																		//
//	READ FILE CONTAINING PAST JOB GUIDS									//
//																		//
// *********************************************************************//			
			FileWriter fw = null;
			File cp = null;
			try {
				fw = new FileWriter(new File(p.getProperty("pastFilePath")));
			} catch (IOException e1) {
				e1.printStackTrace();
			}

// *********************************************************************//
//																		//
//	FOR EACH GUID IN OUR COMPLIST (DIFFERENCE BETWEEN CURRENT 			//
//  RUN FILE AND PREVIOUS RUN FILE) ADD THIS TO THE PAST JOBS FILE/LIST //
//																		//
// *********************************************************************//
			
			for (String newGuid : compList) {
				//TODO load a list of seen jobs here so we can use inspect instead of more for loops

				//Record GUID as one that has been seen before
				try {
					fw.append(newGuid);
					fw.append("\n");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			try {
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

// *********************************************************************//
//																		//
//	JOBREADER1 IS FULL OF JOB POSTS. NEED TO EXTRACT THE TECH JOBS 		//
// 	AND BUILD POSTS FOR THEM.											//
//																		//
// *********************************************************************//			
				
				// Get info
				boolean techJobMatch = false;
				
				System.out.println("The size is " + jobReader.jFile.size());
				
				for (int i = 0; i < jobReader.jFile.size(); i++) {
					
					System.out.println(i + "-");
					
					JSONObject jobObj = jobReader.getObject(i);
					
					if (techJobMatch) {
						techJobMatch = false;
						continue;
					}

					String str = jobObj.get("title").toString();
					String[] splitr = str.split(" ");
					
					for (String x : splitr) {
						System.out.print(x + " ");
					}
					System.out.print("\n");
					
					FileWriter cpw;
					File cps;
					FileWriter npw;
					File nps;
					
					try {
						cps = new File(p.getProperty("currentPostsFile"));
						cpw = new FileWriter(cps, true);
						nps = new File(p.getProperty("newPostsFile"));
						npw = new FileWriter(nps, true);
						for (String s : splitr) {
							System.out.println("       looking for " + s);
							if (keywords.contains(s.toUpperCase())) {
								System.out.println("word Match");
								System.out.println(s);
								System.out.println("JOB TITLE");
								System.out.println(jobObj.get("title").toString());
								System.out.println(jobObj.toString());
								
								techJobMatch = true;
								
								cpw.append("#\n");
								cpw.append(jobObj.get("url").toString() + "\n");
								cpw.append(jobObj.get("title").toString() + "\n");
								cpw.append(jobObj.get("company").toString() + "\n");
								cpw.append(jobObj.get("state").toString() + "\n");
								cpw.append(jobObj.get("city").toString() + "\n");
								cpw.close();
// *********************************************************************//
//																		//
//	POST CREATION SHOULD BE MODULARIZED; GREETING/JOB/CLOSER	 		//
//																		//
// *********************************************************************//		
								npw.append("#\n");
								npw.append("Dollar Bank IT Employment Opportunity!!!\n");
								npw.append(jobObj.get("title").toString() + "\n");
								npw.append(jobObj.get("state").toString() + ", " + 
										jobObj.get("city").toString() + "\n");
								npw.append(jobObj.get("url").toString() + "\n\n");
								npw.append("This post was generated automatically by a db-flasher. The code is available in my github repository under the username tekksparrow.\n");
								npw.close();
								
							}
						}
						
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			
//			try {
//				File pastJobs = new File("./res/pastJobs.txt");
//				BufferedReader br = new BufferedReader(new FileReader(pastJobs));
//				String line = null;
//				
//				while ((line = br.readLine().trim()) != null) {	
//					br.readLine().trim();
//						
//					boolean freshJob = true;
//					boolean techJob = false;
//					JsonNavigator jobReader = new JsonNavigator(jobFiles.get(0));
//					
//					for (String compJob : compList) {
//						if (compJob.equalsIgnoreCase(line)) {
//							freshJob = false;
//						} else if (jobReader.contains(compJob)) {
//							techJob = true;
//						}
//						
//						if (freshJob && techJob) {
//							jobReader.toStringJob(compJob.toString());
//						}
//				
//					}
//				
//				} 
//				
//			} catch(IOException e) {
//				System.out.println(e.getMessage());
//				System.out.println(e.getStackTrace());
//				e.printStackTrace();
//			}
		}

	}
}