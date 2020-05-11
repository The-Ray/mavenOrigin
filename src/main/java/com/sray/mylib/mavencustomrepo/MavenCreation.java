/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sray.mylib.mavencustomrepo;

import com.sray.mylib.filemanagement.FileManagement;
import com.sray.mylib.filemanagement.PropertiesReader;
import com.sray.mylib.helper.S3Constants;
import com.sray.mylib.pom.GeneratePOM;
import java.io.File;
import java.util.Iterator;
import java.util.Properties;

/**
 *
 * @author Sushovan Ray
 */
public class MavenCreation {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        String filePath = "C:\\Users\\Sushovan Ray\\Documents\\OfficeWork\\MVN\\AMSSERVICE\\Worker\\jarlist.txt";
        File file = new File(filePath);
        PropertiesReader preader = new PropertiesReader();
        preader.setFilePath(filePath);
        preader.getPropertiesFile();
        Properties properties = preader.getProperties();
        
        FileManagement fM = new FileManagement();
        fM.setProperties(properties);
        fM.manageFilePath();
        fM.setOutPutFilePath(file.getParent());
        fM.createFolderStructure();
        

//        GeneratePOM gpom = new GeneratePOM();
//        gpom.generatePOMFile();

        System.out.println("System=>"+System.getProperty("home.user"));
        
        
    }
    
}
