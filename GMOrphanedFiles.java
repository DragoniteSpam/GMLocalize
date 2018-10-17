import java.util.ArrayList;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileNotFoundException;

public class GMOrphanedFiles {
	private static final String BACKGROUND_FOLDER=".\\project\\background";
	private static final String FONT_FOLDER=".\\project\\fonts";
	private static final String OBJECT_FOLDER=".\\project\\objects";
	private static final String PATH_FOLDER=".\\project\\paths";
	private static final String ROOM_FOLDER=".\\project\\rooms";
	private static final String SCRIPT_FOLDER=".\\project\\scripts";
	private static final String SOUND_FOLDER=".\\project\\sound";
	private static final String SPRITE_FOLDER=".\\project\\sprites";
	private static final String TIMELINE_FOLDER=".\\project\\timelines";
	
	private static final String TRIPLE_SLASH_COMMENT="///";
	private static final String XML_BEGIN_OBJECT_CODE="<string>";
	private static final String XML_END_OBJECT_CODE="</string>";
	// Because why would the tags for object code and room code be the same.
	private static final String XML_BEGIN_ROOM_CODE="<code>";
	private static final String XML_END_ROOM_CODE="</code>";
	
	public static void main(String[] args) throws IOException {
		ArrayList<GMFile> allScripts=allFiles(SCRIPT_FOLDER, ".gml");
		ArrayList<GMFile> allObjects=allFiles(OBJECT_FOLDER, ".object.gmx");
		ArrayList<GMFile> allRooms=allFiles(ROOM_FOLDER, ".room.gmx");
		ArrayList<GMFile> allTimelines=allFiles(TIMELINE_FOLDER, ".timeline.gmx");
		
		try {
			GMOrphanedFiles gmoBackgrounds=new GMOrphanedFiles(BACKGROUND_FOLDER, ".background.gmx");
			gmoBackgrounds.searchAll(allScripts, allObjects, allRooms, allTimelines);
		} catch (FileNotFoundException e){
			System.err.println(" *** There aren't any Background resources");
		}
		
		try {
			GMOrphanedFiles gmoFonts=new GMOrphanedFiles(FONT_FOLDER, ".font.gmx");
			gmoFonts.searchAll(allScripts, allObjects, allRooms, allTimelines);
		} catch (FileNotFoundException e){
			System.err.println(" *** There aren't any Font resources");
		}
		
		try {
			GMOrphanedFiles gmoObjects=new GMOrphanedFiles(OBJECT_FOLDER, ".object.gmx");
			gmoObjects.searchAll(allScripts, allObjects, allRooms, allTimelines);
		} catch (FileNotFoundException e){
			System.err.println(" *** There aren't any Object resources");
		}
		
		try {
			GMOrphanedFiles gmoPaths=new GMOrphanedFiles(PATH_FOLDER, ".path.gmx");
			gmoPaths.searchAll(allScripts, allObjects, allRooms, allTimelines);
		} catch (FileNotFoundException e){
			System.err.println(" *** There aren't any Path resources");
		}
		
		try {
			GMOrphanedFiles gmoRooms=new GMOrphanedFiles(ROOM_FOLDER, ".room.gmx");
			gmoRooms.searchAll(allScripts, allObjects, allRooms, allTimelines);
		} catch (FileNotFoundException e){
			System.err.println(" *** There aren't any Rooms resources (you should change that, your game won't run without them)");
		}
		
		try {
			GMOrphanedFiles gmoScripts=new GMOrphanedFiles(SCRIPT_FOLDER, ".gml");
			gmoScripts.searchAll(allScripts, allObjects, allRooms, allTimelines);
		} catch (FileNotFoundException e){
			System.err.println(" *** There aren't any Scripts resources (how?)");
		}
		
		try {
			GMOrphanedFiles gmoSounds=new GMOrphanedFiles(SOUND_FOLDER, ".sound.gmx");
			gmoSounds.searchAll(allScripts, allObjects, allRooms, allTimelines);
		} catch (FileNotFoundException e){
			System.err.println(" *** There aren't any Sounds resources");
		}
		
		try {
			GMOrphanedFiles gmoSprites=new GMOrphanedFiles(SPRITE_FOLDER, ".sprite.gmx");
			gmoSprites.searchAll(allScripts, allObjects, allRooms, allTimelines);
		} catch (FileNotFoundException e){
			System.err.println(" *** There aren't any Sprites resources");
		}
		
		try {
			GMOrphanedFiles gmoTimelines=new GMOrphanedFiles(TIMELINE_FOLDER, ".timeline.gmx");
			gmoTimelines.searchAll(allScripts, allObjects, allRooms, allTimelines);
		} catch (FileNotFoundException e){
			System.err.println(" *** There aren't any Timeline resources");
		}
	}

