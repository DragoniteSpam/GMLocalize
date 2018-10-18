package assets.gm1;

import java.io.*;
import org.w3c.dom.*;
import javax.xml.parsers.*;

public class GM1Script extends GM1File {
    private static final String FOLDER=".\\scripts";
    private static final String EXTENSION=".gml";
    
	public GM1Script(String absolutePath){
		super(absolutePath);
	}
    
    public static ArrayList<GM1Script> allFiles(String directory){
		File folder=new File(directory);
		ArrayList<GM1Script> list=new ArrayList<GM1Script>();
		if (folder.exists()){
			for (File entry : folder.listFiles()){
				if (!entry.isDirectory()&&entry.getName().endsWith(EXTENSION)){
					list.add(new GM1Script(entry.getAbsolutePath()));
				}
			}
		}
		return list;
	}
}