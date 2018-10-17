import java.util.ArrayList;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileNotFoundException;

public class GMOrphanedFiles {
	private static final String SCRIPT_FOLDER=".\\project\\scripts";
	
	public static void main(String[] args) throws IOException {
		ArrayList<GMFile> allScripts=allFiles(SCRIPT_FOLDER, ".gml");
		
		GMOrphanedFiles gmoScripts=new GMOrphanedFiles(SCRIPT_FOLDER, ".gml");
		gmoScripts.searchAll(allScripts);
	}

	private static ArrayList<GMFile> allFiles(String folderName, String extension){
		File folder=new File(folderName);
		ArrayList<GMFile> list=new ArrayList<GMFile>();
		for (File entry : folder.listFiles()){
			if (!entry.isDirectory()&&entry.getName().endsWith(extension)){
				list.add(new GMFile(entry, folderName, extension));
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
	
	private void searchAll(ArrayList<GMFile> allScripts){
		ArrayList<GMFile> all=allFiles(assetFolderName, extension);
		for (GMFile gmf : all){
			if (!inUseInScripts(gmf, allScripts)){
				System.out.println(gmf.getName()+" isn't in use in a script, as far as we can tell");
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
					if (line.contains(assetName)){
						script.find();
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
}