	private static ArrayList<GMFile> allFiles(String folderName, String extension){
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
	
	/* ***************************
	 * ***************************
	 * ***************************/
	
	private File assetFolder;
	private String assetFolderName;
	private String extension;
	
	/**
	 *
	 */
	public GMOrphanedFiles(String assetFolderName, String extension) throws IOException {
		this.assetFolder=new File(assetFolderName);
		this.assetFolderName=assetFolderName;
		this.extension=extension;
		if (!this.assetFolder.exists()){
			throw new FileNotFoundException(assetFolderName+" does not exist");
		}
		if (!this.assetFolder.isDirectory()){
			throw new FileNotFoundException(assetFolderName+" is not a folder");
		}
	}
	
	private void searchAll(ArrayList<GMFile> allScripts, ArrayList<GMFile> allObjects,
				ArrayList<GMFile> allRooms, ArrayList<GMFile> allTimelines){
		ArrayList<GMFile> all=allFiles(assetFolderName, extension);
		for (GMFile gmf : all){
			inUseInScripts(gmf, allScripts);
			inUseInObjects(gmf, allObjects);
			inUseInRooms(gmf, allRooms);
			inUseInTimelines(gmf, allTimelines);
			
			if (!gmf.isInUse()){
				System.out.println(gmf.getAssetName()+" isn't in use anywhere, as far as we can tell");
			}
		}
	}
	
	private boolean inUseInScripts(GMFile asset, ArrayList<GMFile> allScripts){
		String assetName=asset.getAssetName();
		
		for (GMFile script : allScripts){
			try {
				FileReader reader=new FileReader(SCRIPT_FOLDER+"\\"+script.getName());
				BufferedReader bufferedReader=new BufferedReader(reader);
				String line;
				
				while ((line=bufferedReader.readLine())!=null){
					if (line.contains(assetName)&&!line.startsWith(TRIPLE_SLASH_COMMENT)){
						asset.find();
						//System.out.println(asset.getAssetName()+" was found in the script code of "+script.getAssetName());
						return true;
					}
				}
			} catch (FileNotFoundException e){
				System.err.println("Didn't find the file: "+script.getName());
			} catch (IOException e){
				System.err.println("Something went wrong in: "+script.getName());
			}
		}
		return false;
	}
	
	private boolean inUseInObjects(GMFile asset, ArrayList<GMFile> allObjects){
		String assetName=asset.getAssetName();
		
		for (GMFile object : allObjects){
			try {
				FileReader reader=new FileReader(OBJECT_FOLDER+"\\"+object.getName());
				BufferedReader bufferedReader=new BufferedReader(reader);
				String line;
				
				boolean inCode=false;
				while ((line=bufferedReader.readLine())!=null){
					if (line.contains(XML_END_OBJECT_CODE)){
						inCode=false;
					}
					if (line.contains(XML_BEGIN_OBJECT_CODE)){
						inCode=true;
					}
					// code starts on the same line as the <script> tag does
					if (inCode){
						if (line.contains(assetName)&&!line.startsWith(TRIPLE_SLASH_COMMENT)){
							asset.find();
							return true;
						}
					}
				}
			} catch (FileNotFoundException e){
				System.err.println("Didn't find the file: "+object.getName());
			} catch (IOException e){
				System.err.println("Something went wrong in: "+object.getName());
			}
		}
		return false;
	}
	
	private boolean inUseInRooms(GMFile asset, ArrayList<GMFile> allRooms){
		String assetName=asset.getAssetName();
		
		for (GMFile room : allRooms){
			try {
				FileReader reader=new FileReader(ROOM_FOLDER+"\\"+room.getName());
				BufferedReader bufferedReader=new BufferedReader(reader);
				String line;
				
				boolean inCode=false;
				while ((line=bufferedReader.readLine())!=null){
					if (line.contains(XML_END_ROOM_CODE)){
						inCode=false;
					}
					if (line.contains(XML_BEGIN_ROOM_CODE)){
						inCode=true;
					}
					if (inCode){
						if (line.contains(assetName)&&!line.startsWith(TRIPLE_SLASH_COMMENT)){
							asset.find();
							return true;
						}
					}
					// todo: instance creation code
				}
			} catch (FileNotFoundException e){
				System.err.println("Didn't find the file: "+room.getName());
			} catch (IOException e){
				System.err.println("Something went wrong in: "+room.getName());
			}
		}
		return false;
	}
	
	private boolean inUseInTimelines(GMFile asset, ArrayList<GMFile> allTimelines){
		String assetName=asset.getAssetName();
		
		for (GMFile timeline : allTimelines){
			try {
				FileReader reader=new FileReader(ROOM_FOLDER+"\\"+timeline.getName());
				BufferedReader bufferedReader=new BufferedReader(reader);
				String line;
				
				boolean inCode=false;
				while ((line=bufferedReader.readLine())!=null){
					// Haven't done this yet
				}
			} catch (FileNotFoundException e){
				System.err.println("Didn't find the file: "+timeline.getName());
			} catch (IOException e){
				System.err.println("Something went wrong in: "+timeline.getName());
			}
		}
		return false;
	}
}