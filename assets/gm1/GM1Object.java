package assets.gm1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import java.io.*;
import org.w3c.dom.*;
import javax.xml.parsers.*;

public class GM1Object extends GM1File {
    private static final String FOLDER=".\\objects";
    private static final String EXTENSION=".object.gmx";
    
	public GM1Object(String absolutePath){
		super(absolutePath);
	}
    
    public static ArrayList<GM1Object> allFiles(String directory){
		File folder=new File(directory+FOLDER);
		ArrayList<GM1Object> list=new ArrayList<GM1Object>();
		if (folder.exists()){
			for (File entry : folder.listFiles()){
				if (!entry.isDirectory()&&entry.getName().endsWith(EXTENSION)){
					list.add(new GM1Object(entry.getAbsolutePath()));
				}
			}
		}
		return list;
	}
    
    public ArrayList<String> allObjects(){
        HashMap<String, String> objectNames=new HashMap<String, String>();
        
        ArrayList<String> nameStrings= xmlGetPrimaryValues("parentName");
        
        for (String s : nameStrings){
            if (!objectNames.containsKey(s)){
                objectNames.put(s, s);
            }
        }
        
        Set<String> keys=objectNames.keySet();
        
        ArrayList<String> instanceNames=new ArrayList<String>();
        for (String s : keys){
            if (!s.equals("<undefined>")){
                instanceNames.add(s);
            }
        }
        
        return instanceNames;
    }
    
    public ArrayList<String> allSprites(){
        HashMap<String, String> spriteNames=new HashMap<String, String>();
        
        ArrayList<String> spriteNameStrings=xmlGetPrimaryValues("spriteName");
        
        for (String s : spriteNameStrings){
            if (!spriteNames.containsKey(s)){
                spriteNames.put(s, s);
            }
        }
        
        ArrayList<String> maskNameStrings=xmlGetPrimaryValues("maskName");
        
        for (String s : maskNameStrings){
            if (!spriteNames.containsKey(s)){
                spriteNames.put(s, s);
            }
        }
        
        Set<String> keys=spriteNames.keySet();
        
        ArrayList<String> names=new ArrayList<String>();
        for (String s : keys){
            if (!s.equals("<undefined>")){
                names.add(s);
            }
        }
        
        return names;
    }
    
    public String code(){
        StringBuilder codeBuilder=new StringBuilder();
        
        // I really don't like XML.
        NodeList eventContainer=document.getElementsByTagName("events");
        for (int i=0; i<eventContainer.getLength(); i++){               // all events in here
            NodeList eventList=((Element)eventContainer.item(i)).getElementsByTagName("event");
            for (int j=0; j<eventList.getLength(); j++){                // for each specific event
                NodeList actions=((Element)eventList.item(j)).getElementsByTagName("action");
                for (int k=0; k<actions.getLength(); k++){              // for each action in the event
                    NodeList argumentContainer=((Element)actions.item(k)).getElementsByTagName("arguments");
                    for (int l=0; l<argumentContainer.getLength(); l++){// all action arguments here
                        NodeList argumentList=((Element)argumentContainer.item(l)).getElementsByTagName("argument");
                        for (int m=0; m<argumentList.getLength(); m++){ // something probably
                            NodeList strings=((Element)argumentList.item(m)).getElementsByTagName("string");
                            for (int n=0; n<strings.getLength(); n++){  // invidivual code strings
                                codeBuilder.append(((Element)strings.item(n)).getTextContent());
                            }
                        }
                    }
                }
            }
        }
        
        return codeBuilder.toString();
    }
}