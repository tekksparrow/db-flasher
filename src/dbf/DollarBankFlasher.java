package dbf;

import java.util.ArrayList;
import java.util.List;

public class DollarBankFlasher {

	public static void main(String[] args) {

		System.out.println("This is the DollarBank Job Flasher Program");

		String curJobFile = "../res/dbJobs.000.json";
		String priJobFile = "../res/dbJobs.001.json";

		ArrayList<String> jobFiles = new ArrayList<String>();
		jobFiles.add(curJobFile);
		jobFiles.add(priJobFile);

		ArrayList<String> curIdList = new ArrayList<String>();
		ArrayList<String> priIdList = new ArrayList<String>();
		List compList = new ArrayList(priIdList);

		for (int i = 0; i < jobFiles.size(); i++) {

			JsonNavigator jsonReader = new JsonNavigator(jobFiles.get(i));

			switch (i) {
				case 0:	 jsonReader.loadIds(curIdList);
						 break;
				case 1:	 jsonReader.loadIds(priIdList);
						 break;
				default: System.out.println("===========Error===========");
			}

			for (String idNum : curIdList) {
				System.out.print(idNum + "\n");
			}
		}

		// Are the json job files the same?
		compList.removeAll(curIdList);
		if (compList.isEmpty()) {
			System.out.println("Same");
		} else {
			System.out.println("Different");
		}

	}
}