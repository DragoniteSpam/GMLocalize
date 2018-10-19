package assets.gm1;

import java.util.ArrayList;

import java.io.*;
import org.w3c.dom.*;
import javax.xml.parsers.*;

/**
 * A file representing Game Maker Studio 1 Script assets.
 *
 * @author DragoniteSpam
 */
public class GM1Script extends GM1File {
    private static final String FOLDER=".\\scripts";
    private static final String EXTENSION=".gml";
    protected static String typeName="Script";
    
    /**
     * Constructor for a GM1Script file. Essentially a wrapper for the GM1File constructor.
     * These types of files are NOT XML, so we pass that information on as well.
     *
     * @param absolutePath the path to the file. Contrary to the variable name, it doesn't
     *      have to be an absolute path, as long as Java can find it.
     */
	public GM1Script(String absolutePath){
		super(absolutePath, false);
	}
    
    /**
     * Returns the name of the asset type; in this case, "Script."
     *
     * @return the name of the asset type
     */
    public String getTypeName(){
        return typeName;
    }
    
    /**
     * Searches a folder (the project folder + the asset folder) for files with the asset extension.
     * Instantiates a new GM1Script for each one that it finds.
     *
     * @return an ArrayList of GM1Script representing all of the scripts found in the project
     *      directory
     */
    public static ArrayList<GM1Script> allFiles(String directory){
		File folder=new File(directory+FOLDER);
		ArrayList<GM1Script> list=new ArrayList<GM1Script>();
		if (folder.exists()){
			for (File entry : folder.listFiles()){
				if (!entry.isDirectory()&&entry.getName().endsWith(EXTENSION)){
					list.add(new GM1Script(entry.getAbsolutePath()));
				}
			}
		}
		return list;
	}
}