package assets.gm1;

import java.util.ArrayList;

import java.io.*;
import org.w3c.dom.*;
import javax.xml.parsers.*;

/**
 * A file representing Game Maker Studio 1 Path assets.
 *
 * @author DragoniteSpam
 */
public class GM1Path extends GM1File {
    private static final String FOLDER=".\\paths";
    private static final String EXTENSION=".path.gmx";
    protected static String typeName="Path";
    
    /**
     * Constructor for a GM1Path file. Essentially a wrapper for the GM1File constructor.
     * These types of files are XML, so we pass that information on as well.
     *
     * @param absolutePath the path to the file. Contrary to the variable name, it doesn't
     *      have to be an absolute path, as long as Java can find it.
     */
	public GM1Path(String absolutePath){
		super(absolutePath, true);
	}
    
    /**
     * Returns the name of the asset type; in this case, "Path."
     *
     * @return the name of the asset type
     */
    public String getTypeName(){
        return typeName;
    }
    
    /**
     * Searches a folder (the project folder + the asset folder) for files with the asset extension.
     * Instantiates a new GM1Path for each one that it finds.
     *
     * @return an ArrayList of GM1Path representing all of the paths found in the project
     *      directory
     */
    public static ArrayList<GM1Path> allFiles(String directory){
		File folder=new File(directory+FOLDER);
		ArrayList<GM1Path> list=new ArrayList<GM1Path>();
		if (folder.exists()){
			for (File entry : folder.listFiles()){
				if (!entry.isDirectory()&&entry.getName().endsWith(EXTENSION)){
					list.add(new GM1Path(entry.getAbsolutePath()));
				}
			}
		}
		return list;
	}
}