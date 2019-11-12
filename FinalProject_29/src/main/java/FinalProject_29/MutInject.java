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
	
	String pathToSourceFiles = "C:\\\\Users\\\\Renata\\\\eclipse-workspace\\\\FinalProject_29\\\\src\\\\main\\\\java\\\\FinalProject_29\\\\" ;
	String pathToPackage = "C:\\\\Users\\\\Renata\\\\eclipse-workspace\\\\FinalProject_29/";
	// create a copy of a file
	
	ProcessBuilder processBuilder = new ProcessBuilder();

    processBuilder.command("cmd.exe", "/c", "bash", "mutInjection.sh");
    processBuilder.directory(new File(pathToSourceFiles));



    try {

        Process process = processBuilder.start();
        int exitCode = process.waitFor();

    } catch (IOException e) {
        e.printStackTrace();
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
    
    //create new processor to compile new class and run pitest
    ProcessBuilder processCompile = new ProcessBuilder();
    ProcessBuilder processPitest = new ProcessBuilder();
    //compile 
    //windows
    processCompile.command("cmd.exe", "/c", "javac", "SUT.java");
    //linux
//    processCompile.command("bash", "-c", "javac", "SUT.java");
    processCompile.directory(new File(pathToSourceFiles));
    
    //run pitest
    //windows
    processPitest.command("cmd.exe", "/c", "gradle", "build","pitest");
    //linux
//    processPitest.command("bash", "-c", "gradle", "build","pitest");
    processPitest.directory(new File(pathToPackage));

    


	String textLine, originalLine, mut1, mut2, mut3 ;
	try {
		// open and read mutant file 
		// Please change file path here
		File mutFile = new File(pathToSourceFiles+"mutants.txt");
		FileReader mutR = new FileReader(mutFile);
		BufferedReader bufR = new BufferedReader(mutR);
		
		
      
        while((textLine = bufR.readLine())!= null) {
        	
			// if encounter original -> spot in SUT, then replace to a mutant
			if (textLine.contains("Original")) {
				originalLine = bufR.readLine(); // original line in DummyMathClass
				textLine = bufR.readLine(); 
				
				mut1 = bufR.readLine(); // first mutant in mutants.txt  
				mut2 = bufR.readLine(); // second
				mut3 = bufR.readLine(); // third
				
				//iterate 3 times to pass through each mutant
				for (int i = 0; i < 3; i++) {
				  // input the (modified) file content to the StringBuffer "input"
		        BufferedReader file = new BufferedReader(new FileReader(pathToSourceFiles+"SUT.java"));
		        StringBuffer inputBuffer = new StringBuffer();
		        String line;

				
//					System.out.println("Mutant ");
			        while ((line = file.readLine()) != null) {
			        	
			        	//replace the line a.k.a mutant injection  
			        	if (line.contains(originalLine)) {
			        		 line = mut1;
			        	}else 	if(line.contains(mut1)) {
			        		line = mut2;
			        	}else if (line.contains(mut2)) {
			        		line = mut3;
			        	}
			            inputBuffer.append(line);
			            inputBuffer.append('\n');

			        }
				
	            file.close();
				  // write the new string with the replaced line OVER the same file
		        FileOutputStream fileOut = new FileOutputStream(pathToSourceFiles+"SUT.java");
		        fileOut.write(inputBuffer.toString().getBytes());
		        fileOut.close();
		        
		        //actually run the pitest
		        try {
		            Process pComp = processCompile.start();
//		            int exit = pComp.waitFor();
		            
		            Process pPitest = processPitest.start();
		            int ex = pPitest.waitFor();

		        } catch (IOException e) {
		            e.printStackTrace();
		        } catch (InterruptedException e) {
		            e.printStackTrace();
		        }
				}
			}
        }

//        file.close();
        
        //close mutation text file 
		mutR.close();
		bufR.close();

      
    } catch (Exception e) {
        System.out.println("Problem reading file.");
    }
		
	}

}
