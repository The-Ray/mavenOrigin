# mavenOrigin
Create your own maven repo with S3
Put your properties file  that contains jar path or create a properties file that like,  
      file.reference.antlr-2.7.7.jar=C:\\Users\\Sushovan Ray\\Documents\\Software\\JAR_FILES\\Jar2\\Drools Lib\\antlr-2.7.7.jar
      file.reference.antlr-3.3.jar=C:\\Users\\Sushovan Ray\\Documents\\Software\\JAR_FILES\\Jar2\\Drools Lib\\antlr-3.3.jar
      file.reference.antlr-runtime-3.3.jar=C:\\Users\\Sushovan Ray\\Documents\\Software\\JAR_FILES\\Jar2\\Drools Lib\\antlr-runtime-3.3.jar
      file.reference.ASPL.jar=C:\\Users\\Sushovan Ray\\Documents\\Software\\JAR_FILES\\Jar2\\WAR\\ASPL.jar
      
Give this file path into main class now, in future this should fetch from a config properties file.
