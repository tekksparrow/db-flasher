package dbf;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.MalformedURLException;
import java.io.IOException;
import java.io.*;

public class LinkedInApiReq {

	String c = 
	String s = 

	public static void main(String[] args) {

		try {

			//LOGIN IN PAGE TEST VIEW DELETE FROM LAST RUN SO ITS FRESH FOR VIEWING ON CURRENT RUN
			File htmlfile = new File("./authpage.html");
			if (htmlfile.delete()) {
				System.out.println("File deleted");
			} else {
				System.out.println("Failed to delete file.");
			}

			//URL url = new URL("https://api.linkedin.com/v1/people/~");
			URL url = new URL("https://www.linkedin.com/oauth/v2/authorization?response_type=code&client_id=yeahrightasshole&redirect_uri=https://localhost");
			
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");

			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestProperty("x-li-format", "json");
			String contentType = con.getHeaderField("Content-Type");

			con.setConnectTimeout(5000);
			con.setReadTimeout(5000);

			int status = con.getResponseCode();
			System.out.println("Status code is " + status);

			BufferedReader in = new BufferedReader(
				new InputStreamReader(con.getInputStream()));
			

			File t1 = new File("./authpage.html");
			FileWriter t11 = new FileWriter(t1, true);

			String inputLine;
			StringBuffer content = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				content.append(inputLine);
				t11.append(inputLine);
			}
			in.close();
			t11.close();

			System.out.println(content);


			con.disconnect();
		}catch(MalformedURLException e) {

		}catch(IOException e) {

		}catch(Exception e) {
			System.out.println(e.getMessage());
			System.out.println(e.getStackTrace());
		}
	}
}