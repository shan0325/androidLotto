package com.shan.mylotto.util;

import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public class OpenApiUtil {

    // rest api get 방식 통신
    public static String getRestApiByGetMethod(String paramUrl) {
        StringBuffer resultSb = new StringBuffer();
        BufferedReader reader = null;
        try {
            URL url = new URL(paramUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            if (conn != null) {
                conn.setConnectTimeout(3000); //3초 기다림
                conn.setRequestMethod("GET");

                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        resultSb.append(line);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        Log.i("resultAPI", resultSb.toString());
        return resultSb.toString();
    }
}
