import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import java.io.*;
import org.json.*;

import assets.gm1.*;
import assets.gm2.*;

/**
 * Class with the main method and a few helper ones. There's a tiny bit of code that will
 * want to be changed if/when I get around to making a GMS2 version.
 *
 * @author DragoniteSpam
 */
public class GMOrphanedFiles {
	private static final String ABSENT_ASSET="00000000-0000-0000-0000-000000000000";
    /**
     * Main method. If you cloned this repository you probably know how it works.
     *
     * @param args the command line arguments; in this case, the names of the project
     *      folders you want to check.
     */
	public static void main(String[] args) throws IOException {
        if (args.length==0){
            System.out.println("Incorrect use: use ");
            System.out.println("    java GMOrphanedFiles <project1name> <project2name> .. <projectNname>");
            System.out.println("to use this tool.");
            System.exit(0);
        }
        
        for (String projectName : args){
            System.out.println("-----------------------------");
            System.out.println(projectName);
            System.out.println("-----------------------------");
            
            GM1Project rootProject1=GM1Project.autoDetect(projectName);
			GM2Project rootProject2=GM2Project.autoDetect(projectName);
            
            if (rootProject1==null&&rootProject2==null){
                System.err.println("We couldn't find a Game Maker Studio (1 or 2) project file in the specified folder. "+
                    "Are you sure you're checking the right one?");
            } else {
				if (rootProject1!=null){
					assesGM1Project(projectName, rootProject1);
				}
				
				if (rootProject2!=null){
					assesGM2Project(projectName, rootProject2);
				}
			}
        }
        
        System.out.println();
        System.out.println("-----------------------------");
        System.out.println();
        System.out.println("There may be more orphaned assets that we haven't been able to find: if "+
            "scr_foo calls scr_bar and scr_bar calls scr_foo, and neither of them are called from "+
            "anywhere else, we're not smart enough to detect that the script is never actually used.");
        System.out.println();
        System.out.println("Additionally, assets that are referenced by name in comments or strings "+
            "will still be marked as \"in use,\" even though they're actually not.");
        System.out.println();
        System.out.println("There may also be some assets that do get used, but not detected: for "+
            "example, if you refer to a sprite by its index instead of its asset name (which is a bad "+
            "idea), or if you were to retrieve the asset through asset_get_name with a compound string "+
            "(which is also a bad idea), we won't be able to tell.");
        System.out.println();
        
        System.out.println("(Hit Enter to quit.)");
        new Scanner(System.in).nextLine();
	}
	
