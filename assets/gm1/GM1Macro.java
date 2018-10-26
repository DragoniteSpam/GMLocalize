package assets.gm1;

import java.util.ArrayList;

import java.io.*;
import org.w3c.dom.*;
import javax.xml.parsers.*;

/**
 * As you are probably aware, Macros in Game Maker Studio 1 aren't stored as files but as
 * part of the .project.gmx XML document, but we can get away with treating them as such
 * anyway so that we don't have to write a separate system for dealing with them.
 *
 * @author DragoniteSpam
 */
public class GM1Macro extends GM1File {
    protected static String typeName="Macro";
    
    private String name;
    
    /**
     * Constructor for a GM1Macro file. Essentially a wrapper for the GM1File constructor.
     * We don't actually pass it a file name, since it doesn't exist, and therefore inform
     * it that it is not XML, either.
     *
     * @param name the name of the macro.
     */
	public GM1Macro(String name){
		super("", false);
        this.name=name;
	}
    
    /**
     * Returns the name of the asset type; in this case, "Macro."
     *
     * @return the name of the asset type
     */
    public String getTypeName(){
        return typeName;
    }
    
    /**
     * Returns the asset name. This is simply the name that was passed to the constructor, instead
     * of a name determined by the path and file extension.
     *
     * @return the asset name
     */
    public String getAssetName(){
		return this.name;
	}
}