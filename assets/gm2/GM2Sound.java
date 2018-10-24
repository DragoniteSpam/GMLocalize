package assets.gm2;

import java.util.ArrayList;

import java.io.*;

/**
 * A file representing Game Maker Studio 2 Sound assets.
 *
 * @author DragoniteSpam
 */
public class GM2Sound extends GM2File {
    private static final String FOLDER=".\\sounds";
    private static final String EXTENSION=".yy";
    protected static String typeName="Sound";
    
    /**
     * Constructor for a GM2Sound file. Essentially a wrapper for the GM2File constructor.
     * These types of files are JSON, so we pass that information on as well.
     *
     * @param absolutePath the path to the file. Contrary to the variable name, it doesn't
     *      have to be an absolute path, as long as Java can find it.
     */
	public GM2Sound(String absolutePath){
		super(absolutePath, true);
	}
    
    /**
     * Returns the name of the asset type; in this case, "Sound."
     *
     * @return the name of the asset type
     */
    public String getTypeName(){
        return typeName;
    }
    
    /**
     * Searches a folder (the project folder + the asset folder) for files with the asset extension.
     * Instantiates a new GM2Sound for each one that it finds.
     *
     * @return an ArrayList of GM2Sound representing all of the sounds found in the project
     *      directory
     */
    public static ArrayList<GM2Sound> allFiles(String directory){
		File folder=new File(directory+FOLDER);
		ArrayList<GM2Sound> list=new ArrayList<GM2Sound>();
		if (folder.exists()){
			for (File entry : folder.listFiles()){
				if (entry.isDirectory()){
					GM2Sound file=new GM2Sound(entry.getAbsolutePath()+"\\"+entry.getName()+EXTENSION);
					if (file.exists()){
						list.add(file);
					}
				}
			}
		}
		return list;
	}
}