	/**
     * Reads through a Game Maker Studio 2 project and attempts to detect orphaned files.
     * You may want to put this in its own class, possibly GM2Project. I just wanted to write
     * code quickly.
     *
     * @param directory the folder that contains the Game Maker project. main normally gives
     * it a relative path, but an absolute one works just as well.
     * @param rootProject the object representing the .YYP file. It's only used for
     *      extracting the starting room and macros (constants), but it's a good indication
     *      that the project exists.
     */
	public static void assesGM2Project(String directory, GM2Project rootProject){
        HashMap<String, GM2File> assets=new HashMap<String, GM2File>();
        HashMap<String, String> assetsInUse=new HashMap<String, String>();
        ArrayList<String> code=new ArrayList<String>();
        HashMap<String, String> codeTokens;
        
        /*
         * Fonts
         */
        ArrayList<String> allFontAssets=new ArrayList<String>();
        
        ArrayList<GM2Font> fonts=GM2Font.allFiles(directory);
        for (GM2Font font : fonts){
            allFontAssets.add(font.getAssetName());
            
            assets.put(font.getID(), font);
        }
        
        /*
         * Objects
         */
        ArrayList<String> allObjectAssets=new ArrayList<String>();
        
        ArrayList<GM2Object> objects=GM2Object.allFiles(directory);
        for (GM2Object object : objects){
            allObjectAssets.add(object.getAssetName());
			
            code.add(object.getCodeString());
			
			selectiveAddToHashMap(assetsInUse, object.allSprites());
			selectiveAddToHashMap(assetsInUse, object.allObjects());
            
            assets.put(object.getID(), object);
        }
		
		/*
         * Paths
		 * I don't have any paths to test with yet
         */
		
		ArrayList<String> allPathAssets=new ArrayList<String>();
        
        ArrayList<GM2Path> paths=GM2Path.allFiles(directory);
        for (GM2Path path : paths){
            allPathAssets.add(path.getAssetName());
			
            assets.put(path.getID(), path);
        }
		
		/*
         * Rooms
         */
        ArrayList<String> allRoomAssets=new ArrayList<String>();
        
        ArrayList<GM2Room> rooms=GM2Room.allFiles(directory);
        for (GM2Room room : rooms){
            allRoomAssets.add(room.getAssetName());
			selectiveAddToHashMap(assetsInUse, room.allInstances());
			selectiveAddToHashMap(assetsInUse, room.allTilemaps());
			
			code.add(room.getCodeString());
            
            assets.put(room.getID(), room);
        }
		
		/*
         * Scripts
         */
        ArrayList<String> allScriptAssets=new ArrayList<String>();
        
        ArrayList<GM2Script> scripts=GM2Script.allFiles(directory);
        for (GM2Script script : scripts){
            allScriptAssets.add(script.getAssetName());
            code.add(script.getCodeString());
            
            assets.put(script.getID(), script);
        }
		
		/*
         * Shaders
         */
        ArrayList<String> allShaderAssets=new ArrayList<String>();
        
        ArrayList<GM2Shader> shaders=GM2Shader.allFiles(directory);
        for (GM2Shader shader : shaders){
            allShaderAssets.add(shader.getAssetName());
            
            assets.put(shader.getID(), shader);
        }
		
		/*
         * Sound
         */
        ArrayList<String> allSoundAssets=new ArrayList<String>();
        
        ArrayList<GM2Sound> sounds=GM2Sound.allFiles(directory);
        for (GM2Sound sound : sounds){
            allSoundAssets.add(sound.getAssetName());
            
            assets.put(sound.getID(), sound);
        }
		
		/*
         * Sprites
         */
        ArrayList<String> allSpriteAssets=new ArrayList<String>();
        
        ArrayList<GM2Sprite> sprites=GM2Sprite.allFiles(directory);
        for (GM2Sprite sprite : sprites){
            allSpriteAssets.add(sprite.getAssetName());
            
            assets.put(sprite.getID(), sprite);
        }
		
		/*
         * Timelines
		 * don't have any of these to test with either
         */
        ArrayList<String> allTimelineAssets=new ArrayList<String>();
        
        ArrayList<GM2Timeline> timelines=GM2Timeline.allFiles(directory);
        for (GM2Timeline timeline : timelines){
            allTimelineAssets.add(timeline.getAssetName());
            /*code.add(timeline.code());*/
            
            assets.put(timeline.getID(), timeline);
        }
		
		/*
         * Tilesets
         */
        ArrayList<String> allTilesetAssets=new ArrayList<String>();
        
        ArrayList<GM2Tileset> tilesets=GM2Tileset.allFiles(directory);
        for (GM2Tileset tileset : tilesets){
            allTilesetAssets.add(tileset.getAssetName());
            
            assets.put(tileset.getID(), tileset);
        }
		
		/*for (String s : code){
			System.out.println(s+"\n");
		}*/
    }
    
