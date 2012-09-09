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

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class JGlacier {
    private static BufferedReader reader;
   
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {                
        
        try {      
            System.out.println("Welcome to jGlacier " + AppCache.appVersion);
            String credentialsFile = System.getProperty("user.dir") + "/credentials.json";
            
            try {                
                AppCache.LoadCredentialsFromFile(credentialsFile);
            } catch (Exception ex) {
                System.out.println("Error on loading your credentials from file '" + credentialsFile + "'.");
                System.out.println("Please create the credentials file with a valid JSON content!");
                System.out.println("Example: {\"accessKey\":\"*** YOUR ACCESS KEY ***\",\"secretKey\":\"*** YOUR SECRET KEY ***\"}");
                System.exit(0);
            }
            System.out.println("Successfully loaded credentials...");
            reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Please enter the target vault: ");
            String vault = reader.readLine();
            AppCache.archiveManager = new ArchiveManager(vault);
            AppCache.vaultManager = new VaultManager(vault);
            AppCache.jobManager = new JobManager(vault);
            
            
            AppCache.MainMenu = new Menu();
            while (true) {
                AppCache.MainMenu.Print();
                String input = reader.readLine();
                
                try {
                    AppCache.MainMenu.HandleChoosement(Integer.parseInt(input));
                } catch (Exception ex) {
                    System.out.println("Error: " + ex.getMessage());
                }
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }                
    }
}
