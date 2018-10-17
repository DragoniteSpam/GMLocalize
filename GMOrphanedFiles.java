import java.util.ArrayList;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileNotFoundException;

public class GMOrphanedFiles {
	public static void main(String[] args) throws IOException {
		GMOrphanedFiles gmoScripts=new GMOrphanedFiles(".\\project\\scripts", ".gml");
		gmoScripts.searchAll();
	}

	private static ArrayList<File> allFiles(String folderName, String extension){
		File folder=new File(folderName);
		ArrayList<File> list=new ArrayList<File>();
		for (File entry : folder.listFiles()){
			if (!entry.isDirectory()&&entry.getName().endsWith(extension)){
				list.add(entry);
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
	
	private void searchAll(){
		ArrayList<File> all=allFiles(assetFolderName, extension);
		for (File f : all){
			String assetName=f.getName().replace(assetFolderName, "").replace(extension, "");
			if (!inUseInScripts(assetName)){
				//System.out.println(f.getName()+" isn't in use in a script, as far as we can tell");
			}
		}
	}
	
	private boolean inUseInScripts(String assetName){
		ArrayList<File> allScripts=allFiles(".\\project\\scripts", ".gml");
		
		for (File script : allScripts){
			try {
				FileReader reader=new FileReader(script);
				BufferedReader bufferedReader=new BufferedReader(reader);
				String line;
				
				while ((line=bufferedReader.readLine())!=null){
					if (line.contains(assetName)){
						System.out.println(assetName+" is in use in this script: "+script.getName());
						break;
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