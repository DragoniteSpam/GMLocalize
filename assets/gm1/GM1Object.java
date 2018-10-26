package assets.gm1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import java.io.*;
import org.w3c.dom.*;
import javax.xml.parsers.*;

/**
 * A file representing Game Maker Studio 1 Object assets.
 *
 * @author DragoniteSpam
 */
public class GM1Object extends GM1File {
    private static final String FOLDER=".\\objects";
    private static final String EXTENSION=".object.gmx";
    protected static String typeName="Object";
    
    /**
     * Constructor for a GM1Object file. Essentially a wrapper for the GM1File constructor.
     * These types of files are XML, so we pass that information on as well.
     *
     * @param absolutePath the path to the file. Contrary to the variable name, it doesn't
     *      have to be an absolute path, as long as Java can find it.
     */
	public GM1Object(String absolutePath){
		super(absolutePath, true);
	}
    
    /**
     * Returns the name of the asset type; in this case, "Object."
     *
     * @return the name of the asset type
     */
    public String getTypeName(){
        return typeName;
    }
    
    /**
     * Searches a folder (the project folder + the asset folder) for files with the asset extension.
     * Instantiates a new GM1Object for each one that it finds.
     *
     * @return an ArrayList of GM1Object representing all of the objects found in the project
     *      directory
     */
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
    
    /**
     * Finds the object that this object is a child of, if any. This is important because if an object's only function
     * is to serve as a base for other objects without being instantiated themselves (think of abstract classes in Java)
     * then it should be marked as "in use."
     *
     * @return a String of the name of all of the parent object, or null if the object doesn't have one
     */
    public String getParent(){
        HashMap<String, String> objectNames=new HashMap<String, String>();
        
        ArrayList<String> nameStrings=xmlGetPrimaryValues("parentName");
        
        return (nameStrings.size()>0&&!nameStrings.get(0).equals("<undefined>"))?nameStrings.get(0):null;
    }
    
    /**
     * Finds all sprites used by the object. Namely, this is the default sprite index (if it has one) and the sprite maskName
     * (if it has one).
     *
     * @return an ArrayList of Strings containing the names of all of the sprites in use by the object
     */
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
    
    /**
     * Extracts the code of the object's events, and returns it as one long string.
     *
     * <b>This is untested with drag-and-drop actions besides the "execute code" one. It should behave
     * the same, if I understand the way that drag-and-drop is stored in the .gmx file, but you may
     * well find some unexpected behavior when using it. I'm taking a gamble that the people who use
     * this tool are not likely to be people who make heavy use of drag-and-drop.</b>
     *
     * @return a String containing the code of all of the object's events
     */
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