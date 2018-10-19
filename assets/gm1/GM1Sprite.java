package assets.gm1;

import java.util.ArrayList;

import java.io.*;
import org.w3c.dom.*;
import javax.xml.parsers.*;

/**
 * A file representing Game Maker Studio 1 Sprite assets.
 *
 * @author DragoniteSpam
 */
public class GM1Sprite extends GM1File {
    private static final String FOLDER=".\\sprites";
    private static final String EXTENSION=".sprite.gmx";
    protected static String typeName="Sprite";
    
    /**
     * Constructor for a GM1Sprite file. Essentially a wrapper for the GM1File constructor.
     * These types of files are XML, so we pass that information on as well.
     *
     * @param absolutePath the path to the file. Contrary to the variable name, it doesn't
     *      have to be an absolute path, as long as Java can find it.
     */
	public GM1Sprite(String absolutePath){
		super(absolutePath, true);
	}
    
    /**
     * Returns the name of the asset type; in this case, "Sprite."
     *
     * @return the name of the asset type
     */
    public String getTypeName(){
        return typeName;
    }
    
    /**
     * Searches a folder (the project folder + the asset folder) for files with the asset extension.
     * Instantiates a new GM1Sprite for each one that it finds.
     *
     * @return an ArrayList of GM1Sprite representing all of the sprites found in the project
     *      directory
     */
    public static ArrayList<GM1Sprite> allFiles(String directory){
		File folder=new File(directory+FOLDER);
		ArrayList<GM1Sprite> list=new ArrayList<GM1Sprite>();
		if (folder.exists()){
			for (File entry : folder.listFiles()){
				if (!entry.isDirectory()&&entry.getName().endsWith(EXTENSION)){
					list.add(new GM1Sprite(entry.getAbsolutePath()));
				}
			}
		}
		return list;
	}
}