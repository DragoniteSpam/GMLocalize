package assets.gm1;

import java.util.ArrayList;

import java.io.*;
import org.w3c.dom.*;
import javax.xml.parsers.*;

public class GM1Room extends GM1File {
    private static final String FOLDER=".\\rooms";
    private static final String EXTENSION=".room.gmx";
    
	public GM1Room(String absolutePath){
		super(absolutePath);
	}
    
    public String creationCode(){
        StringBuilder codeBuilder=new StringBuilder();
    
        NodeList code=document.getElementsByTagName("code");
        if (code.getLength()>0){
            // There's no reason for this to be longer than one, but just to be safe
            for (int i=0; i<code.getLength(); i++){
                codeBuilder.append(code.item(i).getTextContent()+" ");
            }
        }
        
        return codeBuilder.toString();
    }
    
    public static ArrayList<GM1Room> allFiles(String directory){
		File folder=new File(directory+FOLDER);
		ArrayList<GM1Room> list=new ArrayList<GM1Room>();
		if (folder.exists()){
			for (File entry : folder.listFiles()){
				if (!entry.isDirectory()&&entry.getName().endsWith(EXTENSION)){
					list.add(new GM1Room(entry.getAbsolutePath()));
				}
			}
		}
		return list;
	}
}