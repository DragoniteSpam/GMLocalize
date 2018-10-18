package assets.gm1;

import java.util.ArrayList;

import java.io.*;
import org.w3c.dom.*;
import javax.xml.parsers.*;

public class GM1Sprite extends GM1File {
    private static final String FOLDER=".\\sprites";
    private static final String EXTENSION=".sprite.gmx";
    
	public GM1Sprite(String absolutePath){
		super(absolutePath, true);
	}
    
    public static ArrayList<GM1Sprite> allFiles(String directory){
		File folder=new File(directory);
		ArrayList<GM1Sprite> list=new ArrayList<GM1Sprite>();
		if (folder.exists()){
			for (File entry : folder.listFiles()){
				if (!entry.isDirectory()&&entry.getName().endsWith(EXTENSION)){
					list.add(new GM1Sprite(entry.getAbsolutePath()));
				}
			}
		}
		return list;
	}
}