package assets.gm1;

import java.util.ArrayList;

import java.io.*;
import org.w3c.dom.*;
import javax.xml.parsers.*;

public class GM1Project extends GM1File {
    private static final String FOLDER=".\\";
    private static final String EXTENSION=".project.gmx";
    
	public GM1Project(String absolutePath){
		super(absolutePath, true);
	}
    
    public static GM1Project autoDetect(String directory){
		File folder=new File(directory);
		
		if (folder.exists()){
			for (File entry : folder.listFiles()){
				if (!entry.isDirectory()){
                    if (entry.getName().equals(entry.getParentFile().getName().replace(GMS1_EXTENSION, "")+EXTENSION)){
                        return new GM1Project(entry.getAbsolutePath());
                    }
                }
			}
		}
        
		return null;
	}
    
    public String startingRoom(){
        NodeList roomNodeList=document.getElementsByTagName("rooms");
        if (roomNodeList.getLength()==0){
            return null;
        }
        
        NodeList actualRoomNodeList=((Element)roomNodeList.item(0)).getElementsByTagName("room");
        if (actualRoomNodeList.getLength()==0){
            return null;
        }
        
        return actualRoomNodeList.item(0).getTextContent().replace("rooms\\", "");
    }
    
    public ArrayList<String> allMacros(){
        return xmlGetDefaultAttributes("constants", "constant", "name");
    }
    
    public ArrayList<String> allMacroCode(){
        return xmlGetDefaultValues("constants", "constant");
    }
}