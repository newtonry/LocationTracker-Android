//package com.fadetoproductions.rvkn.locationtracker;
//
//import android.util.Log;
//
//import java.io.BufferedWriter;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.io.OutputStream;
//import java.io.OutputStreamWriter;
//import java.net.MalformedURLException;
//import java.net.URL;
//
//import java.net.HttpURLConnection;
//
//
///**
// * Created by rnewton on 1/16/16.
// */
//public class ParseClient {
//
//
//
//
//
//    public static void postData() throws IOException {
//
////        URL url = new URL("http://ryan.fadetoproductions.com");
////        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
////        InputStream in = conn.getInputStream();
////        InputStreamReader isw = new InputStreamReader(in);
////
////        int data = isw.read();
////        while (data != -1) {
////            char current = (char) data;
////            data = isw.read();
////            System.out.print(current);
////        }
////
//
//
//        Log.d("DEBUG", String.valueOf(data));
//
////        conn.setReadTimeout(10000);
////        conn.setConnectTimeout(15000);
//
////
////
////        conn.setRequestMethod("POST");
////
////
////
////        conn.setDoInput(true);
////        conn.setDoOutput(true);
//
////        List<NameValuePair> params = new ArrayList<NameValuePair>();
////        params.add(new BasicNameValuePair("firstParam", paramValue1));
////        params.add(new BasicNameValuePair("secondParam", paramValue2));
////        params.add(new BasicNameValuePair("thirdParam", paramValue3));
//
////        OutputStream os = conn.getOutputStream();
////        BufferedWriter writer = new BufferedWriter(
////                new OutputStreamWriter(os, "UTF-8"));
////        writer.write(getQuery(params));
////        writer.flush();
////        writer.close();
////        os.close();
////
////        conn.connect();
//
//    }
//
//
//}
