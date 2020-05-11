/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sray.mylib.filemanagement;

import com.sray.mylib.helper.S3Constants;
import com.sray.mylib.pom.GeneratePOM;
import com.sray.mylib.pom.POMFactory;
import com.sray.mylib.s3repository.S3ManageConnection;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;

/**
 *
 * @author Sushovan Ray
 */
public class FileManagement {

    private Properties properties;
    private String key;
    private String value;
    private HashSet jarFiles;
    private File file;
    private String fileName;
    private String artifactId;
    private String version;
    private GeneratePOM generatePOM;
    private POMFactory pomFactory;
    private String outPutFilePath;
    private S3ManageConnection S3Conn;

    public void manageFilePath() {

        try {
            jarFiles = new HashSet();
            Iterator propIte = properties.keySet().iterator();
            while (propIte.hasNext()) {
                key = (String) propIte.next();
                value = properties.getProperty(key, "");
                if (key.contains("file.reference.")) {
                    jarFiles.add(value);
                }

            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    private String s3response;
    private String jarFileName;
    private String dependencyPOMFileName;
    private String selfPOMFileName;
    private int count;

    public void createFolderStructure() {
        this.fileName = "";
        this.S3Conn = new S3ManageConnection();
        count = 0;
        try {
            jarFiles.forEach(filePath -> {
                this.file = new File((String) filePath);

                if (this.file.exists()) {
                    this.fileName = this.file.getName();
                    this.fileName = getOnlyFileName();
                    if (this.fileName.split("-").length > 1) {
                        this.artifactId = this.fileName.substring(0, this.fileName.lastIndexOf("-"));
                        this.version = this.fileName.substring(this.fileName.lastIndexOf("-") + 1, this.fileName.length());
                    } else {
                        this.artifactId = this.fileName;
                        this.version = "1.0";
                        this.fileName = this.artifactId + "-" + this.version;
                    }
                    this.pomFactory = new POMFactory();
                    this.pomFactory.setArtifactId(this.artifactId);
                    this.pomFactory.setVersion(this.version);
                    this.generatePOM = new GeneratePOM(this.pomFactory, this.outPutFilePath);
                    this.generatePOM.generateSelfPOMFile();
                    this.generatePOM.generateDependencyPOMFile();
//                  this.jarFileName =S3Constants.ROOT_FOLDER+"/"+S3Constants.GROUP_FOLDER+"/"+this.artifactId+"/"+this.version+"/"+this.artifactId+"-"+this.version+".jar"; 
//                  this.s3response=this.S3Conn.putS3Object(this.file, this.jarFileName);
//                  System.out.println("S3 JAR Upload Response:"+this.s3response);
//                  
//                  this.selfPOMFileName =S3Constants.ROOT_FOLDER+"/"+S3Constants.GROUP_FOLDER+"/"+this.artifactId+"/"+this.version+"/POM.xml"; 
//                  this.s3response=this.S3Conn.putS3Object(this.generatePOM.getSelfPOMFile(), this.selfPOMFileName);
//                  System.out.println("S3 POM Upload Response:"+this.s3response);

                } else {
                    System.out.println("Not Found");
                }
                count++;
            });
            this.mergerDependencyFiles();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String getOnlyFileName() {
        String onlyFileName = "";
        try {
            onlyFileName = this.fileName.substring(0, this.fileName.lastIndexOf("."));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return onlyFileName;
    }

    /**
     * @param outPutFilePath the outPutFilePath to set
     */
    public void setOutPutFilePath(String outPutFilePath) {
        this.outPutFilePath = outPutFilePath;
    }

    public void mergerDependencyFiles() {

        try {
            System.out.println("outPut Path=>"+this.outPutFilePath);
            // create instance of directory 
            File dir = new File(this.outPutFilePath+"/dependency");

            // create obejct of PrintWriter for output file 
            PrintWriter pw = new PrintWriter(this.outPutFilePath+"/dependency/output.xml");

            // Get list of all the files in form of String Array 
            String[] fileNames = dir.list();

            // loop for reading the contents of all the files  
            // in the directory GeeksForGeeks 
            for (String fileName : fileNames) {
                System.out.println("Reading from " + fileName);

                // create instance of file from Name of  
                // the file stored in string Array 
                File f = new File(dir, fileName);

                // create object of BufferedReader 
                BufferedReader br = new BufferedReader(new FileReader(f));
//                pw.println("Contents of file " + fileName);

                // Read from current file 
                String line = br.readLine();
                while (line != null) {

                    // write to the output file 
                    pw.println(line);
                    line = br.readLine();
                }
                pw.flush();
            }
            System.out.println("Reading from all files"
                    + " in directory " + dir.getName() + " Completed");
        } catch (Exception ex) {

        }
    }
}
