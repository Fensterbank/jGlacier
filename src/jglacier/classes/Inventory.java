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

package jglacier.classes;

import com.amazonaws.util.DateUtils;
import com.amazonaws.util.json.JSONArray;
import com.amazonaws.util.json.JSONObject;
import java.util.ArrayList;
import java.util.Date;
import jglacier.AppCache;

public class Inventory {
    String vaultARN;
    Date invDate;
    ArrayList<Archive> archiveList;
    
    public Inventory(String vaultARN, String date, ArrayList<Archive> archives) {
        this.vaultARN = vaultARN;
        this.archiveList = archives;
    }
    
    public Inventory(String jsonText) {
        JSONObject jsonObj;        
        this.archiveList = new ArrayList<Archive>();
        
        try {
            DateUtils du = new DateUtils();
            jsonObj = AppCache.parseJSON(jsonText);
            this.vaultARN = jsonObj.getString("VaultARN");
            this.invDate = du.parseIso8601Date(jsonObj.getString("InventoryDate"));
            JSONArray jsonArr = jsonObj.getJSONArray("ArchiveList");
            
            for (int i = 0; i<jsonArr.length(); i++) {
                JSONObject archiveItem = jsonArr.getJSONObject(i);
                Archive archive = new Archive(archiveItem.getString("ArchiveId"), archiveItem.getString("ArchiveDescription"), du.parseIso8601Date(archiveItem.getString("CreationDate")), archiveItem.getLong("Size"), archiveItem.getString("SHA256TreeHash"));
                this.archiveList.add(archive);
            }                        
        } catch (Exception ex) {
            System.err.println("Error on parsing JSON...");
        }
    }
    
    @Override
    public String toString() {
        String result;
        result = "Content of vault " + this.vaultARN + ":\n";
        result += "Inventory created:\t" + this.invDate.toString() + "\n";  
        result += "Archives:\t\t" + this.archiveList.size() + "\n";
        result += "_____________________________________________________________________\n";
        for (Archive a : this.archiveList) {
            result += a.toString();
        }
        return result;
    }       
}
