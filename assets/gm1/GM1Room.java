package assets.gm1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import java.io.*;
import org.w3c.dom.*;
import javax.xml.parsers.*;

/**
 * A file representing Game Maker Studio 1 Room assets.
 *
 * @author DragoniteSpam
 */
public class GM1Room extends GM1File {
    private static final String FOLDER=".\\rooms";
    private static final String EXTENSION=".room.gmx";
    protected static String typeName="Room";
    
    /**
     * Constructor for a GM1Room file. Essentially a wrapper for the GM1File constructor.
     * These types of files are XML, so we pass that information on as well.
     *
     * @param absolutePath the path to the file. Contrary to the variable name, it doesn't
     *      have to be an absolute path, as long as Java can find it.
     */
	public GM1Room(String absolutePath){
		super(absolutePath, true);
	}
    
    /**
     * Returns the name of the asset type; in this case, "Room."
     *
     * @return the name of the asset type
     */
    public String getTypeName(){
        return typeName;
    }
    
    /**
     * Extracts the creation code of both the room itself and the instances inside the room, and returns it
     * as a string. One long string.
     *
     * @return a String containing the creation code of everything in the room that uses creation code
     */
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
    
    /**
     * Searches a folder (the project folder + the asset folder) for files with the asset extension.
     * Instantiates a new GM1Room for each one that it finds.
     *
     * @return an ArrayList of GM1Room representing all of the rooms found in the project
     *      directory
     */
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
    
    /**
     * Finds all objects used by the room. This includes both instances placed down in the editor and
     * instances that the views are set to follow, although to be honest, if an object is only in use in
     * your project as a view target, it's probably not of much use to you anyway.
     *
     * @return an ArrayList of Strings containing the names of all of the objects in use in the room
     */
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
    
    /**
     * Finds all backgrounds used by the room. This includes both backgrounds used on their own (you know,
     * in the "backgrounds" tab) and used as tile sets.
     *
     * @return an ArrayList of Strings containing the names of all of the backgrounds in use in the room
     */
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