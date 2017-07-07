package com.dmens.pokeno.utils;

import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.ImageIcon;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FileUtils {
	
	private static final Logger LOG = LogManager.getLogger(FileUtils.class);
	private static boolean fileSystemBuilt = false;
	
	private static final String DATA_FOLDER = "data/";
	
	 public static List<String> getFilesFromFolder(String folder, String extension){
		 LOG.debug("Getting file list from folder: " + getResourcesLocation());
		 List <String> fileList = new ArrayList<String>();
        try {
        	
			Files.newDirectoryStream(Paths.get(URI.create(getResourcesLocation()+folder)), path -> path.toString().endsWith(extension))
			.forEach(file->{
				fileList.add(file.getFileName().toString());
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
        return fileList;
	}
	 
	 public static InputStream getFileAsInputStream(String fileRelativeLocation){
		 return FileUtils.class.getClassLoader().getResourceAsStream(DATA_FOLDER + fileRelativeLocation);
	 }
	 
	 public static String getResourcesLocation(){
		 String location = FileUtils.class.getClassLoader().getResource(DATA_FOLDER).toString();
		 if(location.contains(".jar!")){
			 createJarFileSystem(location);
		 }
		 return location;
	 }
	 
	 public static List<String> getFileContentsAsList(String fileRelativeLocation){
		 try {
			return Files.readAllLines(Paths.get(URI.create(getResourcesLocation()+fileRelativeLocation)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	 }
	 
	 public static ImageIcon getFileAsImageIcon(String fileRelativeLocation, int width, int height){
		ImageIcon imageIcon = new ImageIcon(FileUtils.class.getClassLoader().getResource(DATA_FOLDER+fileRelativeLocation));
		Image image = imageIcon.getImage(); // transform it 
        Image newimg = image.getScaledInstance(width, height,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
        return new ImageIcon(newimg);
	 }	
	 
	 private static void createJarFileSystem(String location){
		 if(fileSystemBuilt)
			 return;
		 LOG.debug("Building JAR file system");
		 try {
			FileSystems.newFileSystem(URI.create(location), Collections.<String, Object>emptyMap());
			fileSystemBuilt = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
	 }
	 
	 public static Path createFileAndWriteContents(String filename, String content){
		 try {
			return Files.write(Paths.get(URI.create(getResourcesLocation()+filename)), Arrays.asList(content.split("\n")), Charset.forName("UTF-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	 }

}
