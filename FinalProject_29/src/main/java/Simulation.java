package FinalProject_29;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class Simulation {
    static PrintWriter writer;
    static char[] ops = new char[]{'+','-','*','/'};
    static int[] mutant_type;
    static String mutant_info = "mutants2.txt";
    static String SUT_files = "SUT_files";

    public static void main(String[] args) throws IOException {
        //first part
        assignment1_q4();
        //second part
        assignment2_q4();
    }

    private static void assignment2_q4() throws IOException {
        //open mutant fault list
        File mutant_list = new File(mutant_info);
        Scanner sc = new Scanner(mutant_list);
        Files.createDirectories(Paths.get(SUT_files));
        while (sc.hasNextLine()){
            String l1 = sc.nextLine();
            if(l1.equals("Number of mutants of each type:")) break;
            int linenumber = Integer.parseInt(l1.substring(5));
            char original =  sc.nextLine().charAt(9);
            char type = sc.nextLine().charAt(5);
            sc.nextLine();
            generate_mutant_file(linenumber, original, type);
        }
    }


    private static void generate_mutant_file(int targetline, char original, char type) throws FileNotFoundException{
        File text = new File("src/sample_program.java");
        Scanner scnr = new Scanner(text);
        writer = new PrintWriter(SUT_files+"/mutant_line"+targetline+"_"+get_name(original)+"_to_"+get_name(type)+".java");
        int counter = 1;
        while(scnr.hasNextLine()){
            String line = scnr.nextLine();
            if(counter == targetline){
                int insertlocation = line.indexOf(original);
                String newstr = line.substring(0, insertlocation) + type + line.substring(insertlocation+1);
                writer.println(newstr);
            }
            else{
                writer.println(line);
            }
            counter++;
        }
        writer.close();
    }

    private static String get_name(char c){
        switch (c){
            case '+':
                return "plus";
            case '-':
                return "subtract";
            case '*':
                return "multiply";
            case '/':
                return "divide";
        }
        return " ";
    }


    private static void assignment1_q4() throws FileNotFoundException{
        //creating File instance to reference text file in Java
        File text = new File("src/sample_program.java");

        //Creating Scanner instnace to read File in Java
        Scanner scnr = new Scanner(text);

        writer = new PrintWriter(mutant_info);
        mutant_type = new int[4];

        //Reading each line of file using Scanner class
        int lineNumber = 1;
        while(scnr.hasNextLine()){
            String line = scnr.nextLine();
            findMutant(lineNumber,line);
            lineNumber++;
        }
        writer.println("Number of mutants of each type:");
        for(int i=0; i<4 ; i++){
            writer.println(ops[i]+":"+ mutant_type[i]);
        }

        writer.close();
    }


    private static void findMutant(int lineNum, String line){
        for(int i=0; i<line.length(); i++){
            char c = line.charAt(i);
            if(c == '+' || c == '-' || c == '*' || c == '/') {
                for(int j=0; j<4 ; j++){
                    char op = ops[j];
                    if(op == c) continue;
                    writer.println("Line:" + lineNum);
                    writer.println("Original:" + c);
                    writer.println("Type:" + op);
                    writer.println("-----------------------");
                    mutant_type[j]++;
                }
            }
        }
    }


}

