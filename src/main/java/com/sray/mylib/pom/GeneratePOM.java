/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sray.mylib.pom;

import com.sray.mylib.helper.S3Constants;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 *
 * @author Sushovan Ray
 */
public class GeneratePOM {

    private POMFactory pomFactory;
    private String outPutPath;
    private File file;
    private FileWriter fr;
    private String pomPath;
    private String xmlString;
    private StringWriter stringWriter;
    private XMLOutputFactory xMLOutputFactory;
    private XMLStreamWriter xMLStreamWriter;

    public GeneratePOM(POMFactory pomFactory, String outPutPath) {
        this.pomFactory = pomFactory;
        this.outPutPath = outPutPath;
    }
    
    private File selfPOMFile;
    public void generateSelfPOMFile() {
        this.pomPath = "";
        try {
            this.stringWriter = new StringWriter();
            this.xMLOutputFactory = XMLOutputFactory.newInstance();
            this.xMLStreamWriter = xMLOutputFactory.createXMLStreamWriter(stringWriter);

            this.xMLStreamWriter.writeStartDocument("UTF-8", "1.0");
            this.xMLStreamWriter.writeStartElement("project");
            this.xMLStreamWriter.writeAttribute("xmlns", "http://maven.apache.org/POM/4.0.0");
            this.xMLStreamWriter.writeAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
            this.xMLStreamWriter.writeAttribute("xsi:schemaLocation", "http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd");

            this.xMLStreamWriter.writeStartElement("modelVersion");
            this.xMLStreamWriter.writeCharacters(POMConstants.POM_MODELVERSION);
            this.xMLStreamWriter.writeEndElement();

            this.xMLStreamWriter.writeStartElement("groupId");
            this.xMLStreamWriter.writeCharacters(S3Constants.GROUP_FOLDER.replaceAll("/", "."));
            this.xMLStreamWriter.writeEndElement();

            this.xMLStreamWriter.writeStartElement("artifactId");
            this.xMLStreamWriter.writeCharacters(this.pomFactory.getArtifactId());
            this.xMLStreamWriter.writeEndElement();

            this.xMLStreamWriter.writeStartElement("version");
            this.xMLStreamWriter.writeCharacters(this.pomFactory.getVersion());
            this.xMLStreamWriter.writeEndElement();

            this.xMLStreamWriter.writeEndElement();
            this.xMLStreamWriter.writeEndDocument();

            this.xMLStreamWriter.flush();
            this.xMLStreamWriter.close();
            this.xmlString = stringWriter.getBuffer().toString();
            
            this.xmlString = getPrettyViewXML(this.xmlString, "2");

            stringWriter.close();
            this.pomPath = this.outPutPath + "/" + S3Constants.GROUP_FOLDER + "/" + this.pomFactory.getArtifactId() + "/" + this.pomFactory.getVersion() + "/pom.xml";
            this.file = new File(this.pomPath);
//            file.deleteOnExit();
            this.file.getParentFile().mkdirs();
            if (!this.file.exists()) {
                this.file.createNewFile();
            } else {
                this.file.delete();
                this.file.createNewFile();
            }
            this.fr = new FileWriter(this.file);
            this.fr.write(this.xmlString);
            this.fr.close();
            this.selfPOMFile = this.file;
        } catch (XMLStreamException e) {
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    private File dependencyPOMFile;
    public void generateDependencyPOMFile() {
        this.xMLStreamWriter =null;
        try {
            this.stringWriter = new StringWriter();
            this.xMLOutputFactory = XMLOutputFactory.newInstance();
            this.xMLStreamWriter = this.xMLOutputFactory.createXMLStreamWriter(this.stringWriter);

            this.xMLStreamWriter.writeStartDocument();
            
//            this.xMLStreamWriter.writeStartElement("dependencies");

            this.xMLStreamWriter.writeStartElement("dependency");

            this.xMLStreamWriter.writeStartElement("groupId");
            this.xMLStreamWriter.writeCharacters(S3Constants.GROUP_FOLDER.replaceAll("/", "."));
            this.xMLStreamWriter.writeEndElement();

            this.xMLStreamWriter.writeStartElement("artifactId");
            this.xMLStreamWriter.writeCharacters(this.pomFactory.getArtifactId());
            this.xMLStreamWriter.writeEndElement();

            this.xMLStreamWriter.writeStartElement("version");
            this.xMLStreamWriter.writeCharacters(this.pomFactory.getVersion());
            this.xMLStreamWriter.writeEndElement();

            this.xMLStreamWriter.writeEndElement();
//            this.xMLStreamWriter.writeEndElement();
            this.xMLStreamWriter.writeEndDocument();

            this.xMLStreamWriter.flush();
            this.xMLStreamWriter.close();

            this.xmlString = stringWriter.getBuffer().toString();
            System.out.println("this.xmlString==>"+this.xmlString);
            this.xmlString = getPrettyViewXML(this.xmlString, "2");
//            this.xmlString = this.xmlString.replaceAll("<?xml version=\"1.0\" ?>", "");
//            this.xmlString = this.xmlString.replaceAll("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "");
            stringWriter.close();
            this.pomPath = this.outPutPath + "/dependency/"+this.pomFactory.getArtifactId()+"-dependency.xml";
            this.file = new File(this.pomPath);
//            file.deleteOnExit();
            this.file.getParentFile().mkdirs();
            if (!this.file.exists()) {
                this.file.createNewFile();
            }
            this.fr = new FileWriter(this.file);
            this.fr.write(this.xmlString);
            this.fr.close();
            this.dependencyPOMFile  = this.file;
        } catch (XMLStreamException e) {
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private Source xmlInput;
    private StringWriter stringWr;
    private StreamResult xmlOutput;
    private TransformerFactory transformerFactory;
    private Transformer transformer;
    
    private String getPrettyViewXML(String input, String indent) {

        try {
            this.xmlInput = new StreamSource(new StringReader(input));
            this.stringWr = new StringWriter();
            this.xmlOutput = new StreamResult(this.stringWr);
            this.transformerFactory = TransformerFactory.newInstance();
            this.transformerFactory.setAttribute("indent-number", indent);
            this.transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(this.xmlInput, this.xmlOutput);
            return xmlOutput.getWriter().toString();
        } catch (Exception e) {
            throw new RuntimeException(e); // simple exception handling, please review it
        }
    }
    
    public File getSelfPOMFile(){
        return this.selfPOMFile;
    }
    
    public File getDependencyPOMFile(){
        return this.dependencyPOMFile;
    }

}
