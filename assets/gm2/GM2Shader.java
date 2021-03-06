package assets.gm2;

import java.util.ArrayList;

import java.io.*;

/**
 * A file representing Game Maker Studio 2 Shader assets.
 *
 * @author DragoniteSpam
 */
public class GM2Shader extends GM2File {
    private static final String FOLDER="\\shaders";
    private static final String EXTENSION=".yy";
    protected static String typeName="Shader";
    
    /**
     * Constructor for a GM2Shader file. Essentially a wrapper for the GM2File constructor.
     * These types of files are JSON, so we pass that information on as well.
     *
     * @param absolutePath the path to the file. Contrary to the variable name, it doesn't
     *      have to be an absolute path, as long as Java can find it.
     */
	public GM2Shader(String absolutePath){
		super(absolutePath, true);
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
     * Instantiates a new GM2Shader for each one that it finds.
     *
     * @return an ArrayList of GM2Shader representing all of the shaders found in the project
     *      directory
     */
    public static ArrayList<GM2Shader> allFiles(String directory){
		File folder=new File(directory+FOLDER);
		ArrayList<GM2Shader> list=new ArrayList<GM2Shader>();
		if (folder.exists()){
			for (File entry : folder.listFiles()){
				if (entry.isDirectory()){
					GM2Shader file=new GM2Shader(entry.getAbsolutePath()+"\\"+entry.getName()+EXTENSION);
					if (file.exists()){
						list.add(file);
					}
				}
			}
		}
		return list;
	}
}