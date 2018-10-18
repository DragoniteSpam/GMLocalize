import java.util.ArrayList;

import java.io.*;
import org.w3c.dom.*;
import javax.xml.parsers.*;

import assets.gm1.*;

public class GMOrphanedFiles {
	public static final String ROOT_FOLDER=".\\project";
	
	public static final String BACKGROUND_FOLDER=".\\project\\background";
	public static final String FONT_FOLDER=".\\project\\fonts";
	public static final String OBJECT_FOLDER=".\\project\\objects";
	public static final String PATH_FOLDER=".\\project\\paths";
	public static final String ROOM_FOLDER=".\\project\\rooms";
	public static final String SCRIPT_FOLDER=".\\project\\scripts";
    public static final String SHADER_FOLDER=".\\project\\shaders";
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
        if (args.length==0){
            System.out.println("Incorrect use: use ");
            System.out.println("    java GMOrphanedFiles <project1name> <project2name> .. <projectNname>");
            System.out.println("to use this tool.");
            System.exit(0);
        }
        
        for (String projectName : args){
            /*ArrayList<GMFile> allProjects=allFiles(ROOT_FOLDER, ".project.gmx");
            
            ArrayList<GMFile> allScripts=allFiles(SCRIPT_FOLDER, ".gml");
            ArrayList<GMFile> allObjects=allFiles(OBJECT_FOLDER, ".object.gmx");
            ArrayList<GMFile> allRooms=allFiles(ROOM_FOLDER, ".room.gmx");
            ArrayList<GMFile> allTimelines=allFiles(TIMELINE_FOLDER, ".timeline.gmx");*/
            
            //allProjectCode();
            
            GM1Project rootProject1=GM1Project.autoDetect(projectName);
            
            if (rootProject1==null/*&&rootProject2==null*/){
                System.err.println("Couldn't find a Game Maker Studio (1 or 2) project file in the specified folder. "+
                    "Are you sure you're checking the right one?");
                System.exit(0);
            }
            
            if (rootProject1!=null){
                assesGM1Project(projectName, rootProject1);
            }
        }
	}
    
    public static void assesGM1Project(String directory, GM1Project rootProject){
        String startingRoomName=rootProject.startingRoom();
        String code=null;
        StringBuilder codeBuilder=new StringBuilder();
        
        ArrayList<GM1Room> rooms=GM1Room.allFiles(directory);
        for (GM1Room room : rooms){
            codeBuilder.append(room.creationCode());
            ArrayList<String> roomInstances=room.allInstances();
            ArrayList<String> roomBackgrounds=room.allBackgrounds();
        }
        
        code=codeBuilder.toString();
    }
}