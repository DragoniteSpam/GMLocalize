import java.util.ArrayList;
import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;

public class GMOrphanedFiles {
	public static void main(String[] args) throws IOException {
		GMOrphanedFiles gmoScripts=new GMOrphanedFiles(".\\project\\scripts");
		GMOrphanedFiles gmoObjects=new GMOrphanedFiles(".\\project\\objects");
		GMOrphanedFiles gmoSprites=new GMOrphanedFiles(".\\project\\sprites");
		for (File f : gmoScripts.allFiles()){
			System.out.println(f);
		}
		for (File f : gmoObjects.allFiles()){
			System.out.println(f);
		}
		for (File f : gmoSprites.allFiles()){
			System.out.println(f);
		}
	}
	
	/* ***************************
	 * ***************************
	 * ***************************/
	
	private File assetFolder;
	
	/**
	 *
	 */
	public GMOrphanedFiles(String assetFolderName) throws IOException {
		this.assetFolder=new File(assetFolderName);
		if (!this.assetFolder.exists()){
			throw new FileNotFoundException(assetFolderName+" does not exist");
		}
		if (!this.assetFolder.isDirectory()){
			throw new FileNotFoundException(assetFolderName+" is not a folder");
		}
	}
	
	private ArrayList<File> allFiles(){
		ArrayList<File> list=new ArrayList<File>();
		for (File entry : assetFolder.listFiles()){
			if (!entry.isDirectory()){
				list.add(entry);
			}
		}
		return list;
	}
}