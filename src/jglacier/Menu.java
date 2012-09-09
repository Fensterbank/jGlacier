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

import com.amazonaws.services.glacier.model.GlacierJobDescription;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;

public class Menu {
    private String[] entries;
    private BufferedReader reader;
    
    public Menu() {        
        entries = new String[7];
        entries[0] = "Upload archive";                          // 1
        entries[1] = "Download archive";                        // 2
        entries[2] = "Initiate a new inventory job";            // 3
        entries[3] = "Display the jobs list";                   // 4
        entries[4] = "Display a job output";                    // 5
        entries[5] = "Display last inventory";                  // 6                
        entries[6] = "Exit program";                            // 7
        reader = new BufferedReader(new InputStreamReader(System.in));
    }
    
    public void Print() {
        try {
            Runtime.getRuntime().exec("clear");
        } catch (Exception ex) {            
        }
        System.out.println("\n");
        for (int i = 0; i < entries.length; i++) {
            System.out.println(i+1 + ")\t" + entries[i]);
        }
        System.out.print("Please choose: ");        
    }
    
    
    public void HandleChoosement(int number) throws Exception {
        System.out.println("\n");
        if (number > entries.length) {
            throw new Exception("Please select a valid menu entry!");
        } else {
            switch (number) {
                case 1:                    
                    System.out.print("Please enter file path: ");
                    String fileName = reader.readLine();
                    
                    System.out.print("Please enter archive description: ");
                    String descr = reader.readLine();
                    
                    try {
                        Date startTime = new Date();                        
                        if (AppCache.archiveManager.UploadFile(fileName, descr)) {
                            long diffInSeconds = ((new Date()).getTime() - startTime.getTime()) / 1000;                        
                        
                            System.out.println("Upload completed.\nTime taken:\t" + AppCache.getTimespanString(diffInSeconds) + "\n");
                        } else {
                            System.out.println("Upload failed. Sorry.\n");
                        }
                    } catch (Exception ex) {
                        System.out.println("Error: " + ex.getMessage());
                    }                    
                    break;
                case 2:
                    System.out.println("Sorry, this function is still not implemented. :(");
                    break;
                case 3:
                    AppCache.vaultManager.InitiateInventoryJob();                    
                    break;
                case 4:
                    AppCache.jobManager.ListJobs();                    
                    break;
                case 5:
                    List<GlacierJobDescription> jobs;
                    jobs = AppCache.jobManager.GetAndDisplayCompletedJobs();
                    
                    System.out.print("Select job: ");
                    String id = reader.readLine();
                    
                    try {
                        String jobID = jobs.get(Integer.parseInt(id)-1).getJobId();
                        System.out.append(AppCache.jobManager.GetJobResult(jobID));                                                
                    } catch (Exception ex) {
                        System.out.println("Please choose a valid entry!");
                    }
                    break;
                case 6:
                    try {
                        AppCache.vaultManager.DisplayLastInventory();
                        System.out.println("");
                    } catch (Exception ex) {
                        System.out.println("Sorry, an error occured: " + ex.getMessage());
                    }
                    break;
                case 7:
                    System.exit(0);
                    break;

            }            
        }
    }
}
