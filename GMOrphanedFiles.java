import java.util.ArrayList;

import java.io.*;
import org.w3c.dom.*;
import javax.xml.parsers.*;

import assets.gm1.*;

public class GMOrphanedFiles {
	public static final String ROOT_FOLDER=".\\project";
	
	public static final String BACKGROUND_FOLDER=".\\project\\background";
	public static final String FONT_FOLDER=".\\project\\fonts";
	public static final String OBJECT_FOLDER=".\\project\\objects";
	public static final String PATH_FOLDER=".\\project\\paths";
	public static final String ROOM_FOLDER=".\\project\\rooms";
	public static final String SCRIPT_FOLDER=".\\project\\scripts";
    public static final String SHADER_FOLDER=".\\project\\shaders";
	public static final String SOUND_FOLDER=".\\project\\sound";
	public static final String SPRITE_FOLDER=".\\project\\sprites";
	public static final String TIMELINE_FOLDER=".\\project\\timelines";
	
	public static final String TRIPLE_SLASH_COMMENT="///";
	public static final String XML_BEGIN_OBJECT_CODE="<string>";
	public static final String XML_END_OBJECT_CODE="</string>";
	// Because why would the tags for object code and room code be the same.
	public static final String XML_BEGIN_ROOM_CODE="<code>";
	public static final String XML_END_ROOM_CODE="</code>";
	
	public static void main(String[] args) throws IOException {
        if (args.length==0){
            System.out.println("Incorrect use: use ");
            System.out.println("    java GMOrphanedFiles <project1name> <project2name> .. <projectNname>");
            System.out.println("to use this tool.");
            System.exit(0);
        }
        
        for (String projectName : args){
            GM1Project rootProject1=GM1Project.autoDetect(projectName);
            
            if (rootProject1==null/*&&rootProject2==null*/){
                System.err.println("Couldn't find a Game Maker Studio (1 or 2) project file in the specified folder. "+
                    "Are you sure you're checking the right one?");
                System.exit(0);
            }
            
            if (rootProject1!=null){
                assesGM1Project(projectName, rootProject1);
            }
        }
	}
    
    public static void assesGM1Project(String directory, GM1Project rootProject){
        ArrayList<String> assetsInUse=new ArrayList<String>();
        ArrayList<String> code=new ArrayList<String>();
        
        String startingRoomName=rootProject.startingRoom();
        assetsInUse.add(startingRoomName);
        
        /*
         * Macros
         */
        ArrayList<String> allMacroNames=rootProject.allMacros();
        code.addAll(rootProject.allMacroCode());
        
        /*
         * Backgrounds
         */
        ArrayList<String> allBackgroundAssets=new ArrayList<String>();
        
        ArrayList<GM1Background> backgrounds=GM1Background.allFiles(directory);
        for (GM1Background background : backgrounds){
            allBackgroundAssets.add(background.getAssetName());
        }
        
        /*
         * Fonts
         */
        ArrayList<String> allFontAssets=new ArrayList<String>();
        
        ArrayList<GM1Font> fonts=GM1Font.allFiles(directory);
        for (GM1Font font : fonts){
            allFontAssets.add(font.getAssetName());
        }
        
        /*
         * Objects
         */
        ArrayList<String> allObjectAssets=new ArrayList<String>();
        
        ArrayList<GM1Object> objects=GM1Object.allFiles(directory);
        for (GM1Object object : objects){
            allObjectAssets.add(object.getAssetName());
            assetsInUse.addAll(object.allObjects());
            assetsInUse.addAll(object.allSprites());
            code.add(object.code());
        }
        
        /*
         * Rooms
         */
        ArrayList<String> allRoomAssets=new ArrayList<String>();
        
        ArrayList<GM1Room> rooms=GM1Room.allFiles(directory);
        for (GM1Room room : rooms){
            allRoomAssets.add(room.getAssetName());
            code.add(room.creationCode());
            assetsInUse.addAll(room.allInstances());
            assetsInUse.addAll(room.allBackgrounds());
        }
        
        /*
         * Scripts
         */
        ArrayList<String> allScriptAssets=new ArrayList<String>();
        
        ArrayList<GM1Script> scripts=GM1Script.allFiles(directory);
        for (GM1Script script : scripts){
            allScriptAssets.add(script.getAssetName());
        }
        
        /*
         * Sprites
         */
        ArrayList<String> allSpriteAssets=new ArrayList<String>();
        
        ArrayList<GM1Sprite> sprites=GM1Sprite.allFiles(directory);
        for (GM1Sprite sprite : sprites){
            allSpriteAssets.add(sprite.getAssetName());
        }
    }
}