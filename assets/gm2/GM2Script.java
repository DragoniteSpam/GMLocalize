package assets.gm2;

import java.util.ArrayList;

import java.io.*;

/**
 * A file representing Game Maker Studio 2 Script assets.
 *
 * @author DragoniteSpam
 */
public class GM2Script extends GM2File {
    private static final String FOLDER="\\scripts";
    private static final String EXTENSION=".yy";
    protected static String typeName="Script";
	
	private String codeString;
    
    /**
     * Constructor for a GM2Script file. Essentially a wrapper for the GM2File constructor.
     * These types of files are plain text, so we pass that information on as well.
     *
     * @param absolutePath the path to the file. Contrary to the variable name, it doesn't
     *      have to be an absolute path, as long as Java can find it.
     */
	public GM2Script(String absolutePath){
		super(absolutePath, false);
		this.codeString=code();
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
     * Instantiates a new GM2Script for each one that it finds.
     *
     * @return an ArrayList of GM2Script representing all of the scripts found in the project
     *      directory
     */
    public static ArrayList<GM2Script> allFiles(String directory){
		File folder=new File(directory+FOLDER);
		ArrayList<GM2Script> list=new ArrayList<GM2Script>();
		if (folder.exists()){
			for (File entry : folder.listFiles()){
				if (entry.isDirectory()){
					if (entry.getName().charAt(0)=='@'){
						// compatibility script folders are prefixed with "@" for some reason
						GM2Script file=new GM2Script(entry.getAbsolutePath()+"\\"+entry.getName().substring(1, entry.getName().length())+EXTENSION);
						if (file.exists()){
							list.add(file);
						} else {
							System.out.println("Didn't find "+file.getAbsolutePath());
						}
					} else {
						GM2Script file=new GM2Script(entry.getAbsolutePath()+"\\"+entry.getName()+EXTENSION);
						if (file.exists()){
							list.add(file);
						} else {
							System.out.println("Didn't find "+file.getAbsolutePath());
						}
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
            FileReader reader=new FileReader(changeExtension(".gml"));
            BufferedReader bufferedReader=new BufferedReader(reader);
            String line;
            
            while ((line=bufferedReader.readLine())!=null){
                builder.append(line.trim());
            }
        
            bufferedReader.close();
        } catch (FileNotFoundException e){
            System.err.println("Didn't find the file: "+getName());
        } catch (IOException e){
            System.err.println("Something went wrong in: "+getName());
        }
        
        return builder.toString();
    }
}