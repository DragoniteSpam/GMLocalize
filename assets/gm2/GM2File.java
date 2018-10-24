package assets.gm2;

import java.util.ArrayList;
import java.lang.UnsupportedOperationException;

import java.io.*;

/**
 * Wrapper for java.io.File that contains a few methods for reading the data out of them 
 * and, more importantly, marking them as "in use" or not.
 *
 * @author DragoniteSpam
 */
public class GM2File extends File {
    protected static final String GMS2_EXTENSION=".yy";
    protected static String typeName="File";
	
	private boolean inUse;
	private String assetName;
    
    protected String plainText;
    protected boolean isJSON;
	
    /**
     * Constructor for a GM2File. Automatically extracts the JSON document if the file exists
     * and it's supposed to be a JSON document, and automatically extracts the plain text if
     * the file exists. If the file exists it figures out the asset name based by removing the
     * extension, otherwise it sets the asset name equal to the absolutePath exactly.
     *
     * @param absolutePath the path to the file. Contrary to the variable name, it doesn't
     *      have to be an absolute path, as long as Java can find it.
     * @param isJSON should be true if the file type is supposed to be an JSON file, false otherwise.
     *      As a general rule, files that end with .*.yy are JSON.
     */
	public GM2File(String absolutePath, boolean isJSON){
		super(absolutePath);
        if (exists()){
            this.assetName=getName().substring(0, getName().indexOf('.'));
        } else {
            this.assetName=absolutePath;
        }
		this.inUse=false;
        this.isJSON=isJSON;
	}
	
    /**
     * Determines whether or not the file is known to be in use.
     *
     * @return true if the file is known to be in use, false otherwise.
     */
	public final boolean isInUse(){
		return this.inUse;
	}
	
    /**
     * Marks the file as in use. There is no way of undoing this.
     */
	public final void find(){
		this.inUse=true;
	}
	
    /**
     * Returns the asset name. If the file is located at "C:\Pong\objects\Controller\Controller.yy,"
     * the asset name will be "Controller."
     *
     * @return the asset name
     */
	public String getAssetName(){
		return this.assetName;
	}
    
    /**
     * Returns the plain text of the file, if the file exists. If it doesn't exist, it'll return null.
     * The whitespace is trimmed off of each line and each of the lines are concatenated together,
     * resulting in one long string with no newline characters. If you don't want these things, you
     * may want to parse the file with normal file operations.
     *
     * @return the plain text of the file
     */
    public String getPlainText(){
        return this.plainText;
    }
    
    /**
     * Returns the name of the asset type. At least, that's what it does in the subclasses; Java
     * doesn't allow you to make methods of non-abstract classes abstract, so it throws an
     * UnsupportedOperationException instead.
     *
     * @return nothing, because it throws an exception instead
     */
    public String getTypeName(){
        throw new UnsupportedOperationException("GM2File.getTypeName() is only implemented in its child classes. It's supposed to be abstract, but Java doesn't allow that.");
    }
	
	protected String changeExtension(String newExtension){
		return getAbsolutePath().substring(0, getAbsolutePath().indexOf('.'))+newExtension;
	}
}