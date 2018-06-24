import java.net.HttpURLConnection;
import java.net.URL;
import java.net.MalformedURLException;
import java.io.IOException;
import java.io.*;

public class TryApi {

	public static void main(String[] args) {

		try {

			URL url = new URL("https://httpbin.org/get");
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");

			con.setRequestProperty("Content-Type", "application/json");
			String contentType = con.getHeaderField("Content-Type");

			con.setConnectTimeout(5000);
			con.setReadTimeout(5000);

			int status = con.getResponseCode();
			System.out.println("Status code is " + status);

			BufferedReader in = new BufferedReader(
				new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer content = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				content.append(inputLine);
			}
			in.close();

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