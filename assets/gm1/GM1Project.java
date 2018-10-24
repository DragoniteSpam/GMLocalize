package assets.gm1;

import java.util.ArrayList;

import java.io.*;
import org.w3c.dom.*;
import javax.xml.parsers.*;

/**
 * A file representing the Game Maker project as a whole.
 *
 * @author DragoniteSpam
 */
public class GM1Project extends GM1File {
    private static final String FOLDER=".\\";
    private static final String EXTENSION=".project.gmx";
    protected static String typeName="Project";
    
    /**
     * Constructor for a GM1Project file. Essentially a wrapper for the GM1File constructor.
     * These types of files are XML, so we pass that information on as well.
     *
     * @param absolutePath the path to the file. Contrary to the variable name, it doesn't
     *      have to be an absolute path, as long as Java can find it.
     */
	public GM1Project(String absolutePath){
		super(absolutePath, true);
	}
    
    /**
     * Returns the name of the asset type; in this case, "Project."
     *
     * @return the name of the asset type
     */
    public String getTypeName(){
        return typeName;
    }
    
    /**
     * Searches the project folder for a file named "<project name>.project.gmx."
     * There can only be one of these in a folder. If no such file exists, it'll return null instead.
     *
     * @return a GM1Project object representing the overall project, or null if none is found.
     */
    public static GM1Project autoDetect(String directory){
        GM1Project root=new GM1Project(directory+"\\"+new File(directory).getName().replace(GMS1_EXTENSION, "")+EXTENSION);
        
        return (root.exists())?root:null;
	}
    
    /**
     * Searches the XML document for the project's starting room. (In Game Maker, this will be the first room
     * in the resource tree.)
     *
     * @return a String containing the name of the starting room, or null if no rooms are found.
     */
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
    
    /**
     * Searches the XML document for the project's macro names.
     *
     * @return an ArrayList of Strings representing the names of the macros in the project
     */
    public ArrayList<String> allMacros(){
        return xmlGetDefaultAttributes("constants", "constant", "name");
    }
    
    /**
     * Searches the XML document for the project's macros' values. You shouldn't put anything that can be renamed
     * (i.e. variables or asset names) in a macro, but they're still code, so we still check them.
     *
     * @return an ArrayList of Strings representing the code stored in each of the macros.
     */
    public ArrayList<String> allMacroCode(){
        return xmlGetDefaultValues("constants", "constant");
    }
}