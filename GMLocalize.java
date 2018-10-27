import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import java.io.*;
import org.json.*;

import assets.gm1.*;
/*import assets.gm2.*;*/

public class GMLocalize {
	private static final String ABSENT_ASSET="00000000-0000-0000-0000-000000000000";
    
	public static void main(String[] args) throws IOException {
        if (args.length==0){
            System.out.println("Incorrect use: use");
            System.out.println("\tjava GMLocalize <[-project1existing]> <project1name> <[-project2existing]> <project2name> .. <[-projectNexisting]> <projectNname>");
            System.out.println("Example:");
            System.out.println("\tjava GMLocalize Pong.gmx SpaceInvaders.gmx");
            System.out.println("\tjava GMLocalize -pongstrings.txt Pong.gmx SpaceInvaders.gmx");
            System.out.println("to use this tool.");
            System.out.println("(There should probably not be any text in either of those games though.)");
            System.exit(0);
        }
        
        for (int i=0; i<args.length; i++){
            String projectName=args[i];
            if (projectName.charAt(0)=='-'){
                continue;
            }
            System.out.println("-----------------------------");
            System.out.println(projectName);
            System.out.println("-----------------------------");
            
            GM1Project rootProject1=GM1Project.autoDetect(projectName);
			//GM2Project rootProject2=GM2Project.autoDetect(projectName);
            
            if (rootProject1==null/*&&rootProject2==null*/){
                System.err.println("We couldn't find a Game Maker Studio (1 or 2) project file in the specified folder. "+
                    "Are you sure you're checking the right one?");
            } else {
                String existingFileName;
                if (i>0&&args[i-1].charAt(0)=='-'){
                    existingFileName=args[i-1].substring(1, args[i-1].length());
                } else {
                    existingFileName=null;
                }
                
				if (rootProject1!=null){
					assesGM1Project(projectName, rootProject1, existingFileName);
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
        System.out.println("\t5. The program stops reading localization text when it finds a "+
            "closing quotation mark (either kind), so if you try to combine strings inside the "+
            "L() script, like L(\"%0 fish, %1 fish, \"+\"%2 fish, %3 fish\", 1, 2, \"red\", "+
            "\"blue\"), the text that it finds will only be \"%0 fish, %1 fish, \".");
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
    
    public static void assesGM1Project(String directory, GM1Project rootProject, String existingFileName){
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
        String additionFileName=rootProject.getAssetName()+"-additions.txt";
		write(sort(code, false), outputFileName, existingFileName, additionFileName);
		
		System.out.println("Probable localization strings written to "+outputFileName+".");
        if (existingFileName!=null){
            System.out.println("Strings that were found that were not in the original file were "+
                "written to "+additionFileName+".");
        }
    }
	
	private static ArrayList<String> sort(ArrayList<String> code, boolean escapeQuotes){
		HashSet<String> lstrings=new HashSet<String>();
		ArrayList<String> output=new ArrayList<String>();
		
		final String signal="L(";
		
		for (String codeString : code){
			int index=codeString.indexOf(signal);
			while (index>=0){
                char c2=codeString.charAt(index+2);
                if (c2=='\"'||c2=='\''){
                    char quoteCharacter=c2;
                    StringBuilder builder=new StringBuilder();
                    for (int i=index+3; i<codeString.length(); i++){
                        char c=codeString.charAt(i);
                        char cp=codeString.charAt(i-1);
                        if (c==quoteCharacter&&(!escapeQuotes||cp!='\\')){
                            String result=builder.toString();
                            if (!lstrings.contains(result)){
                                lstrings.add(result);
                            }
                            break;
                        } else {
                            builder.append(c);
                        }
                    }
                }
				index=codeString.indexOf(signal, index+1);
			}
		}
		
		for (String key : lstrings){
			output.add(key);
		}
		
		output.sort(String::compareToIgnoreCase);
		return output;
	}
	
	private static void write(ArrayList<String> strings, String outputFileName, String existingFileName, String additionFileName){
        HashMap<String, HashMap<String, String>> existing=readExisting(existingFileName);
        final String DEF="[default]";
        
        Set<String> codesInUse;
        boolean hasDefault=false;
        
        if (existing==null){
            codesInUse=null;
        } else {
            codesInUse=new HashSet(existing.keySet());
            if (codesInUse.contains(DEF)){
                codesInUse.remove(DEF);
                hasDefault=true;
            }
        }
        
		try {
			PrintWriter printer=new PrintWriter(new FileWriter(outputFileName));
            PrintWriter printerAddition=new PrintWriter(new FileWriter(additionFileName));
            for (String line : strings){
                printer.print(DEF+line+"\r\n");
                if (codesInUse!=null){
                    for (String code : codesInUse){
                        HashMap<String, String> langStrings=existing.get(code);
                        if (langStrings.containsKey(line)){
                            printer.print(code+langStrings.get(line)+"\r\n");
                        } else {
                            printer.print(code+"\r\n");
                        }
                    }
                    if (hasDefault){
                        HashMap<String, String> defaultStrings=existing.get(DEF);
                        if (!defaultStrings.containsKey(line)){
                            printerAddition.print(line+"\r\n");
                        }
                    }
                }
			}
			printer.close();
            printerAddition.close();
        } catch (IOException e){
            System.err.println("Something went wrong in: "+outputFileName);
        }
	}
    
    private static HashMap<String, HashMap<String, String>> readExisting(String existingFileName){
        if (existingFileName==null){
            return null;
        }
        
        HashMap<String, HashMap<String, String>> values=new HashMap<String, HashMap<String, String>>();
        final String DEF="[default]";
        
        try {
            BufferedReader bufferedReader=new BufferedReader(new FileReader(new File(existingFileName)));
            String line, code, contents, original="";
            
            while ((line=bufferedReader.readLine())!=null){
                // comments are permitted, but will be scrubbed out of the updated version
                if (line.charAt(0)!='#'){
                    // this will behave unpredictably if you feed it a badly-formatted file
                    int stopPoint=line.indexOf("]");
                    if (stopPoint>-1){
                        code=line.substring(0, stopPoint+1);
                        contents=line.replace(code, "");
                        if (code.equals(DEF)){
                            original=contents;
                        }
                        HashMap<String, String> langStrings;
                        if (values.containsKey(code)){
                            langStrings=values.get(code);
                        } else {
                            langStrings=new HashMap<String, String>();
                            values.put(code, langStrings);
                        }
                        if (!langStrings.containsKey(original)){
                            langStrings.put(original, contents);
                        }
                    }
                }
            }
        
            bufferedReader.close();
        } catch (FileNotFoundException e){
            System.err.println("Didn't find the file: "+existingFileName);
        } catch (IOException e){
            System.err.println("Something went wrong in: "+existingFileName);
        }
        
        return values;
    }
}