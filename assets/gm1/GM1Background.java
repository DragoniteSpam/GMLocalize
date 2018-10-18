package assets.gm1;

import java.util.ArrayList;

import java.io.*;
import org.w3c.dom.*;
import javax.xml.parsers.*;

public class GM1Background extends GM1File {
    private static final String FOLDER=".\\background";
    private static final String EXTENSION=".background.gmx";
    protected static String typeName="Background";
    
	public GM1Background(String absolutePath){
		super(absolutePath, true);
	}
    
    public String getTypeName(){
        return typeName;
    }
    
    public static ArrayList<GM1Background> allFiles(String directory){
		File folder=new File(directory+FOLDER);
		ArrayList<GM1Background> list=new ArrayList<GM1Background>();
		if (folder.exists()){
			for (File entry : folder.listFiles()){
				if (!entry.isDirectory()&&entry.getName().endsWith(EXTENSION)){
					list.add(new GM1Background(entry.getAbsolutePath()));
				}
			}
		}
		return list;
	}
}