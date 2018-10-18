package assets.gm1;

import java.util.ArrayList;

import java.io.*;
import org.w3c.dom.*;
import javax.xml.parsers.*;

public class GM1Path extends GM1File {
    private static final String FOLDER=".\\paths";
    private static final String EXTENSION=".path.gmx";
    protected static String typeName="Path";
    
	public GM1Path(String absolutePath){
		super(absolutePath, true);
	}
    
    public String getTypeName(){
        return typeName;
    }
    
    public static ArrayList<GM1Path> allFiles(String directory){
		File folder=new File(directory+FOLDER);
		ArrayList<GM1Path> list=new ArrayList<GM1Path>();
		if (folder.exists()){
			for (File entry : folder.listFiles()){
				if (!entry.isDirectory()&&entry.getName().endsWith(EXTENSION)){
					list.add(new GM1Path(entry.getAbsolutePath()));
				}
			}
		}
		return list;
	}
}