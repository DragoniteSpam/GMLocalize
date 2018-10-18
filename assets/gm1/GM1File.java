package assets.gm1;

import java.util.ArrayList;

import java.io.*;
import org.w3c.dom.*;
import javax.xml.parsers.*;

public class GM1File extends File {
    protected static final String GMS1_EXTENSION=".gmx";
	
	private boolean inUse;
	private String assetName;
    
    protected Document document;
    protected String plainText;
	
	public GM1File(String absolutePath){
		super(absolutePath);
		this.assetName=getName().substring(0, getName().indexOf('.'));
		this.inUse=false;
        
        this.document=parseXMLDocument();
        this.plainText=readPlainText();
	}
	
	public boolean isInUse(){
		return this.inUse;
	}
	
	public void find(){
		this.inUse=true;
	}
	
	public String getAssetName(){
		return this.assetName;
	}
    
    public Document getDocument(){
        return this.document;
    }
    
    public String getPlainText(){
        return this.plainText;
    }
    
    private final Document parseXMLDocument(){
        try {
            DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
            DocumentBuilder builder=factory.newDocumentBuilder();
            StringBuilder xmlBuilder=new StringBuilder();
            xmlBuilder.append("<?xml version=\"1.0\"?>");
            xmlBuilder.append("<wrapper>");
            
            try {
                FileReader reader=new FileReader(getAbsolutePath());
                BufferedReader bufferedReader=new BufferedReader(reader);
                String line;
                
                while ((line=bufferedReader.readLine())!=null){
                    // The space is because the documentbuilder likes to ignore newlines.
                    // We need newlines because without them it gets very hard to identify
                    // individual tokens in cases where lines blend together (the end of
                    // comments, etc.)
                    xmlBuilder.append(line.trim()+" ");
                }
            
                bufferedReader.close();
            } catch (FileNotFoundException e){
                System.err.println("Didn't find the file: "+getName());
            } catch (IOException e){
                System.err.println("Something went wrong in: "+getName());
            }
            
            xmlBuilder.append("</wrapper>");
            return builder.parse(new ByteArrayInputStream(xmlBuilder.toString().getBytes("UTF-8")));
        } catch (Exception e){
            System.err.println("Something bad happened when reading the xml of some assets");
        }
        
        return null;
    }
    
    private final String readPlainText(){
        StringBuilder builder=new StringBuilder();
        
        try {
            FileReader reader=new FileReader(getAbsolutePath());
            BufferedReader bufferedReader=new BufferedReader(reader);
            String line;
            
            while ((line=bufferedReader.readLine())!=null){
                builder.append(line.trim());
            }
        
            bufferedReader.close();
        } catch (FileNotFoundException e){
            System.err.println("Didn't find the file: "+getName());
        } catch (IOException e){
            System.err.println("Something went wrong in: "+getName());
        }
        
        return builder.toString();
    }
    
    protected ArrayList<String> xmlGetDefaultAttributes(String outerName, String innerName, String attributeName){
        ArrayList<String> strings=new ArrayList<String>();
        
        NodeList outer=document.getElementsByTagName(outerName);
        for (int i=0; i<outer.getLength(); i++){
            NodeList inner=((Element)outer.item(i)).getElementsByTagName(innerName);
            for (int j=0; j<inner.getLength(); j++){
                String name=((Element)inner.item(j)).getAttribute(attributeName);
                if (name.length()>0){
                    strings.add(name);
                }
            }
        }
        
        return strings;
    }
}