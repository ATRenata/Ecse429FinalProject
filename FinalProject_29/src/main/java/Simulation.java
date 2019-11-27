import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyPair;
import java.util.*;
import java.util.UUID;

public class Simulation{
    static PrintWriter writer;
    static char[] ops = new char[]{'+','-','*','/'};
    static int[] mutant_type;
    static String mutant_info = "src/main/java/mutants2.txt";
    static String SUT_files = "src/main/java/SUT_files";
    static String my_program = "src/main/java/sample_program.java";

    static int[][] input_vectors = new int[][]{{0,4},{100,5},{42,67}};

    public static void main(String[] args) throws Exception {
        //first part
        System.out.println("Creating mutant list...");
        assignment1_q4();
        //second part
        System.out.println("Generating SUTs...");
        assignment2_q4();

        Map<String,String> killedMutantIDs;
        List<String> result = new ArrayList<>();
        final File folder = new File(SUT_files);
        search(".*\\.java", folder, result);
        Files.createDirectories(Paths.get("src/main/java/Updated_mutants"));

        System.out.println("Start sequential simulation...");
        long seq_start = System.currentTimeMillis();
        for(int ctr = 0; ctr < input_vectors.length; ctr++){
            System.out.println("Start testing input vector ["+input_vectors[ctr][0]+","+input_vectors[ctr][1]+"]");
            ReturnValues r = Helper.runProcess("sampleprogram", "java "+my_program+" "+input_vectors[ctr][0]+" "+input_vectors[ctr][1]);
            //sequential
            killedMutantIDs = new HashMap<>();
            for (String s : result) {
                File text = new File(s);
                Scanner scnr = new Scanner(text);
                String ID = scnr.nextLine().substring(2);
                ReturnValues mutant_return =  Helper.runProcess(ID,"java "+s+" "+input_vectors[ctr][0]+" "+input_vectors[ctr][1]);
                //System.out.println(r.output+ " "+ mutant_return.output);
                if(r.output.equals(mutant_return.output)){
                        //&& r.error_message != null && r.error_message.equals(mutant_return.error_message)
                        //&& r.exit_value == mutant_return.exit_value){
                    System.out.println("survive mutant "+mutant_return.id);
                }
                else{
                    System.out.println("kill mutant "+mutant_return.id);
                    killedMutantIDs.put(mutant_return.id,"killed by ["+input_vectors[ctr][0]+","+input_vectors[ctr][1]+"]");
                }
            }
            updated_mutant_file(killedMutantIDs,"mutantlist_sequential_input_"+input_vectors[ctr][0]+"_"+input_vectors[ctr][1]+".txt");

        }
        System.out.println("Sequential simulation takes "+(System.currentTimeMillis()-seq_start)+" ms");






        System.out.println("Start parallel simulation...");
        long par_start = System.currentTimeMillis();
        ArrayList<ArrayList<String>> files = new ArrayList<>(3);
        for (int i=0; i<3; i++)
        {
            files.add(new ArrayList<>());
        }
        int filecounter = 0;
        for (String s : result) {
            files.get(filecounter).add(s);
            filecounter++;
            filecounter = filecounter%3;
        }

        for(int ctr = 0; ctr < input_vectors.length; ctr++){
            System.out.println("Start testing input vector ["+input_vectors[ctr][0]+","+input_vectors[ctr][1]+"]");
            ReturnValues r = Helper.runProcess("sampleprogram", "java "+my_program+" "+input_vectors[ctr][0]+" "+input_vectors[ctr][1]);
            MyThread[] threads = new MyThread[3];
            for (int i=0; i<3; i++)
            {
                threads[i]= new MyThread(files.get(i), r, input_vectors[ctr][0], input_vectors[ctr][1]);
                threads[i].start();
            }
            killedMutantIDs = new HashMap<>();
            for (int i=0; i<3; i++){
                threads[i].join();
                Map<String, String> temp = threads[i].getKilledIDs();
                for(Map.Entry<String,String> entry: temp.entrySet()){
                    killedMutantIDs.put(entry.getKey(),entry.getValue());
                }
            }
            updated_mutant_file(killedMutantIDs,"mutantlist_parallel_input_"+input_vectors[ctr][0]+"_"+input_vectors[ctr][1]+".txt");

        }
        System.out.println("Parallel simulation takes "+(System.currentTimeMillis()-par_start)+" ms");


    }


    private static void updated_mutant_file(Map<String, String> killedMutantIDs, String filename) throws IOException {
        File mutant_list = new File(mutant_info);
        Scanner sc = new Scanner(mutant_list);
        writer = new PrintWriter("src/main/java/Updated_mutants/"+filename);
        while (sc.hasNextLine()){
            String l1 = sc.nextLine();
            if(!l1.substring(0,3).equals("ID:")) {
                writer.println(l1);
                continue;
            }
            String id = l1.substring(3);
            if(killedMutantIDs.containsKey(id)) {
                writer.println(l1 + "("+killedMutantIDs.get(id)+")");
            }
            else{
                writer.println(l1 + " (survived)");
            }

        }
        writer.close();
    }

