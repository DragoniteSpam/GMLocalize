package assets.gm1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import java.io.*;
import org.w3c.dom.*;
import javax.xml.parsers.*;

/**
 * A file representing Game Maker Studio 1 Timeline assets.
 *
 * @author DragoniteSpam
 */
public class GM1Timeline extends GM1File {
    private static final String FOLDER=".\\timelines";
    private static final String EXTENSION=".timeline.gmx";
    protected static String typeName="Timeline";
    
    /**
     * Constructor for a GM1Timeline file. Essentially a wrapper for the GM1File constructor.
     * These types of files are XML, so we pass that information on as well.
     *
     * @param absolutePath the path to the file. Contrary to the variable name, it doesn't
     *      have to be an absolute path, as long as Java can find it.
     */
	public GM1Timeline(String absolutePath){
		super(absolutePath, true);
	}
    
    /**
     * Returns the name of the asset type; in this case, "Timeline."
     *
     * @return the name of the asset type
     */
    public String getTypeName(){
        return typeName;
    }
    
    /**
     * Searches a folder (the project folder + the asset folder) for files with the asset extension.
     * Instantiates a new GM1Timeline for each one that it finds.
     *
     * @return an ArrayList of GM1Timeline representing all of the timelines found in the project
     *      directory
     */
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
    
    /**
     * Extracts the code of the timeline's moments and returns it as one long string.
     *
     * <b>This is untested with drag-and-drop actions besides the "execute code" one. See
     * {@link assets.gm1.GM1Object#code() GM1Object#code()} for greater detail.</b>
     *
     * @return a String containing the code of all of the timeline's moments
     */
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