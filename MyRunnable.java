import java.util.HashMap;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.lang.Integer;


public class MyRunnable implements Runnable {

    // private HashMap<Integer,Integer> indexToThreadMap = new HashMap<>();
    private int starting_line;
    private int end_line;
    private int positiveCount = 0;
    private int negativeCount = 0;
    private int zeroCount = 0;
    public MyRunnable(int starting_line,int end_line){
        this.starting_line = starting_line; 
        this.end_line= end_line;
    }

    @Override 
    public void  run(){
        
    try (BufferedReader br = new BufferedReader(new FileReader("numbers.txt"))) {
    //     String line;
    //     int i = 0;
    //     while ((line = br.readLine()) != null) {
    //     // System.out.println(line);
    //         i++;
        
        
    //   }

    //   System.out.println(i);
        String line;
        int lineNumber = 1;
        

        while((line = br.readLine()) !=null ){
            if (lineNumber >= starting_line && lineNumber<=end_line){
                int num = Integer.parseInt(line);
                 if (num > 0) {
                    positiveCount++;
                } else if (num < 0) {
                    negativeCount++;
                } else {
                    zeroCount++;
                }

            }


        if(lineNumber>=end_line){
            break;



        }    


        lineNumber++;

        }






    } 
    catch (IOException e) {
      System.out.println("Error reading file.");
    }


         

        

                public int getPositiveCount() {
            return positiveCount;
        }

            public int getNegativeCount() {
                return negativeCount;
            }

            public int getZeroCount() {
                return zeroCount;
            }

    }


    

}