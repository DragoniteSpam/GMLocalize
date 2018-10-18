package assets.gm1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import java.io.*;
import org.w3c.dom.*;
import javax.xml.parsers.*;

public class GM1Timeline extends GM1File {
    private static final String FOLDER=".\\timelines";
    private static final String EXTENSION=".timeline.gmx";
    
	public GM1Timeline(String absolutePath){
		super(absolutePath);
	}
    
    public static ArrayList<GM1Timeline> allFiles(String directory){
		File folder=new File(directory+FOLDER);
		ArrayList<GM1Timeline> list=new ArrayList<GM1Timeline>();
		if (folder.exists()){
			for (File entry : folder.listFiles()){
				if (!entry.isDirectory()&&entry.getName().endsWith(EXTENSION)){
					list.add(new GM1Timeline(entry.getAbsolutePath()));
				}
			}
		}
		return list;
	}
    
    public String code(){
        StringBuilder codeBuilder=new StringBuilder();
        
        // I still really don't like XML.
        NodeList eventContainer=document.getElementsByTagName("entry");
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