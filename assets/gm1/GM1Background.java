package assets.gm1;

import java.util.ArrayList;

import java.io.*;
import org.w3c.dom.*;
import javax.xml.parsers.*;

/**
 * A file representing Game Maker Studio 1 Background assets.
 *
 * @author DragoniteSpam
 */
public class GM1Background extends GM1File {
    private static final String FOLDER=".\\background";
    private static final String EXTENSION=".background.gmx";
    protected static String typeName="Background";
    
    /**
     * Constructor for a GM1Background file. Essentially a wrapper for the GM1File constructor.
     * These types of files are XML, so we pass that information on as well.
     *
     * @param absolutePath the path to the file. Contrary to the variable name, it doesn't
     *      have to be an absolute path, as long as Java can find it.
     */
	public GM1Background(String absolutePath){
		super(absolutePath, true);
	}
    
    /**
     * Returns the name of the asset type; in this case, "Background."
     *
     * @return the name of the asset type
     */
    public String getTypeName(){
        return typeName;
    }
    
    /**
     * Searches a folder (the project folder + the asset folder) for files with the asset extension.
     * Instantiates a new GM1Background for each one that it finds.
     *
     * @return an ArrayList of GM1Background representing all of the backgrounds found in the project
     *      directory
     */
    public static ArrayList<GM1Background> allFiles(String directory){
		File folder=new File(directory+FOLDER);
		ArrayList<GM1Background> list=new ArrayList<GM1Background>();
		if (folder.exists()){
			for (File entry : folder.listFiles()){
				if (!entry.isDirectory()&&entry.getName().endsWith(EXTENSION)){
					list.add(new GM1Background(entry.getAbsolutePath()));
				}
			}
		}
		return list;
	}
}