    public static void search(final String pattern, final File folder, List<String> result) {
        for (final File f : folder.listFiles()) {

            if (f.isDirectory()) {
                search(pattern, f, result);
            }

            if (f.isFile()) {
                if (f.getName().matches(pattern)) {
                    result.add(f.getAbsolutePath());
                }
            }

        }
    }

    private static void assignment2_q4() throws IOException {
        //open mutant fault list
        File mutant_list = new File(mutant_info);
        Scanner sc = new Scanner(mutant_list);
        Files.createDirectories(Paths.get(SUT_files));
        while (sc.hasNextLine()){
            String l1 = sc.nextLine();
            if(l1.equals("Number of mutants of each type:")) break;
            String id = l1.substring(3);
            int linenumber = Integer.parseInt(sc.nextLine().substring(5));
            char original =  sc.nextLine().charAt(9);
            char type = sc.nextLine().charAt(5);
            sc.nextLine();
            generate_mutant_file(id,linenumber, original, type);
        }
    }

    private static void generate_mutant_file(String id,int targetline, char original, char type) throws FileNotFoundException{
        File text = new File(my_program);
        Scanner scnr = new Scanner(text);
        String newJavaFileName = "mutant_line"+targetline+"_"+get_name(original)+"_to_"+get_name(type);
        writer = new PrintWriter(SUT_files+"/"+newJavaFileName+".java");
        writer.println("//"+id);
        writer.println("package SUT_files;");
        int counter = 1;
        while(scnr.hasNextLine()){
            String line = scnr.nextLine();
            String newstr = line;
            if(counter == targetline) {
                int insertlocation = newstr.indexOf(original);
                newstr = newstr.substring(0, insertlocation) + type + newstr.substring(insertlocation + 1);
            }
            if(line.contains("sample_program")){
                int startindex = newstr.indexOf("sample_program");
                newstr = newstr.substring(0, startindex) + newJavaFileName + newstr.substring(startindex+"sample_program".length());
            }
            writer.println(newstr);
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
        File text = new File(my_program);

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
                    writer.println("ID:"+UUID.randomUUID());
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



class Helper{
    public static ReturnValues runProcess(String id,String command) throws Exception {
        Process pro = Runtime.getRuntime().exec(command);
        String out = getData( pro.getInputStream());
        String error = getData(pro.getErrorStream());
        pro.waitFor();
        int exitvalue = pro.exitValue();
        return new ReturnValues(id, out, error, exitvalue);
        //System.out.println("Thread "+threadid+" output:"+out+",error:"+error+",exitvalue:"+exitvalue);
    }

    public static String getData(InputStream ins) throws Exception {
        BufferedReader in = new BufferedReader(new InputStreamReader(ins));
        String line = in.readLine();
        return line;
    }

}

class ReturnValues{
    String id;
    String output;
    String error_message;
    int exit_value;
    ReturnValues(String id, String output, String error_message, int exit_value){
        this.id = id;
        this.output = output;
        this.error_message = error_message;
        this.exit_value = exit_value;
    }
}

class MyThread extends Thread{

    ReturnValues original_return;
    ArrayList<String> SUTs;
    long threadID = -1;
    int input1;
    int input2;

    Map<String, String> killedMutantIDs;


    public MyThread(ArrayList<String> filenames, ReturnValues original_return, int input1, int input2){
        this.SUTs = filenames;
        this.original_return = original_return;
        killedMutantIDs = new HashMap<>();
        this.input1 = input1;
        this.input2 = input2;

    }

    public Map<String, String> getKilledIDs(){
        return killedMutantIDs;
    }

    public void run()
    {
        try
        {
            this.threadID = Thread.currentThread().getId();
            for(String sut: SUTs)
            {
                File text = new File(sut);
                Scanner scnr = new Scanner(text);
                String ID = scnr.nextLine().substring(2);
                ReturnValues mutant_return =  Helper.runProcess(ID,"java "+sut+" "+input1+" "+input2);
                if(original_return.output.equals(mutant_return.output)){
                //&& original_return.error_message.equals(mutant_return.error_message)
                //&& original_return.exit_value == mutant_return.exit_value){
                    System.out.println("survive mutant "+mutant_return.id);
                }
                else{
                    System.out.println("kill mutant "+mutant_return.id);
                    killedMutantIDs.put(mutant_return.id,"killed by ["+input1+","+input2+"]");
                }

            }

        }
        catch (Exception e)
        {
            // Throwing an exception
            System.out.println ("Exception is caught in thread " + threadID);
        }
        finally {
            //System.out.println("ending thread "+threadID);
            interrupt();
        }
    }


}

