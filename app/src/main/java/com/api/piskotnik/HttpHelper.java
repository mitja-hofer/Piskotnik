package com.api.piskotnik;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class HttpHelper {
    public static String httpPostCall(String requestURL, String token, HashMap<String, String> dataParams) {
        return httpCall(requestURL, "", "POST", 200, dataParams);
    }

    public static String httpGetCall(String requestURL, String token, HashMap<String, String> dataParams) {
        return httpCall(requestURL, token, "GET", 200, dataParams);
    }

    private static String httpCall(String requestURL, String token, String method, int expectedResponseCode, HashMap<String, String> dataParams) {
        URL url;
        String response = "";
        try {
            url = new URL(requestURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(15000);
            conn.setReadTimeout(15000);
            conn.setRequestMethod(method);
            conn.setRequestProperty("Accept", "application/json");
            if (token != "") {
                conn.setRequestProperty("Authorization", "Bearer " + token);
            }

            if (method != "GET") {
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(new JSONObject(dataParams).toString());

                writer.flush();
                writer.close();
                os.close();
            }

            int responseCode = conn.getResponseCode();

            if (responseCode == expectedResponseCode) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    response += line;
                }
            } else {
                response = "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

}