    /**
     * Reads through a Game Maker Studio 1 project and attempts to detect orphaned files.
     * You may want to put this in its own class, possibly GM1Project. I just wanted to write
     * code quickly.
     *
     * @param directory the folder that contains the Game Maker project. main normally gives
     * it a relative path, but an absolute one works just as well.
     * @param rootProject the object representing the .project.gmx file. It's only used for
     *      extracting the starting room and macros (constants), but it's a good indication
     *      that the project exists.
     */
    public static void assesGM1Project(String directory, GM1Project rootProject){
        ArrayList<GM1File> assets=new ArrayList<GM1File>();
        ArrayList<String> assetsInUse=new ArrayList<String>();
        ArrayList<String> code=new ArrayList<String>();
        HashMap<String, String> codeTokens;
        
        
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
            
            String parentName=object.getParent();
            if (parentName!=null){
                assetsInUse.add(parentName);
            }
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
            // index of the room that's used as the background, but because we
            // all love consistency it's saved as an integer index and not an
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
         * Other things
         * These aren't asset files but you might want to know about them
         * all the same
         */
        
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
         * Decided against doing enums, since the way I've structured the code
         * (both the Java and the GML) makes it hard to tell the difference between
         * declaring an enum and using one
         */
        
        /*
         * Configs don't contain anything useful to us, at least as far as I know.
         * We could do datafiles too, but since they can only be accessed as strings
         * and strings can be broken up (file_text_open_read("folder"+"\"+"filename"))
         * it feels kind of futile.
         */
        
        codeTokens=findAllCodeTokens(code);
        
        mark(assets, rootProject.startingRoom());
        
        for (String name : assetsInUse){
            mark(assets, name);
        }
        
        searchCode(assets, codeTokens);
        
        int n=0;
        System.out.println("The following all appear to be orphaned assets:");
        for (GM1File f : assets){
            if (!f.isInUse()){
                System.out.println("\t"+f.getTypeName()+": "+f.getAssetName());
                n++;
            }
        }
        if (n==0){
            System.out.println("\t(none found)");
        } else {
            System.out.println();
            System.out.println("NOTHING HAS BEEN AUTOMATICALLY DELETED. You may wish to have a look "+
                "at each of the assets individually and decide for yourself if you still need them.");
            System.out.println();
            System.out.println("I would highly suggest using source control or at least making a backup "+
                "of your project if you're unsure about deleting something that can't be easily replaced.");
            System.out.println();
            System.out.println("Not all assets listed are necessarily part of your Game Maker project. "+
                "If you manually added appropriately-named resources to one of the folders without "+
                "correctly adding them to Game Maker, or if you previously deleted an asset without "+
                "having Game Maker set to delete removed files from the disk, or if Game Maker bugged "+
                "out and \"forgot\" to rename/remove a file even though it was supposed to, you may "+
                "still see it listed here. In that case, it's probably safe to delete.");
        }
    }
    
    /**
     * Searches the code for asset names. The code is provided in the form of a HashMap instead of
     * strings of text, so looking this up should be considerably faster than before, when it was
     * just strings of text.
     *
     * @param assets ArrayList of strings containing the names of the assets to check for.
     * @param code HashMap of strings containing each individual token that appears in the code.
     */
    public static void searchCode(ArrayList<GM1File> assets, HashMap<String, String> code){
        for (GM1File f : assets){
            if (!f.isInUse()&&code.containsKey(f.getAssetName())){
                f.find();
            }
        }
    }
    
    /**
     * Marks asset(s) with the specified name as "in use." Right now it searches a list so
     * it performs in O(n) time, but if you want it to be faster (say you have thousands and thousands
     * of assets) you should change this to take a HashMap instead.
     *
     * Also, if support for GMS2 ever gets added, GM1File and its subclasses will probably need to
     * be refactored a bit.
     *
     * @param assets ArrayList of GMFiles; ideally exactly one of these has an asset name matching
     * @param name the name of the asset you want to mark as "in use." If you somehow have more than
     *      one asset with the same name, both will be marked and you'll be warned.
     */
    public static void mark(ArrayList<GM1File> assets, String name){
        boolean found=false;
        for (GM1File f : assets){
            if (f.getAssetName().equals(name)){
                if (found){
                    System.err.println("You have duplicate asset(s) named "+name+". Normally Game Maker does not allow this. You should resolve that.");
                }
                found=true;
                f.find();
            }
        }
    }
    
    /**
     * Takes strings of code and separates them into individual tokens. Another way to think about
     * it is that it removes all of the characters that aren't legal-ish variable names and breaks
     * each term into a HashMap.
     *
     * @param code ArrayList containing strings of code
     * @return HashMap containing the individual tokens, as described above. (The key is all we care
     *      about, but it sets the value to the same string as the key. There's probably a better data
     *      structure for this but I can't think of it off the top of my head.)
     */
    public static HashMap<String, String> findAllCodeTokens(ArrayList<String> code){
        HashMap<String, String> tokens=new HashMap<String, String>();
        
        for (String section : code){
            String[] each=section.split("[ \\-+*/%=&\\|^!~.,<>{}()\\[\\];?:#@]");
            for (String s : each){
                if (!tokens.containsKey(s)){
                    tokens.put(s, s);
                }
            }
        }
        
        return tokens;
    }
	
	private static void selectiveAddToHashMap(HashMap<String, String> map, ArrayList<String> values){
		for (String string : values){
			if (!string.equals(ABSENT_ASSET)&&!map.containsKey(string)){
				map.put(string, string);
			}
		}
	}
}