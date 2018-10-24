package assets.gm2;

import java.util.ArrayList;

import java.io.*;

/**
 * A file representing Game Maker Studio 2 Room assets.
 *
 * @author DragoniteSpam
 */
public class GM2Room extends GM2File {
    private static final String FOLDER="\\rooms";
    private static final String EXTENSION=".yy";
    protected static String typeName="Room";
	
	private String codeString;
    
    /**
     * Constructor for a GM2Room file. Essentially a wrapper for the GM2File constructor.
     * These types of files are JSON, so we pass that information on as well.
     *
     * @param absolutePath the path to the file. Contrary to the variable name, it doesn't
     *      have to be an absolute path, as long as Java can find it.
     */
	public GM2Room(String absolutePath){
		super(absolutePath, true);
		this.codeString=code();
	}
    
    /**
     * Returns the name of the asset type; in this case, "Room."
     *
     * @return the name of the asset type
     */
    public String getTypeName(){
        return typeName;
    }
	
	/**
     * Returns all of the creation code of the room's instances, and the room itself, assuming the room
	 * exists. If it doesn't exist or has no code to speak of, it'll return an empty string.
     * The whitespace is trimmed off of each line and each of the lines are concatenated together,
     * resulting in one long string with no newline characters. If you don't want these things, you
     * may want to parse the file with normal file operations.
     *
     * @return the room's total creation code
     */
    public String getCodeString(){
        return this.codeString;
    }
    
    /**
     * Searches a folder (the project folder + the asset folder) for files with the asset extension.
     * Instantiates a new GM2Room for each one that it finds.
     *
     * @return an ArrayList of GM2Room representing all of the rooms found in the project
     *      directory
     */
    public static ArrayList<GM2Room> allFiles(String directory){
		File folder=new File(directory+FOLDER);
		ArrayList<GM2Room> list=new ArrayList<GM2Room>();
		if (folder.exists()){
			for (File entry : folder.listFiles()){
				if (entry.isDirectory()){
					GM2Room file=new GM2Room(entry.getAbsolutePath()+"\\"+entry.getName()+EXTENSION);
					if (file.exists()){
						list.add(file);
					}
				}
			}
		}
		return list;
	}
	
	private final String code(){
		if (!exists()){
            return "";
        }
		
        StringBuilder builder=new StringBuilder();
        
        try {
			File folder=new File(getParentFile().getAbsolutePath());
			for (File entry : folder.listFiles()){
				if (!entry.isDirectory()){
					if (entry.getName().endsWith(GML_EXTENSION)){
						FileReader reader=new FileReader(entry);
						BufferedReader bufferedReader=new BufferedReader(reader);
						String line;
						
						while ((line=bufferedReader.readLine())!=null){
							builder.append(line.trim()+" ");
						}
					
						bufferedReader.close();
					}
				}
			}
        } catch (FileNotFoundException e){
            System.err.println("Didn't find the file: "+getName());
        } catch (IOException e){
            System.err.println("Something went wrong in: "+getName());
        }
        
        return builder.toString();
	}
}