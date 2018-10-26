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
		
		System.out.println();
		System.out.println("-----------------------------");
		System.out.println();
		
		System.out.println("This program currently only searches for sections in code "+
			"that look like the signal string (\"L(\"), since that's what the script "+
			"that fetches localized text looks like. Of course, that means there are a "+
			"few ways to fool it.");
		System.out.println("\t1. Commented-out localization code will still be caught "+
			"and reported.");
		System.out.println("\t2. \"Localization code\" in strings themselves will still "+
			"be reported.");
		System.out.println("\t3. Script names that end with a capital L followed immediately "+
			"by a parameter list, i.e. \"FALL();\" will be reported (and will likely produce "+
			"unexpected behavior unless the first argument is a string).");
		System.out.println("\t4. If you put a space between the capital L and the parenthesis, "+
			"it will NOT be reported, as this program checks for exact matches of the \"signal\" "+
			"string.");
		System.out.println("I may come back to this and do proper analysis of code some day "+
			"instead of just searching for substrings, but for the time being I'm the only "+
			"person who's going to use this, and my code is formatted in such a way that it's "+
			"unlikely going to break the program.");
        
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
		
		/*
		 * Search for substrings in the code that begin with "L(" and make a list
		 * of the strings that follow it
		 */
		String outputFileName=rootProject.getAssetName()+"-output.txt";
		write(sort(code, false), outputFileName);
		
		System.out.println("Probable localization strings written to "+outputFileName+".");
    }
	
	private static ArrayList<String> sort(ArrayList<String> code, boolean escapeQuotes){
		HashMap<String, String> lstrings=new HashMap<String, String>();
		ArrayList<String> output=new ArrayList<String>();
		
		final String signal="L(";
		
		for (String codeString : code){
			int index=codeString.indexOf(signal);
			while (index>=0){
				boolean building=false;
				char quoteCharacter='"';
				StringBuilder builder=new StringBuilder();
				for (int i=index; i<codeString.length(); i++){
					char c=codeString.charAt(i);
					if (c=='"'||c=='\''){
						if (building&&c==quoteCharacter){
							String result=builder.toString();
							if (!lstrings.containsKey(result)){
								lstrings.put(result, result);
							}
							break;
						} else {
							building=true;
							quoteCharacter=c;
						}
					} else if (building){
						builder.append(c);
					}
				}
				index=codeString.indexOf(signal+1);
			}
		}
		
		for (String key : lstrings.keySet()){
			output.add(key);
		}
		
		output.sort(String::compareToIgnoreCase);
		return output;
	}
	
	private static void write(ArrayList<String> strings, String outputFileName){
		try {
			PrintWriter printer=new PrintWriter(new FileWriter(outputFileName));
            for (String line : strings){
				printer.print(line+"\r\n");
			}
			printer.close();
        } catch (IOException e){
            System.err.println("Something went wrong in: "+outputFileName);
        }
	}
}