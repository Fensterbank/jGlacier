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

import com.amazonaws.services.glacier.model.InitiateJobRequest;
import com.amazonaws.services.glacier.model.InitiateJobResult;
import com.amazonaws.services.glacier.model.JobParameters;
import java.io.BufferedReader;
import java.io.FileReader;
import jglacier.classes.Inventory;

public class VaultManager {
    private String vaultName;
    
    public VaultManager(String vaultName) {
        this.vaultName = vaultName;        
    }
    
    public void InitiateInventoryJob() {
        InitiateJobRequest initJobRequest = new InitiateJobRequest()
                        .withVaultName(this.vaultName)
                        .withJobParameters(
                            new JobParameters()
                                .withType("inventory-retrieval")
        );
        InitiateJobResult initJobResult = AppCache.getClient().initiateJob(initJobRequest);
        System.out.println("New Inventory Job initiated. Please wait a few hours...");        
    }
    
    public void DisplayLastInventory() throws Exception {
        try {
            String jsonText;
            FileReader fr = new FileReader("lastInventory.json");
            BufferedReader br = new BufferedReader(fr);

            jsonText = "";
            String line;

            while ((line = br.readLine()) != null) {
                jsonText += line;
            }

            Inventory inv = new Inventory(jsonText);
            System.out.println(inv.toString());
        } catch (Exception ex) {
            System.out.println("It seems the inventory hasn't already been loaded by jGlacier!");
            System.out.println("Please initiate an inventory job and wait for the output.");            
        }
    }
}
