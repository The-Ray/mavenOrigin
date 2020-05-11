/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sray.mylib.filemanagement;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author Sushovan Ray
 */

public class PropertiesReader {
    
    String fileName;
    File file;
    String filePath;
    Properties properties;
    InputStream inputStream;
    
    
    
    public void getPropertiesFile(){
        
        try{
        this.file = new File(this.filePath);
        this.properties  = new Properties();
        this.inputStream = new FileInputStream(this.file);
        this.properties.load(this.inputStream);
        
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    
    public Properties getProperties(){
        return this.properties;
    }
    
    public void setFilePath(String filePath){
        this.filePath = filePath;
    }
    
}
