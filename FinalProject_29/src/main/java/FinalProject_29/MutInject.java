package FinalProject_29;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MutInject {
//Ter Renata
// 260804224
	
	
public static void main(String[] args) throws IOException {
	
	// create a copy of a file
	
	ProcessBuilder processBuilder = new ProcessBuilder();

    processBuilder.command("cmd.exe", "/c", "bash", "mutInjection.sh");
    processBuilder.directory(new File("C:\\Users\\Renata\\eclipse-workspace\\FinalProject_29\\src\\main\\java\\FinalProject_29"));

    // can also run the java file like this
    // processBuilder.command("java", "Hello");

    try {

        Process process = processBuilder.start();

        BufferedReader reader =
                new BufferedReader(new InputStreamReader(process.getInputStream()));

        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }

        int exitCode = process.waitFor();
        System.out.println("\nExited with error code : " + exitCode);

    } catch (IOException e) {
        e.printStackTrace();
    } catch (InterruptedException e) {
        e.printStackTrace();
    }


//	String path = "C:\\\\Users\\\\Renata\\\\eclipse-workspace\\\\FinalProject_29\\\\src\\\\main\\\\java\\\\FinalProject_29";
//	Process p = new ProcessBuilder("cmd.exe", "/c",path+"/mutInjection.sh").start();
//	 try {
//		p.waitFor();
//	} catch (InterruptedException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	}
//	Runtime.getRuntime().exec("C:\\Users\\Renata\\eclipse-workspace\\FinalProject_29\\src\\main\\java\\FinalProject_29/mutInjection.sh");
//	Process p = Runtime.getRuntime().exec("bin/bash C:\\\\Users\\\\Renata\\\\eclipse-workspace\\\\FinalProject_29\\\\src\\\\main\\\\java\\\\FinalProject_29/mutInjection.sh");
//    try {
//		p.waitFor();
//	} catch (InterruptedException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	}
//	 
//	String textLine, originalLine, mut1, mut2, mut3 ;
//	try {
//		// open and read mutant file 
//		// Please change file path here
//		File mutFile = new File("C:\\\\Users\\\\Renata\\\\eclipse-workspace\\\\FinalProject_29\\\\src\\\\main\\\\java\\\\FinalProject_29/mutants.txt");
//		FileReader mutR = new FileReader(mutFile);
//		BufferedReader bufR = new BufferedReader(mutR);
//		
//		
//      
//        while((textLine = bufR.readLine())!= null) {
//        	
//			// if encounter original -> spot in SUT, then replace to a mutant
//			if (textLine.contains("Original")) {
//				originalLine = bufR.readLine(); // original line in DummyMathClass
//				textLine = bufR.readLine(); 
//				
//				mut1 = bufR.readLine(); // first mutant in mutants.txt  
//				mut2 = bufR.readLine(); // second
//				mut3 = bufR.readLine(); // third
//				
//				  // input the (modified) file content to the StringBuffer "input"
//		        BufferedReader file = new BufferedReader(new FileReader("C:\\\\Users\\\\Renata\\\\eclipse-workspace\\\\FinalProject_29\\\\src\\\\main\\\\java\\\\FinalProject_29/SUT.java"));
//		        StringBuffer inputBuffer = new StringBuffer();
//		        String line;
//
//				//iterate 3 times to pass through each mutant
//				for (int i = 0; i < 3; i++) {
////					System.out.println("Mutant ");
//			        while ((line = file.readLine()) != null) {
//			        	
//			        	//replace the line a.k.a mutant injection  
//			        	if (line.contains(originalLine)) {
//			        		 line = mut1;
//			        	}if(line.contains(mut1)) {
//			        		line = mut2;
//			        	}if (line.contains(mut2)) {
//			        		line = mut3;
//			        	}
////			            System.out.println(line);	
//			            inputBuffer.append(line);
//			            inputBuffer.append('\n');
//
//			        }
//				}
//	            file.close();
//				  // write the new string with the replaced line OVER the same file
//		        FileOutputStream fileOut = new FileOutputStream("C:\\Users\\Renata\\eclipse-workspace\\A1-29\\src\\SUT.java");
//		        fileOut.write(inputBuffer.toString().getBytes());
//		        fileOut.close();
//
//			}
//        }
//
////        file.close();
//        
//        //close mutation text file 
//		mutR.close();
//		bufR.close();
//
//      
//    } catch (Exception e) {
//        System.out.println("Problem reading file.");
//    }
//		
	}

}
