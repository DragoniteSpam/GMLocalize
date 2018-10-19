package assets.gm1;

import java.util.ArrayList;
import java.lang.UnsupportedOperationException;

import java.io.*;
import org.w3c.dom.*;
import javax.xml.parsers.*;

/**
 * Wrapper for java.io.File that contains a few methods for reading the data out of them 
 * and, more importantly, marking them as "in use" or not.
 *
 * @author DragoniteSpam
 */
public class GM1File extends File {
    protected static final String GMS1_EXTENSION=".gmx";
    protected static String typeName="File";
	
	private boolean inUse;
	private String assetName;
    
    protected Document document;
    protected String plainText;
    protected boolean isXML;
	
    /**
     * Constructor for a GM1File. Automatically extracts the XML document if the file exists
     * and it's supposed to be an XML document, and automatically extracts the plain text if
     * the file exists. If the file exists it figures out the asset name based by removing the
     * extension, otherwise it sets the asset name equal to the absolutePath exactly.
     *
     * @param absolutePath the path to the file. Contrary to the variable name, it doesn't
     *      have to be an absolute path, as long as Java can find it.
     * @param isXML should be true if the file type is supposed to be an XML file, false otherwise.
     *      As a general rule, files that end with .*.gmx are usually XML.
     */
	public GM1File(String absolutePath, boolean isXML){
		super(absolutePath);
        if (exists()){
            this.assetName=getName().substring(0, getName().indexOf('.'));
        } else {
            this.assetName=absolutePath;
        }
		this.inUse=false;
        this.isXML=isXML;
        
        this.document=parseXMLDocument();
        this.plainText=readPlainText();
	}
	
    /**
     * Determines whether or not the file is known to be in use.
     *
     * @return true if the file is known to be in use, false otherwise.
     */
	public final boolean isInUse(){
		return this.inUse;
	}
	
    /**
     * Marks the file as in use. There is no way of undoing this.
     */
	public final void find(){
		this.inUse=true;
	}
	
    /**
     * Returns the asset name. If the file is located at "C:\Pong.gmx\objects\Controller.object.gmx,"
     * the asset name will be "Controller."
     *
     * @return the asset name
     */
	public String getAssetName(){
		return this.assetName;
	}
    
    /**
     * Returns the XML Document if the file exists and is a valid XML file (as specified in the
     * constructor). If either of these conditions are not true, it'll return null.
     *
     * @return the XML Document
     */
    public Document getDocument(){
        return this.document;
    }
    
    /**
     * Returns the plain text of the file, if the file exists. If it doesn't exist, it'll return null.
     * The whitespace is trimmed off of each line and each of the lines are concatenated together,
     * resulting in one long string with no newline characters. If you don't want these things, you
     * may want to parse the file with normal file operations.
     *
     * @return the plain text of the file
     */
    public String getPlainText(){
        return this.plainText;
    }
    
    /**
     * Returns the name of the asset type. At least, that's what it does in the subclasses; Java
     * doesn't allow you to make methods of non-abstract classes abstract, so it throws an
     * UnsupportedOperationException instead.
     *
     * @return nothing, because it throws an exception instead
     */
    public String getTypeName(){
        throw new UnsupportedOperationException("GM1File.getTypeName() is only implemented in its child classes. It's supposed to be abstract, but Java doesn't allow that.");
    }
    
    /**
     * If the file exists and its type is an XML file (as specified in the constructor), it
     * parses and returns the XML Document. Otherwise, it doesn't do anything.
     *
     * @return the XML Document, if available, or null
     */
    private final Document parseXMLDocument(){
        if (!exists()||!isXML){
            return null;
        }
        
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
    
    /**
     * If the file exists, it reads the information out of it in plain text. Each line is trimmed
     * and they're concatenated together, resulting in one long string with no newline characters.
     *
     * @return the plain text of the file, if available, or null
     */
    private final String readPlainText(){
        if (!exists()){
            return "";
        }
        
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
    
    /*
     * There has got to be a way to simplify these. I just don't know what it is.
     * Note: I'm not familiar with XML vocabulary, so if/when I get words wrong, let'searching
     * hope that people are able to figure out what I'm talking about anyway.
     */
    
    /**
     * Finds the values of element attributes of top-level XML elements of the file's XML
     * document which match the outerName parameter, if the document exists.
     *
     * @param outerName the name of the top-level XML element
     * @param attributeName the name of the attribute you're searching for
     * @return an ArrayList of Strings containing the values found in the element attributes
     */
    protected ArrayList<String> xmlGetPrimaryAttributes(String outerName, String attributeName){
        if (this.document==null){
            return null;
        }
        
        ArrayList<String> strings=new ArrayList<String>();
        
        NodeList outer=document.getElementsByTagName(outerName);
        for (int i=0; i<outer.getLength(); i++){
            String name=((Element)outer.item(i)).getAttribute(attributeName);
            if (name.length()>0){
                strings.add(name);
            }
        }
        
        return strings;
    }
    
    /**
     * Finds the content of elements of top-level XML elements of the file's XML document which
     * match the outerName parameter, if the document exists.
     *
     * @param outerName the name of the top-level XML element
     * @return an ArrayList of Strings containing the values found in the elements
     */
    protected ArrayList<String> xmlGetPrimaryValues(String outerName){
        if (this.document==null){
            return null;
        }
        
        ArrayList<String> strings=new ArrayList<String>();
        
        NodeList outer=document.getElementsByTagName(outerName);
        for (int i=0; i<outer.getLength(); i++){
            String name=((Element)outer.item(i)).getTextContent();
            if (name.length()>0){
                strings.add(name);
            }
        }
        
        return strings;
    }
    
    /**
     * Finds the values of attributes of second-level XML elements of the file's XML document of
     * innerName of outerName, if the document exists.
     *
     * @param outerName the name of the top-level XML element
     * @param innerName the name of the second-level XML element
     * @param attributeName the name of the attribute you're searching for
     * @return an ArrayList of Strings containing the values found in the element attributes
     */
    protected ArrayList<String> xmlGetDefaultAttributes(String outerName, String innerName, String attributeName){
        if (this.document==null){
            return null;
        }
        
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
    
    /**
     * Finds the content of second-level XML elements of the file's XML document of
     * innerName of outerName, if the document exists.
     *
     * @param outerName the name of the top-level XML element
     * @param innerName the name of the second-level XML element
     * @return an ArrayList of Strings containing the values found in the elements
     */
    protected ArrayList<String> xmlGetDefaultValues(String outerName, String innerName){
        if (this.document==null){
            return null;
        }
        
        ArrayList<String> strings=new ArrayList<String>();
        
        NodeList outer=document.getElementsByTagName(outerName);
        for (int i=0; i<outer.getLength(); i++){
            NodeList inner=((Element)outer.item(i)).getElementsByTagName(innerName);
            for (int j=0; j<inner.getLength(); j++){
                String content=((Element)inner.item(j)).getTextContent();
                if (content.length()>0){
                    strings.add(content);
                }
            }
        }
        
        return strings;
    }
}