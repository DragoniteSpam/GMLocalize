package assets.gm1;

import java.util.ArrayList;

import java.io.*;
import org.w3c.dom.*;
import javax.xml.parsers.*;

public class GM1Shader extends GM1File {
    private static final String FOLDER=".\\shaders";
    private static final String EXTENSION=".shader";
    
	public GM1Shader(String absolutePath){
		super(absolutePath);
	}
    
    public static ArrayList<GM1Shader> allFiles(String directory){
		File folder=new File(directory);
		ArrayList<GM1Shader> list=new ArrayList<GM1Shader>();
		if (folder.exists()){
			for (File entry : folder.listFiles()){
				if (!entry.isDirectory()&&entry.getName().endsWith(EXTENSION)){
					list.add(new GM1Shader(entry.getAbsolutePath()));
				}
			}
		}
		return list;
	}
}