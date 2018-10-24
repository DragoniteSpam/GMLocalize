package assets.gm2;

import java.util.ArrayList;

import java.io.*;

/**
 * A file representing Game Maker Studio 2 Object assets.
 *
 * @author DragoniteSpam
 */
public class GM2Object extends GM2File {
    private static final String FOLDER="\\objects";
    private static final String EXTENSION=".yy";
    protected static String typeName="Object";
	
	private String codeString;
    
    /**
     * Constructor for a GM2Object file. Essentially a wrapper for the GM2File constructor.
     * These types of files are JSON, so we pass that information on as well.
     *
     * @param absolutePath the path to the file. Contrary to the variable name, it doesn't
     *      have to be an absolute path, as long as Java can find it.
     */
	public GM2Object(String absolutePath){
		super(absolutePath, true);
		this.codeString=code();
	}
	
	/**
     * Returns all of the code of the object's events if it exists and has any code. If it doesn't
	 * exist or has no events, it'll return an empty string.
     * The whitespace is trimmed off of each line and each of the lines are concatenated together,
     * resulting in one long string with no newline characters. If you don't want these things, you
     * may want to parse the file with normal file operations.
     *
     * @return the object's event code
     */
    public String getCodeString(){
        return this.codeString;
    }
    
    /**
     * Returns the name of the asset type; in this case, "Object."
     *
     * @return the name of the asset type
     */
    public String getTypeName(){
        return typeName;
    }
    
    /**
     * Searches a folder (the project folder + the asset folder) for files with the asset extension.
     * Instantiates a new GM2Object for each one that it finds.
     *
     * @return an ArrayList of GM2Object representing all of the objects found in the project
     *      directory
     */
    public static ArrayList<GM2Object> allFiles(String directory){
		File folder=new File(directory+FOLDER);
		ArrayList<GM2Object> list=new ArrayList<GM2Object>();
		if (folder.exists()){
			for (File entry : folder.listFiles()){
				if (entry.isDirectory()){
					GM2Object file=new GM2Object(entry.getAbsolutePath()+"\\"+entry.getName()+EXTENSION);
					if (file.exists()){
						list.add(file);
					}
				}
			}
		}
		return list;
	}
	
	public ArrayList<String> allSprites(){
		ArrayList<String> values=new ArrayList<String>();
		
		values.add(json.getString("spriteId"));
		values.add(json.getString("maskSpriteId"));
		
		return values;
	}
	
	public ArrayList<String> allObjects(){
		ArrayList<String> values=new ArrayList<String>();
		
		values.add(json.getString("parentObjectId"));
		
		// you don't need to get the objects involved in collision events because they're
		// either in use somewhere else already or a parent of something that's used somewhere
		// else, but if you wanted to, it would look something like this
		/*
		JSONArray events = obj.getJSONArray("eventList");
		for (int i = 0; i < events.length(); i++){
			values.add(events.getJSONObject(i).getString("collisionObjectId"));
		}
		*/
		
		return values;
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
							builder.append(line.trim());
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