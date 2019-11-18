

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileWriter;  
//import java.io.*; 

public class MutTestingPIT {
	
	static String pathSourceDir = "/Users/shiqiaozhu/eclipse-workspace/FinalProject_29/src/main/java/FinalProject_29/" ;

	public static void main(String[] args) throws IOException {
		FileWriter fw=new FileWriter(pathSourceDir+"mutants.txt");   
		BufferedReader reader;
		int lineNumber = 0;
		char[] mathOper = {'+','-','*','/'};
		int[] mutantPerType = new int [4];
		try {
			reader = new BufferedReader(new FileReader(
					//Please change the directory of your test file here
					pathSourceDir+"MathDummy.java"));
			String line = reader.readLine();
			while (line != null) {
				for(int i = 0; i < mathOper.length; i++) {
					int counter = line.indexOf(mathOper[i]);  
					if(counter != -1) {
//						fw.write("Original at line "+lineNumber+"\n"+mathOper[i]+" "+line+"\nMutants:\n"); 
						fw.write("Original at line "+lineNumber+"\n"+line+"\nMutants:\n");
						StringBuilder myName = new StringBuilder(line);
						for(int j = 0; j < mathOper.length; j++) {
							if(j!=i) {
								myName.setCharAt(counter, mathOper[j]);
//								fw.write(mathOper[j]+" "+myName+"\n");s
								fw.write(myName+"\n");
								mutantPerType[j]++;
							}
						}	
//						fw.write("\n");
					}
				}
				// read next line
				lineNumber++;
				line = reader.readLine();
			}
			fw.write("\n\n");
			for(int i = 0; i < mathOper.length; i++) {
				fw.write("Total mutants for "+mathOper[i]+" is "+mutantPerType[i]+"\n");
			}
			reader.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		fw.close();   
	}

}
