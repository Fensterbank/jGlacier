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


import java.util.Date;
import jglacier.AppCache;

public class Archive {
    private String id;
    private String description;
    private Date creationDate;
    private long size;
    private String hash;
    
    public Archive(String id, String descr, Date creationDate, long size, String hash) {
        this.id = id;
        this.description = descr;
        this.size = size;
        this.hash = hash;
        this.creationDate = creationDate;
    }
    
    public String getID() {
        return this.id;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public Date getCreationDate() {
        return this.creationDate;
    }
    
    public long getSize() {
        return this.size;
    }
    
    public String getHumanReadableSize() {
        return AppCache.getFileSizeString(this.size);
    }
    
    public String getHash() {
        return this.hash;
    }
    
    @Override
    public String toString() {
        return this.description + "\t|\t" + this.creationDate.toString() + "\t|\t" + this.getHumanReadableSize();
    }
    
    
}
