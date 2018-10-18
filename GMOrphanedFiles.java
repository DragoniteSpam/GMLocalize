import java.util.ArrayList;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileNotFoundException;

public class GMOrphanedFiles {
	public static final String ROOT_FOLDER=".\\project";
	
	public static final String BACKGROUND_FOLDER=".\\project\\background";
	public static final String FONT_FOLDER=".\\project\\fonts";
	public static final String OBJECT_FOLDER=".\\project\\objects";
	public static final String PATH_FOLDER=".\\project\\paths";
	public static final String ROOM_FOLDER=".\\project\\rooms";
	public static final String SCRIPT_FOLDER=".\\project\\scripts";
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
		ArrayList<GMFile> allProjects=allFiles(ROOT_FOLDER, ".project.gmx");
		
		ArrayList<GMFile> allScripts=allFiles(SCRIPT_FOLDER, ".gml");
		ArrayList<GMFile> allObjects=allFiles(OBJECT_FOLDER, ".object.gmx");
		ArrayList<GMFile> allRooms=allFiles(ROOM_FOLDER, ".room.gmx");
		ArrayList<GMFile> allTimelines=allFiles(TIMELINE_FOLDER, ".timeline.gmx");
		
		// Assets
		
		try {
			GMAssetType gmoBackgrounds=new GMAssetType(BACKGROUND_FOLDER, ".background.gmx");
			gmoBackgrounds.searchAll(allScripts, allObjects, allRooms, allTimelines);
		} catch (FileNotFoundException e){
			System.err.println(" *** There aren't any Background resources");
		}
		
		try {
			GMAssetType gmoFonts=new GMAssetType(FONT_FOLDER, ".font.gmx");
			gmoFonts.searchAll(allScripts, allObjects, allRooms, allTimelines);
		} catch (FileNotFoundException e){
			System.err.println(" *** There aren't any Font resources");
		}
		
		try {
			GMAssetType gmoObjects=new GMAssetType(OBJECT_FOLDER, ".object.gmx");
			gmoObjects.searchAll(allScripts, allObjects, allRooms, allTimelines);
		} catch (FileNotFoundException e){
			System.err.println(" *** There aren't any Object resources");
		}
		
		try {
			GMAssetType gmoPaths=new GMAssetType(PATH_FOLDER, ".path.gmx");
			gmoPaths.searchAll(allScripts, allObjects, allRooms, allTimelines);
		} catch (FileNotFoundException e){
			System.err.println(" *** There aren't any Path resources");
		}
		
		try {
			GMAssetType gmoRooms=new GMAssetType(ROOM_FOLDER, ".room.gmx");
			gmoRooms.searchAll(allScripts, allObjects, allRooms, allTimelines);
		} catch (FileNotFoundException e){
			System.err.println(" *** There aren't any Rooms resources (you should change that, your game won't run without them)");
		}
		
		try {
			GMAssetType gmoScripts=new GMAssetType(SCRIPT_FOLDER, ".gml");
			gmoScripts.searchAll(allScripts, allObjects, allRooms, allTimelines);
		} catch (FileNotFoundException e){
			System.err.println(" *** There aren't any Scripts resources (how?)");
		}
		
		try {
			GMAssetType gmoSounds=new GMAssetType(SOUND_FOLDER, ".sound.gmx");
			gmoSounds.searchAll(allScripts, allObjects, allRooms, allTimelines);
		} catch (FileNotFoundException e){
			System.err.println(" *** There aren't any Sounds resources");
		}
		
		try {
			GMAssetType gmoSprites=new GMAssetType(SPRITE_FOLDER, ".sprite.gmx");
			gmoSprites.searchAll(allScripts, allObjects, allRooms, allTimelines);
		} catch (FileNotFoundException e){
			System.err.println(" *** There aren't any Sprites resources");
		}
		
		try {
			GMAssetType gmoTimelines=new GMAssetType(TIMELINE_FOLDER, ".timeline.gmx");
			gmoTimelines.searchAll(allScripts, allObjects, allRooms, allTimelines);
		} catch (FileNotFoundException e){
			System.err.println(" *** There aren't any Timeline resources");
		}
		
		// Macros
		
		try {
			GMProjectFile macros=new GMProjectFile(ROOT_FOLDER, ".project.gmx", allProjects);
			macros.searchAll(allScripts, allObjects, allRooms, allTimelines);
		} catch (FileNotFoundException e){
			System.err.println(" *** There aren't any Project File resources (is this even a Game Maker project?)");
		}
		
		// Enums
	}

	public static ArrayList<GMFile> allFiles(String folderName, String extension){
		File folder=new File(folderName);
		ArrayList<GMFile> list=new ArrayList<GMFile>();
		if (folder.exists()){
			for (File entry : folder.listFiles()){
				if (!entry.isDirectory()&&entry.getName().endsWith(extension)){
					list.add(new GMFile(entry, folderName, extension));
				}
			}
		}
		return list;
	}
}