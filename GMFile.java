import java.io.File;

public class GMFile extends File {
	
	private boolean inUse;
	private String assetName;
	
	public GMFile(String name, String assetFolderName, String extension){
		super(name);
		this.assetName=name.replace(assetFolderName, "").replace(extension, "");
		this.inUse=false;
	}
	
	public GMFile(File original, String assetFolderName, String extension){
		this(original.getName(), assetFolderName, extension);
	}
	
	public boolean isInUse(){
		return this.inUse;
	}
	
	public void find(){
		this.inUse=true;
	}
	
	public String getAssetName(){
		return this.assetName;
	}
	
	/*public GMFile[] listFiles(){
		File[] original=super.listFiles();
		GMFile[] wrapperFiles=new GMFile[original.length];
		for (int i=0; i<original.length; i++){
			wrapperFiles[i]=new GMFile(original[i]);
		}
		
		return wrapperFiles;
	}*/
}