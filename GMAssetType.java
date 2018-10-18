import java.util.ArrayList;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileNotFoundException;

public class GMAssetType {
	protected File assetFolder;
	protected String assetFolderName;
	protected String extension;
	
	public GMAssetType(String assetFolderName, String extension) throws IOException {
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
	
	public void searchAll(ArrayList<GMFile> allScripts, ArrayList<GMFile> allObjects,
				ArrayList<GMFile> allRooms, ArrayList<GMFile> allTimelines){
		searchAll(allScripts, allObjects, allRooms, allTimelines, null);
	}
	
	public void searchAll(ArrayList<GMFile> allScripts, ArrayList<GMFile> allObjects,
				ArrayList<GMFile> allRooms, ArrayList<GMFile> allTimelines, ArrayList<String> toIgnore){
		ArrayList<GMFile> all=myFiles();
		
		for (GMFile gmf : all){
			if (toIgnore!=null){
				for (String ignore : toIgnore){
					if (gmf.getAssetName().equals(ignore)){
						return;
					}
				}
			}
			inUseInScripts(gmf, allScripts);
			inUseInObjects(gmf, allObjects);
			inUseInRooms(gmf, allRooms);
			inUseInTimelines(gmf, allTimelines);
			
			if (!gmf.isInUse()){
				System.out.println(gmf.getAssetName()+" isn't in use anywhere, as far as we can tell");
			}
		}
	}
	
	protected ArrayList<GMFile> myFiles(){
		return GMOrphanedFiles.allFiles(assetFolderName, extension);
	}
	
	protected boolean inUseInScripts(GMFile asset, ArrayList<GMFile> allScripts){
		String assetName=asset.getAssetName();
		
		for (GMFile script : allScripts){
			try {
				FileReader reader=new FileReader(GMOrphanedFiles.SCRIPT_FOLDER+"\\"+script.getName());
				BufferedReader bufferedReader=new BufferedReader(reader);
				String line;
				
				while ((line=bufferedReader.readLine())!=null){
					line=line.trim();
					if (line.contains(assetName)&&!line.startsWith(GMOrphanedFiles.TRIPLE_SLASH_COMMENT)){
						asset.find();
						//System.out.println(asset.getAssetName()+" was found in the script code of "+script.getAssetName());
						return true;
					}
				}
			
				bufferedReader.close();
			} catch (FileNotFoundException e){
				System.err.println("Didn't find the file: "+script.getName());
			} catch (IOException e){
				System.err.println("Something went wrong in: "+script.getName());
			}
		}
		return false;
	}
	
	protected boolean inUseInObjects(GMFile asset, ArrayList<GMFile> allObjects){
		String assetName=asset.getAssetName();
		
		for (GMFile object : allObjects){
			try {
				FileReader reader=new FileReader(GMOrphanedFiles.OBJECT_FOLDER+"\\"+object.getName());
				BufferedReader bufferedReader=new BufferedReader(reader);
				String line;
				
				boolean inCode=false;
				while ((line=bufferedReader.readLine())!=null){
					line=line.trim();
					if (line.contains(GMOrphanedFiles.XML_END_OBJECT_CODE)){
						inCode=false;
					}
					if (line.contains(GMOrphanedFiles.XML_BEGIN_OBJECT_CODE)&&
							!line.startsWith(GMOrphanedFiles.TRIPLE_SLASH_COMMENT)){
						inCode=true;
					}
					// code starts on the same line as the <script> tag does
					if (inCode){
						if (line.contains(assetName)&&!line.startsWith(GMOrphanedFiles.TRIPLE_SLASH_COMMENT)){
							asset.find();
							return true;
						}
					}
				}
			
				bufferedReader.close();
			} catch (FileNotFoundException e){
				System.err.println("Didn't find the file: "+object.getName());
			} catch (IOException e){
				System.err.println("Something went wrong in: "+object.getName());
			}
		}
		return false;
	}
	
	protected boolean inUseInRooms(GMFile asset, ArrayList<GMFile> allRooms){
		String assetName=asset.getAssetName();
		
		for (GMFile room : allRooms){
			try {
				FileReader reader=new FileReader(GMOrphanedFiles.ROOM_FOLDER+"\\"+room.getName());
				BufferedReader bufferedReader=new BufferedReader(reader);
				String line;
				
				boolean inCode=false;
				while ((line=bufferedReader.readLine())!=null){
					line=line.trim();
					if (line.contains(GMOrphanedFiles.XML_END_ROOM_CODE)){
						inCode=false;
					}
					if (line.contains(GMOrphanedFiles.XML_BEGIN_ROOM_CODE)){
						inCode=true;
					}
					if (inCode){
						if (line.contains(assetName)&&!line.startsWith(GMOrphanedFiles.TRIPLE_SLASH_COMMENT)){
							asset.find();
							return true;
						}
					}
					// todo: instance creation code
				}
			
				bufferedReader.close();
			} catch (FileNotFoundException e){
				System.err.println("Didn't find the file: "+room.getName());
			} catch (IOException e){
				System.err.println("Something went wrong in: "+room.getName());
			}
		}
		return false;
	}
	
	protected boolean inUseInTimelines(GMFile asset, ArrayList<GMFile> allTimelines){
		String assetName=asset.getAssetName();
		
		for (GMFile timeline : allTimelines){
			try {
				FileReader reader=new FileReader(GMOrphanedFiles.TIMELINE_FOLDER+"\\"+timeline.getName());
				BufferedReader bufferedReader=new BufferedReader(reader);
				String line;
				
				boolean inCode=false;
				while ((line=bufferedReader.readLine())!=null){
					line=line.trim();
					// Haven't done this yet
				}
			
				bufferedReader.close();
			} catch (FileNotFoundException e){
				System.err.println("Didn't find the file: "+timeline.getName());
			} catch (IOException e){
				System.err.println("Something went wrong in: "+timeline.getName());
			}
		}
		return false;
	}
}