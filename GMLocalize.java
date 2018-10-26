import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import java.io.*;
import org.json.*;

import assets.gm1.*;
/*import assets.gm2.*;*/

public class GMLocalize {
	private static final String ABSENT_ASSET="00000000-0000-0000-0000-000000000000";
    
	public static void main(String[] args) throws IOException {
        if (args.length==0){
            System.out.println("Incorrect use: use ");
            System.out.println("    java GMLOcalize <project1name> <project2name> .. <projectNname>");
            System.out.println("to use this tool.");
            System.exit(0);
        }
        
        for (String projectName : args){
            System.out.println("-----------------------------");
            System.out.println(projectName);
            System.out.println("-----------------------------");
            
            GM1Project rootProject1=GM1Project.autoDetect(projectName);
			//GM2Project rootProject2=GM2Project.autoDetect(projectName);
            
            if (rootProject1==null/*&&rootProject2==null*/){
                System.err.println("We couldn't find a Game Maker Studio (1 or 2) project file in the specified folder. "+
                    "Are you sure you're checking the right one?");
            } else {
				if (rootProject1!=null){
					assesGM1Project(projectName, rootProject1);
				}
				
				/*if (rootProject2!=null){
					assesGM2Project(projectName, rootProject2);
				}*/
			}
        }
        
        System.out.println("(Hit Enter to quit.)");
        new Scanner(System.in).nextLine();
	}
	
	/*public static void assesGM2Project(String directory, GM2Project rootProject){
        ArrayList<String> code=new ArrayList<String>();
        ArrayList<String> localizationStrings=new ArrayList<String>();
    }*/
    
    public static void assesGM1Project(String directory, GM1Project rootProject){
        ArrayList<String> code=new ArrayList<String>();
        ArrayList<String> localizationStrings=new ArrayList<String>();
        
        /*
         * Backgrounds, Configs, Fonts, Paths, Shaders and Sprites have no
		 * Game Maker code in them. (You don't localize shader code.)
         */
        
        /*
         * Objects
         */
        for (GM1Object object : GM1Object.allFiles(directory)){
            code.add(object.code());
        }
        
        /*
         * Rooms
         */
        for (GM1Room room : GM1Room.allFiles(directory)){
            code.add(room.creationCode());
        }
        
        /*
         * Scripts
         */
        for (GM1Script script : GM1Script.allFiles(directory)){
            code.add(script.getPlainText());
        }
        
        /*
         * Timelines
         */
        for (GM1Timeline timeline : GM1Timeline.allFiles(directory)){
            code.add(timeline.code());
        }
        
        /*
         * Macros
		 * I have no idea why you'd put a localized string in a macro but let's
		 * leave as few stones unturned as possible
         */
        
        code.addAll(rootProject.allMacroCode());
        
        System.out.println("All code:");
        for (String codeString : code){
			System.out.println("\t"+codeString);
        }
    }
}