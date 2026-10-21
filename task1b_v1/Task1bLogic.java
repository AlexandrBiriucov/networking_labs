
package task1b_v1;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Task1bLogic{

public static int count_lines(){
int numberLines=0;

try{
    BufferedReader bf = new BufferedReader(new FileReader("numbers.txt"));
        
        while ((bf.readLine()) != null){
               numberLines++;

        }


    }

    catch (IOException e){
        System.out.println("Error reading file");
        e.printStackTrace();
    }

    return numberLines;

}

public static int[] loadArr(){

int[] numbersArr=new int[count_lines()];
int i=0;
String line;

    try{
    BufferedReader bf = new BufferedReader(new FileReader("numbers.txt"));
        
        while ((line=bf.readLine()) != null){
            numbersArr[i]=Integer.parseInt(line);     
                i++;
        }
        

    }

    catch (IOException e){
        System.out.println("Error reading file");
        e.printStackTrace();
    }
        return numbersArr;
}


    public static void runThreads() {
        int[] numbersArr = loadArr();
        ArrayList<ManagedThreads> ManagedThreadList= ThreadManagerB.getThreads(numbersArr);


        int numberOfProcessors = Runtime.getRuntime().availableProcessors();

        long start = System.nanoTime();
        for (ManagedThreads managedThread : ManagedThreadList ){
                            
                 managedThread.getThread().start();

            } 

                for (ManagedThreads managedThread : ManagedThreadList) {

            try {
                managedThread.getThread().join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }





        long end = System.nanoTime();



           int  totalPositive = 0;
            int totalNegative = 0;
            int zeros = 0;


            for ( ManagedThreads managedThread : ManagedThreadList){

                totalPositive+=managedThread.getTask().getPositiveCount();
                totalNegative+=managedThread.getTask().getNegativeCount();
                zeros+=managedThread.getTask().getZeroCount();


            }
        
            double timeTaken = (end - start) / 1_000_000.0;
            System.out.println("total positive " + totalPositive);
            System.out.println("total negative " +totalNegative);
            System.out.println("total zeros " +zeros);
            System.out.println("Time taken: " + timeTaken + " ms");


            }






    }



