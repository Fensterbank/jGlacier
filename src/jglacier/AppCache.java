/*
 * jGlacier
 * 
 * Open Source console based JAVA client for Amazon Glacier
 * 2012-09-09
 * 
 * Copyright 2012 by Frédéric Bolvin
 * This software is released under the GPLv3.
 *
 * */

package jglacier;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.glacier.AmazonGlacierAsyncClient;
import com.amazonaws.services.glacier.AmazonGlacierClient;
import com.amazonaws.util.json.JSONException;
import com.amazonaws.util.json.JSONObject;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class AppCache {
    public static final String appVersion = "0.1";
    public static ArchiveManager archiveManager;
    public static VaultManager vaultManager;
    public static JobManager jobManager;
    public static Menu MainMenu;
    
    private static AWSCredentials credentials;
    private static AmazonGlacierClient client;
    private static AmazonGlacierAsyncClient asyncClient;
    
     
    public static void setCredentials(String accessKey, String secretKey) {
        AppCache.credentials = new BasicAWSCredentials(accessKey, secretKey);
        
        // We have credentials, so initialize client now
        client = new AmazonGlacierClient(credentials);
        client.setEndpoint("https://glacier.eu-west-1.amazonaws.com/");
        
        asyncClient = new AmazonGlacierAsyncClient(credentials);
        asyncClient.setEndpoint("https://glacier.eu-west-1.amazonaws.com/");
    }
    
    public static AWSCredentials getCredentials() {
        return AppCache.credentials;
    }
    
    public static AmazonGlacierClient getClient() {
        return AppCache.client;
    }
    
    public static AmazonGlacierAsyncClient getAsyncClient() {
        return AppCache.asyncClient;
    }
    
    public static JSONObject parseJSON(String text) throws JSONException {
        JSONObject json = new JSONObject(text);
        return json;
    }
    
    public static String getFileSizeString(long bytes) {        
        bytes = bytes / 1024;
        Double d = (double)bytes;
        if (bytes < 1024) {
            return bytes + " KiB";
        } else {
            bytes = bytes / 1024;
            if (bytes < 1024) {
                return bytes + " MiB";
            } else {
                bytes = bytes / 1024;
                if (bytes < 1024) {
                    return bytes + " GiB";
                } else {
                    bytes = bytes / 1024;            
                    return bytes + " TiB";
                }                                        
            }
        }
    }
    
    public static String getTimespanString(long diffInSeconds) {
            long diff[] = new long[] { 0, 0, 0, 0 };
            /* sec */diff[3] = (diffInSeconds >= 60 ? diffInSeconds % 60 : diffInSeconds);
            /* min */diff[2] = (diffInSeconds = (diffInSeconds / 60)) >= 60 ? diffInSeconds % 60 : diffInSeconds;
            /* hours */diff[1] = (diffInSeconds = (diffInSeconds / 60)) >= 24 ? diffInSeconds % 24 : diffInSeconds;
            /* days */diff[0] = (diffInSeconds = (diffInSeconds / 24));
            return String.format(
                "%d day%s, %d hour%s, %d minute%s, %d second%s",
                diff[0],
                diff[0] > 1 ? "s" : "",
                diff[1],
                diff[1] > 1 ? "s" : "",
                diff[2],
                diff[2] > 1 ? "s" : "",
                diff[3],
                diff[3] > 1 ? "s" : "");
    }
    
    public static void LoadCredentialsFromFile(String fileName) throws FileNotFoundException, IOException, JSONException {    
        
        FileReader fr = new FileReader(fileName);
        BufferedReader br = new BufferedReader(fr);
        
        String jsonText;
        jsonText = "";
        String line;
        
        while ((line = br.readLine()) != null) {
            jsonText += line;
        }
        JSONObject res = new JSONObject(jsonText);
        AppCache.setCredentials(res.getString("accessKey"), res.getString("secretKey"));
    }
}
