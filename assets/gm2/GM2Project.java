package assets.gm2;

import java.util.ArrayList;

import java.io.*;

/**
 * A file representing the Game Maker project as a whole.
 *
 * @author DragoniteSpam
 */
public class GM2Project extends GM2File {
    private static final String FOLDER="\\";
    private static final String EXTENSION=".yyp";
    protected static String typeName="Project";
    
    /**
     * Constructor for a GM2Project file. Essentially a wrapper for the GM2File constructor.
     * These types of files are JSON, usually, so we pass that information on as well.
     *
     * @param absolutePath the path to the file. Contrary to the variable name, it doesn't
     *      have to be an absolute path, as long as Java can find it.
     */
	public GM2Project(String absolutePath){
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
     * Searches the project folder for a file named "<project name>.yyp."
     * There can only be one of these in a folder. If such a file exists, it'll return null instead.
     *
     * @return a GM2Project object representing the overall project, or null if none is found.
     */
    public static GM2Project autoDetect(String directory){
        GM2Project root=new GM2Project(directory+"\\"+new File(directory).getName()+EXTENSION);
        
        return (root.exists())?root:null;
	}
}