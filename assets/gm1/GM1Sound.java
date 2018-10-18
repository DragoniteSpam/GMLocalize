package assets.gm1;

import java.util.ArrayList;

import java.io.*;
import org.w3c.dom.*;
import javax.xml.parsers.*;

public class GM1Sound extends GM1File {
    private static final String FOLDER=".\\sound";
    private static final String EXTENSION=".sound.gmx";
    
	public GM1Sound(String absolutePath){
		super(absolutePath, true);
	}
    
    public static ArrayList<GM1Sound> allFiles(String directory){
		File folder=new File(directory);
		ArrayList<GM1Sound> list=new ArrayList<GM1Sound>();
		if (folder.exists()){
			for (File entry : folder.listFiles()){
				if (!entry.isDirectory()&&entry.getName().endsWith(EXTENSION)){
					list.add(new GM1Sound(entry.getAbsolutePath()));
				}
			}
		}
		return list;
	}
}