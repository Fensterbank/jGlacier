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

import com.amazonaws.services.glacier.TreeHashGenerator;
import com.amazonaws.services.glacier.model.UploadArchiveRequest;
import com.amazonaws.services.glacier.model.UploadArchiveResult;
import com.amazonaws.services.glacier.transfer.ArchiveTransferManager;
import com.amazonaws.services.glacier.transfer.UploadResult;
import com.amazonaws.services.glacier.transfer.JobStatusMonitor;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.concurrent.Future;

public class ArchiveManager {
    private String vaultName;
    
    public ArchiveManager(String vaultName) {
        this.vaultName = vaultName;
    }
    
    public Boolean UploadFile(String fileName, String description) throws Exception {
        ArchiveTransferManager atm = new ArchiveTransferManager(AppCache.getClient(), AppCache.getCredentials());
        File fileToUpload = new File(fileName);
        
        
        if (fileToUpload.exists()) {                                    
            System.out.println("Uploading file " + fileToUpload.getName() + " (" + AppCache.getFileSizeString(fileToUpload.length()) + ")");
            System.out.println("Please wait...");
            UploadResult result = atm.upload(vaultName, description, new File(fileName));                              
            if (result!=null) {
                return true;
            } else {
                return false;
            }
        } else {
            throw new Exception("File not found!");
        }        
    }
    
    public Boolean UploadFileAsync(String fileName, String description) throws Exception {
            File fileToUpload = new File(fileName);
            Date startTime = new Date();
            if (fileToUpload.exists()) {                                    
                System.out.println("Uploading file " + fileToUpload.getName() + " (" + AppCache.getFileSizeString(fileToUpload.length()) + ")");
                System.out.println("Please wait...");
 
                
                InputStream is = new FileInputStream(fileToUpload); 
                byte[] body = new byte[(int) fileToUpload.length()];
                is.read(body);
                                    
                // Send request.
                UploadArchiveRequest request = new UploadArchiveRequest()
                    .withVaultName(vaultName)
                    .withArchiveDescription(description)
                    .withChecksum(TreeHashGenerator.calculateTreeHash(new File(fileName))) 
                    .withBody(new ByteArrayInputStream(body))
                    .withContentLength((long)body.length);
                
                Future<UploadArchiveResult> result = AppCache.getAsyncClient().uploadArchiveAsync(request);
                while(!result.isDone()) {
                   System.out.println("Uploading...");
                   Thread.sleep(2000);
                }
                if (result.get()!=null) {
                    return true;
                } else {
                    return false;
                }              
        } else {
            throw new Exception("File not found!");
        }        
    }
    
    
    
    public String getVaultName() {
        return this.vaultName;
    }
            
}
