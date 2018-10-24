package assets.gm2;

import java.util.ArrayList;

import java.io.*;

/**
 * A file representing Game Maker Studio 2 Timeline assets.
 *
 * @author DragoniteSpam
 */
public class GM2Timeline extends GM2File {
    private static final String FOLDER=".\\timelines";
    private static final String EXTENSION=".yy";
    protected static String typeName="Timeline";
    
    /**
     * Constructor for a GM2Timeline file. Essentially a wrapper for the GM2File constructor.
     * These types of files are JSON, so we pass that information on as well.
     *
     * @param absolutePath the path to the file. Contrary to the variable name, it doesn't
     *      have to be an absolute path, as long as Java can find it.
     */
	public GM2Timeline(String absolutePath){
		super(absolutePath, true);
	}
    
    /**
     * Returns the name of the asset type; in this case, "Timeline."
     *
     * @return the name of the asset type
     */
    public String getTypeName(){
        return typeName;
    }
    
    /**
     * Searches a folder (the project folder + the asset folder) for files with the asset extension.
     * Instantiates a new GM2Timeline for each one that it finds.
     *
     * @return an ArrayList of GM2Timeline representing all of the timelines found in the project
     *      directory
     */
    public static ArrayList<GM2Timeline> allFiles(String directory){
		File folder=new File(directory+FOLDER);
		ArrayList<GM2Timeline> list=new ArrayList<GM2Timeline>();
		if (folder.exists()){
			for (File entry : folder.listFiles()){
				if (entry.isDirectory()){
					GM2Timeline file=new GM2Timeline(entry.getAbsolutePath()+"\\"+entry.getName()+EXTENSION);
					if (file.exists()){
						list.add(file);
					}
				}
			}
		}
		return list;
	}
}