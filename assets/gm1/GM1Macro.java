package assets.gm1;

import java.util.ArrayList;

import java.io.*;
import org.w3c.dom.*;
import javax.xml.parsers.*;

public class GM1Macro extends GM1File {    
    private String name;
    
	public GM1Macro(String name){
		super("", false);
        this.name=name;
	}
    
    public String getAssetName(){
		return this.name;
	}
}