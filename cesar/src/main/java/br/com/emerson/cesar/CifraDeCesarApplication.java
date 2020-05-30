package br.com.emerson.cesar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CifraDeCesarApplication {

	private static final Logger LOGGER = LogManager.getLogger(CifraDeCesarApplication.class);

	private static final String TOKEN_VALUE = System.getenv("TOKEN");

	public static void main(String[] args) {
		LOGGER.info("OLÁ LOGGER");
		try {
			connect();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			CifraDeCesarApplication.LOGGER.error("Erro {}", e);
		}
	}

	public static void connect() throws IOException {
		URL url = new URL("https://api.codenation.dev/v1/challenge/dev-ps/generate-data?token=" + TOKEN_VALUE);
		HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		LOGGER.info(con.getURL().toString());
		Map<String, String> parameters = new HashMap<>();
		parameters.put("token", "002d3ddc981008a130773f0fa5adda45d4b332b4");

		con.setDoOutput(true);
		// DataOutputStream out = new DataOutputStream(con.getOutputStream());
		// out.writeBytes(ParameterStringBuilder.getParamsString(parameters));
		// con.setRequestProperty("token", "002d3ddc981008a130773f0fa5adda45d4b332b4");
		// out.flush();
		// out.close();

		LOGGER.info(con.getURL().toString());
		con.connect();
		int status = con.getResponseCode();
		System.out.println(String.valueOf(status));
		Reader streamReader = null;

		if (status > 299) {
			streamReader = new InputStreamReader(con.getErrorStream());
		} else {
			streamReader = new InputStreamReader(con.getInputStream());
		}
		LOGGER.info("status: {}", status);
		BufferedReader in = new BufferedReader(streamReader);
		String inputLine;
		StringBuffer content = new StringBuffer();
		while ((inputLine = in.readLine()) != null) {
			content.append(inputLine);
		}
		in.close();
		LOGGER.info(content.toString());
		con.disconnect();

	}

	static class ParameterStringBuilder {
		public static String getParamsString(Map<String, String> params) throws UnsupportedEncodingException {
			StringBuilder result = new StringBuilder();

			for (Map.Entry<String, String> entry : params.entrySet()) {
				result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
				result.append("=");
				result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
				result.append("&");
			}

			String resultString = result.toString();
			return resultString.length() > 0 ? resultString.substring(0, resultString.length() - 1) : resultString;
		}
	}
}
