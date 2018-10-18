import java.util.ArrayList;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileNotFoundException;

public class GMProjectFile extends GMAssetType {
	private ArrayList<String> macros;
	
	public GMProjectFile(String assetFolderName, String extension, ArrayList<GMFile> allProjectFiles) throws IOException {
		super(assetFolderName, extension);
		
		macros=findAllMacros(allProjectFiles);
	}
	
	private ArrayList<String> findAllMacros(ArrayList<GMFile> projectFiles){
		ArrayList<String> macros=new ArrayList<String>();
		
		if (projectFiles.size()>1){
			System.err.println(" *** We've detected that you have more than one project files. "+
				"We're sure you have a reason, and will assume that you want to check for unused "+
				"macros in all of them.");
		}
		
		for (GMFile gmf : projectFiles){
			try {
				FileReader reader=new FileReader(GMOrphanedFiles.ROOT_FOLDER+"\\"+gmf.getName());
				BufferedReader bufferedReader=new BufferedReader(reader);
				String line;
				
				// It would be easier to just parse the XML like a normal
				// person, wouldn't it.
				while ((line=bufferedReader.readLine())!=null){
					line=line.trim();
					if (line.startsWith("<constant name=")){
						// I am terrible.
						StringBuilder cname=new StringBuilder();
						boolean nameBuilding=false;
						for (int i=0; i<line.length(); i++){
							if (line.charAt(i)=='"'){
								nameBuilding=!nameBuilding;
								if (!nameBuilding){
									break;
								}
							} else if (nameBuilding){
								cname.append(line.charAt(i));
							}
						}
						
						String result=cname.toString();
						
						if (result.length()>0){
							macros.add(result);
						}
					}
				}
				
				bufferedReader.close();
			} catch (FileNotFoundException e){
				System.err.println("Didn't find the file: "+gmf.getName());
			} catch (IOException e){
				System.err.println("Something went wrong in: "+gmf.getName());
			}
		}
		
		return macros;
	}
	
	/*public void searchAll(ArrayList<GMFile> allScripts, ArrayList<GMFile> allObjects,
				ArrayList<GMFile> allRooms, ArrayList<GMFile> allTimelines){
		ArrayList<GMFile> all=macrosToGMFiles();
		for (GMFile gmf : all){
			inUseInScripts(gmf, allScripts);
			inUseInObjects(gmf, allObjects);
			inUseInRooms(gmf, allRooms);
			inUseInTimelines(gmf, allTimelines);
			
			if (!gmf.isInUse()){
				System.out.println(gmf.getAssetName()+" isn't in use anywhere, as far as we can tell");
			}
		}
	}*/
	
	protected ArrayList<GMFile> myFiles(){
		ArrayList<GMFile> files=new ArrayList<GMFile>();
		// These aren't files, but we treat them as such so we don't have to re-write more code.
		for (String m : macros){
			files.add(new GMFile(m, assetFolderName, extension));
		}
		
		return files;
	}
}