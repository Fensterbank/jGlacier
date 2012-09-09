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

import com.amazonaws.services.glacier.model.GetJobOutputRequest;
import com.amazonaws.services.glacier.model.GetJobOutputResult;
import com.amazonaws.services.glacier.model.GlacierJobDescription;
import com.amazonaws.services.glacier.model.ListJobsRequest;
import com.amazonaws.services.glacier.model.ListJobsResult;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import jglacier.classes.Inventory;

public class JobManager {
    private String vaultName;
    private List<GlacierJobDescription> lastRetrievedJobs;
    
    public JobManager(String vaultName) {
        this.vaultName = vaultName;      
    }
    
    public void ListJobs() {
        ListJobsRequest request = new ListJobsRequest()
                .withVaultName(this.vaultName);
        ListJobsResult result = AppCache.getClient().listJobs(request);
        lastRetrievedJobs = result.getJobList();
        
        int i = 1;
        for (GlacierJobDescription job : lastRetrievedJobs) {
            System.out.print(i + "] ");
            if (job.isCompleted()) {
                System.out.print("Completed:\t");                                                
            } else {
                System.out.print("Incompleted:\t");                
            }
            System.out.print(job.getAction());
            System.out.println("\nStarted:\t" + job.getCreationDate());
            
            if (job.isCompleted()) {
                System.out.println("Finished:\t" + job.getCompletionDate());                
            }
            System.out.println("ID:\t\t" + job.getJobId());
            if (job.getJobDescription() != null && !job.getJobDescription().equals("")) {
                System.out.println("Description:\t" + job.getJobDescription());
            }
            System.out.println("______");            
            System.out.println("Status:\t\t" + job.getStatusCode());            
            System.out.println("Message:\t" + job.getStatusMessage());            
            
            System.out.println("");
            i++;
        }
                
    }
    
    public List<GlacierJobDescription> GetAndDisplayCompletedJobs() {
        ListJobsRequest request = new ListJobsRequest()
                .withVaultName(this.vaultName)
                .withCompleted("true");
        ListJobsResult result = AppCache.getClient().listJobs(request);
        lastRetrievedJobs = result.getJobList();
        
        int i = 1;
        for (GlacierJobDescription job : lastRetrievedJobs) {
            if (job.getStatusCode().equals("Succeeded")) {
                System.out.print(i + "]\t");
                System.out.print(job.getAction());            
                System.out.print("\t(Initiated on " + job.getCreationDate() + ")\n");               
                i++;
            }
        }
        System.out.println("");
        return lastRetrievedJobs;
    }
        
    
    public String GetJobResult(String jobId) {
        
        GetJobOutputRequest getJobOutputRequest = new GetJobOutputRequest()
            .withVaultName(this.vaultName)
            .withJobId(jobId);
        GetJobOutputResult jobOutputResult = AppCache.getClient().getJobOutput(getJobOutputRequest);
    
        
        BufferedReader in = new BufferedReader(new InputStreamReader(jobOutputResult.getBody()));   
        
        FileWriter f = null;;
        try {
            File myFile = new File("lastInventory.json");
            if (myFile.exists()) {
                myFile.delete();
            }
            
            f = new FileWriter("lastInventory.json");
        } catch (IOException ex) {
        }
        
        String inputLine;
        
        String jsonOutput;
        jsonOutput = "";
        
        try {
            while ((inputLine = in.readLine()) != null) {
                jsonOutput += inputLine;
                if (f!=null) {
                    f.append(inputLine);
                }                
            }
        } catch (IOException ex) {            
        }
        if (f!=null)  {
            try {
                f.close();
            } catch (IOException ex) {

            }
        }
        return new Inventory(jsonOutput).toString();        
    }
}
