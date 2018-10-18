package assets.gm1;

import java.util.ArrayList;

import java.io.*;
import org.w3c.dom.*;
import javax.xml.parsers.*;

public class GM1Font extends GM1File {
    private static final String FOLDER=".\\fonts";
    private static final String EXTENSION=".font.gmx";
    
	public GM1Font(String absolutePath){
		super(absolutePath, true);
	}
    
    public static ArrayList<GM1Font> allFiles(String directory){
		File folder=new File(directory);
		ArrayList<GM1Font> list=new ArrayList<GM1Font>();
		if (folder.exists()){
			for (File entry : folder.listFiles()){
				if (!entry.isDirectory()&&entry.getName().endsWith(EXTENSION)){
					list.add(new GM1Font(entry.getAbsolutePath()));
				}
			}
		}
		return list;
	}
}