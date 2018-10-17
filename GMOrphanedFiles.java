import java.util.ArrayList;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileNotFoundException;

public class GMOrphanedFiles {
	private static final String SCRIPT_FOLDER=".\\project\\scripts";
	private static final String OBJECT_FOLDER=".\\project\\objects";
	
	private static final String TRIPLE_SLASH_COMMENT="///";
	private static final String XML_BEGIN_CODE="<string>";
	private static final String XML_END_CODE="</string>";
	
	public static void main(String[] args) throws IOException {
		ArrayList<GMFile> allScripts=allFiles(SCRIPT_FOLDER, ".gml");
		ArrayList<GMFile> allObjects=allFiles(OBJECT_FOLDER, ".object.gmx");
		
		GMOrphanedFiles gmoScripts=new GMOrphanedFiles(SCRIPT_FOLDER, ".gml");
		gmoScripts.searchAll(allScripts, allObjects);
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
	
	private void searchAll(ArrayList<GMFile> allScripts, ArrayList<GMFile> allObjects){
		ArrayList<GMFile> all=allFiles(assetFolderName, extension);
		for (GMFile gmf : all){
			inUseInScripts(gmf, allScripts);
			inUseInObjects(gmf, allObjects);
			
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
					if (line.contains(XML_END_CODE)){
						inCode=false;
					}
					if (line.contains(XML_BEGIN_CODE)){
						inCode=true;
					}
					// code starts on the same line as the <script> tag does
					if (inCode){
						//System.out.println(line);
						if (line.contains(assetName)&&!line.startsWith(TRIPLE_SLASH_COMMENT)){
							asset.find();
							//System.out.println(asset.getAssetName()+" was found in the object code of "+object.getAssetName());
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
}