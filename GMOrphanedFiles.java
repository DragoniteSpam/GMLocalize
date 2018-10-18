import java.util.ArrayList;

import java.io.*;
import org.w3c.dom.*;
import javax.xml.parsers.*;

import assets.gm1.*;

public class GMOrphanedFiles {
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
        ArrayList<GM1File> assets=new ArrayList<GM1File>();
        ArrayList<String> assetsInUse=new ArrayList<String>();
        ArrayList<String> code=new ArrayList<String>();
        
        /*
         * Macros
         */
        ArrayList<String> allMacroNames=rootProject.allMacros();
        code.addAll(rootProject.allMacroCode());
        ArrayList<GM1Macro> macros=new ArrayList<GM1Macro>();
        for (String n : allMacroNames){
            GM1Macro m=new GM1Macro(n);
            macros.add(m);
            assets.add(m);
        }
        
        /*
         * Backgrounds
         */
        ArrayList<String> allBackgroundAssets=new ArrayList<String>();
        
        ArrayList<GM1Background> backgrounds=GM1Background.allFiles(directory);
        for (GM1Background background : backgrounds){
            allBackgroundAssets.add(background.getAssetName());
            
            assets.add(background);
        }
        
        /*
         * Fonts
         */
        ArrayList<String> allFontAssets=new ArrayList<String>();
        
        ArrayList<GM1Font> fonts=GM1Font.allFiles(directory);
        for (GM1Font font : fonts){
            allFontAssets.add(font.getAssetName());
            
            assets.add(font);
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
            
            assets.add(object);
        }
        
        /*
         * Paths
         */
        ArrayList<String> allPathAssets=new ArrayList<String>();
        
        ArrayList<GM1Path> paths=GM1Path.allFiles(directory);
        for (GM1Path path : paths){
            allPathAssets.add(path.getAssetName());
            assets.add(path);
            // Paths also have a "backroom" element, which is presumably the
            // index of the room that's used as the background, but becuse we
            // all love consistancy it's saved as an integer index and not an
            // asset name and that makes it quite difficult to tell which room
            // is actually being used, since this program iterates over each
            // file in the folder and not the entry in the project file, and
            // the operating system stores the files in alphabetical order.
            
            // That's the long way of me saying I'm not going to detect backgrounds
            // that are only in use for positioning paths. Why are you using paths,
            // anyway?
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
            
            assets.add(room);
        }
        
        /*
         * Scripts
         */
        ArrayList<String> allScriptAssets=new ArrayList<String>();
        
        ArrayList<GM1Script> scripts=GM1Script.allFiles(directory);
        for (GM1Script script : scripts){
            allScriptAssets.add(script.getAssetName());
            code.add(script.getPlainText());
            
            assets.add(script);
        }
        
        /*
         * Shaders
         */
        ArrayList<String> allShaderAssets=new ArrayList<String>();
        
        ArrayList<GM1Shader> shaders=GM1Shader.allFiles(directory);
        for (GM1Shader shader : shaders){
            allShaderAssets.add(shader.getAssetName());
            
            assets.add(shader);
        }
        
        /*
         * Sound
         */
        ArrayList<String> allSoundAssets=new ArrayList<String>();
        
        ArrayList<GM1Sound> sounds=GM1Sound.allFiles(directory);
        for (GM1Sound sound : sounds){
            allSoundAssets.add(sound.getAssetName());
            
            assets.add(sound);
        }
        
        /*
         * Sprites
         */
        ArrayList<String> allSpriteAssets=new ArrayList<String>();
        
        ArrayList<GM1Sprite> sprites=GM1Sprite.allFiles(directory);
        for (GM1Sprite sprite : sprites){
            allSpriteAssets.add(sprite.getAssetName());
            
            assets.add(sprite);
        }
        
        /*
         * Timelines
         */
        ArrayList<String> allTimelineAssets=new ArrayList<String>();
        
        ArrayList<GM1Timeline> timelines=GM1Timeline.allFiles(directory);
        for (GM1Timeline timeline : timelines){
            allTimelineAssets.add(timeline.getAssetName());
            code.add(timeline.code());
            
            assets.add(timeline);
        }
        
        /*
         * Configs don't contain anything useful to us, at least as far as I know.
         * We could do datafiles too, but since they can only be accessed as strings
         * and strings can be broken up (file_text_open_read("folder"+"\"+"filename"))
         * it feels kind of futile.
         */
        
        mark(assets, rootProject.startingRoom());
        
        for (String name : assetsInUse){
            mark(assets, name);
        }
        
        searchCode(assets, code);
        
        for (GM1File f : assets){
            if (!f.isInUse()){
                System.out.println("We didn't find "+f.getTypeName()+": "+f.getAssetName());
            }
        }
    }
    
    public static void searchCode(ArrayList<GM1File> assets, ArrayList<String> code){
        for (GM1File f : assets){
            if (!f.isInUse()&&f.search(code)){
                f.find();
            }
        }
    }
    
    public static void mark(ArrayList<GM1File> assets, String name){
        boolean found=false;
        for (GM1File f : assets){
            if (f.getAssetName().equals(name)){
                if (found){
                    warn("You have duplicate asset(s) named "+name+". Normally Game Maker does not allow this. You should resolve that.");
                }
                found=true;
                f.find();
            }
        }
    }
    
    public static void warn(Object message){
        System.err.println(message);
    }
    
    public static String[] codeSplit(String code){
        ArrayList<String> terms=new ArrayList<String>();
        return null;
    }
}