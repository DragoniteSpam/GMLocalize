package assets.gm1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import java.io.*;
import org.w3c.dom.*;
import javax.xml.parsers.*;

public class GM1Room extends GM1File {
    private static final String FOLDER=".\\rooms";
    private static final String EXTENSION=".room.gmx";
    
	public GM1Room(String absolutePath){
		super(absolutePath, true);
	}
    
    public String creationCode(){
        StringBuilder codeBuilder=new StringBuilder();
    
        NodeList code=document.getElementsByTagName("code");
        // There's no reason for this to be longer than one, but just to be safe
        for (int i=0; i<code.getLength(); i++){
            codeBuilder.append(code.item(i).getTextContent()+" ");
        }
        
        ArrayList<String> codeStrings= xmlGetDefaultAttributes("instances", "instance", "code");
        
        for (String s : codeStrings){
            codeBuilder.append(s);
        }
        
        return codeBuilder.toString();
    }
    
    public static ArrayList<GM1Room> allFiles(String directory){
		File folder=new File(directory+FOLDER);
		ArrayList<GM1Room> list=new ArrayList<GM1Room>();
		if (folder.exists()){
			for (File entry : folder.listFiles()){
				if (!entry.isDirectory()&&entry.getName().endsWith(EXTENSION)){
					list.add(new GM1Room(entry.getAbsolutePath()));
				}
			}
		}
		return list;
	}
    
    public ArrayList<String> allInstances(){
        HashMap<String, String> objectNames=new HashMap<String, String>();
        
        ArrayList<String> nameStrings= xmlGetDefaultAttributes("instances", "instance", "objName");
        
        for (String s : nameStrings){
            if (!objectNames.containsKey(s)){
                objectNames.put(s, s);
            }
        }
        
        nameStrings= xmlGetDefaultAttributes("views", "view", "objName");
        
        for (String s : nameStrings){
            if (!objectNames.containsKey(s)){
                objectNames.put(s, s);
            }
        }
        
        Set<String> keys=objectNames.keySet();
        
        ArrayList<String> instanceNames=new ArrayList<String>();
        for (String s : keys){
            // Sometimes if you delete an object that's in use in Game Maker
            // it'll replace that object with "<undefined>" and we don't want that.
            if (!s.equals("<undefined>")){
                instanceNames.add(s);
            }
        }
        
        return instanceNames;
    }
    
    public ArrayList<String> allBackgrounds(){
        HashMap<String, String> bgNames=new HashMap<String, String>();
        
        ArrayList<String> nameStrings= xmlGetDefaultAttributes("backgrounds", "background", "name");
        
        for (String s : nameStrings){
            if (!bgNames.containsKey(s)){
                bgNames.put(s, s);
            }
        }
        
        nameStrings= xmlGetDefaultAttributes("tiles", "tile", "bgName");
        
        for (String s : nameStrings){
            if (!bgNames.containsKey(s)){
                bgNames.put(s, s);
            }
        }
        
        Set<String> keys=bgNames.keySet();
        
        ArrayList<String> names=new ArrayList<String>();
        for (String s : keys){
            if (!s.equals("<undefined>")){
                names.add(s);
            }
        }
        
        return names;
    }
}