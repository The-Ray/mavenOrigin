/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sray.mylib.s3repository;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.Owner;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.sray.mylib.helper.S3Constants;
import java.io.File;

/**
 *
 * @author Sushovan Ray
 */
public class S3ManageConnection {
    
    private AWSCredentials credentials;
    private AWSCredentialsProvider credentialProvider;
    private AmazonS3 amazons3;
    private String responseMessage;
    
    public AWSCredentials mergeAWSCredential(){
        System.out.println(S3Constants.AWS_SECRET_KEY+" == "+S3Constants.AWS_SECRET_ACCESS_KEY);
        this.credentials = new BasicAWSCredentials(S3Constants.AWS_SECRET_KEY, S3Constants.AWS_SECRET_ACCESS_KEY);
        return this.credentials;
    }
    
    public AWSCredentialsProvider getAWSCredentialProvider(){
        this.mergeAWSCredential();
        this.credentialProvider = new AWSStaticCredentialsProvider(this.credentials);
        return this.credentialProvider;
    }
    
    public AmazonS3 getS3Client(){
        
        this.getAWSCredentialProvider();
        this.amazons3 = AmazonS3ClientBuilder.standard()
                .withCredentials(this.credentialProvider)
                .withRegion(Regions.AP_SOUTHEAST_1)
                .build();
        return this.amazons3;
    }
    
    public boolean checkBucket(){
        this.getS3Client();
        if(this.amazons3.doesBucketExistV2(S3Constants.BUCKET)){
            return true;
        }else{
            this.responseMessage = "BUCKET Does not Exist";
            return false;
        }
    }
    
    public boolean checkRootFolder(){
        this.getS3Client();
        if(this.amazons3.doesObjectExist(S3Constants.BUCKET, S3Constants.ROOT_FOLDER)){
            return true;
        }else{
            this.responseMessage += "\n Root Folder does not exist";
            return false;
        }
    }
    
    public boolean checkGroupFolder(){
        this.getS3Client();
        if(this.amazons3.doesObjectExist(S3Constants.BUCKET, S3Constants.GROUP_FOLDER)){
            return true;
        }else{
            this.responseMessage += "\n Group Folder does not exist";
            return false;
        }
    }
    
    private String keyName;
    private PutObjectResult putObjectResult;
    public String putS3Object(File file, String fileName){
        try{
        this.getS3Client();
            this.keyName = fileName;
            this.putObjectResult = this.amazons3.putObject(S3Constants.BUCKET, this.keyName, file);
            this.responseMessage="success";
        }catch(Exception ex){
            this.responseMessage = ex.toString();
            ex.printStackTrace();
        }
        return this.responseMessage;
        
    }
    
    
    
}
