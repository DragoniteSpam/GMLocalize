package assets.gm1;

import java.util.ArrayList;

import java.io.*;
import org.w3c.dom.*;
import javax.xml.parsers.*;

/**
 * A file representing Game Maker Studio 1 Shader assets.
 *
 * @author DragoniteSpam
 */
public class GM1Shader extends GM1File {
    private static final String FOLDER=".\\shaders";
    private static final String EXTENSION=".shader";
    protected static String typeName="Shader";
    
    /**
     * Constructor for a GM1Shader file. Essentially a wrapper for the GM1File constructor.
     * These types of files are NOT XML, so we pass that information on as well.
     *
     * @param absolutePath the path to the file. Contrary to the variable name, it doesn't
     *      have to be an absolute path, as long as Java can find it.
     */
	public GM1Shader(String absolutePath){
		super(absolutePath, false);
	}
    
    /**
     * Returns the name of the asset type; in this case, "Shader."
     *
     * @return the name of the asset type
     */
    public String getTypeName(){
        return typeName;
    }
    
    /**
     * Searches a folder (the project folder + the asset folder) for files with the asset extension.
     * Instantiates a new GM1Shader for each one that it finds.
     *
     * @return an ArrayList of GM1Shader representing all of the shaders found in the project
     *      directory
     */
    public static ArrayList<GM1Shader> allFiles(String directory){
		File folder=new File(directory+FOLDER);
		ArrayList<GM1Shader> list=new ArrayList<GM1Shader>();
		if (folder.exists()){
			for (File entry : folder.listFiles()){
				if (!entry.isDirectory()&&entry.getName().endsWith(EXTENSION)){
					list.add(new GM1Shader(entry.getAbsolutePath()));
				}
			}
		}
		return list;
	}
}