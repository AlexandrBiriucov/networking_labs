import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Task1Logic {
 
//1, 2, 4, 8, 16, 32 and 64    

static int totalLines;
static int totalPositive;
static int totalNegative;
static int zeros;


    public static void countLines(){
        try{
        BufferedReader reader = new BufferedReader(new FileReader("numbers.txt"));
        int lines = 0;
        while (reader.readLine() != null) lines++;
        totalLines = lines;
        reader.close();
        }
        catch(IOException e){
            System.out.println("File was not found");
            e.printStackTrace();

        }



    }


    public static int[] filePartitions(){
        countLines();
        int partitions[] = new int[7];
        
        int index=1;
        partitions[0]=totalLines;
        // 1, 2, 4, 8, 16, 32 and 64 
        for (int i=2;i<=64;i*=2){
            partitions[index]=totalLines/i;
            index++;


        }



        return partitions;

    }

    // public static void getThreads(int numberOfThreads){
    // // Thread threadArr[];    
    //     // int arr[] = {1, 2, 4, 8, 16, 32, 64};
    //     List<Thread> threadList;
    //     for (int i =0 ; i<numberOfThreads;i++){



    //     }









    public static void threadRun(){
    // ThreadManager threadManager = new ThreadManager();
    //         Thread thread = threadManager.createThread();
    //         thread.start();

    ArrayList<ManagedThread> threadList = new ArrayList<>();
    int arr[] = {1, 2, 4, 8, 16, 32, 64};
    int partitions[] = filePartitions();
    // HashMap<Integer,Integer> threadsToPartitionHashMap = new HashMap<>();
        // ThreadManager.getThreads(totalLines)

        // for(int i =0 ; i < arr.length;i++){
        //     threadsToPartitionHashMap.put(arr[i],partitions[i]);

        // }
        
        



        for(int j=0;j<arr.length;j++){
            //j number of the cuurent threads


            // threadList=ThreadManager.getThreads(arr[j],partitions[j]);
            threadList=ThreadManager.getThreads(arr[j],partitions[j]);


            long start = System.nanoTime();
            for(ManagedThread managedThread : threadList){
                managedThread.getThread().start();
                
            }
            for (ManagedThread managedThread : threadList){
                try{
                    managedThread.getThread().join();
                    
                }

                catch(InterruptedException e){
                    Thread.currentThread().interrupt();;
                    return;

                }
                
            }
           
            totalPositive = 0;
            totalNegative = 0;
            zeros = 0;


            for ( ManagedThread managedThread : threadList){

                totalPositive+=managedThread.getTask().getPositiveCount();
                totalNegative+=managedThread.getTask().getNegativeCount();
                zeros+=managedThread.getTask().getZeroCount();


            }
            
            long end= System.nanoTime();
            long time_execution_took= end-start;
            System.out.println("total positive " + totalPositive);
            System.out.println("total negative " +totalNegative);
            System.out.println("total zeros " +zeros);
            System.out.println("time taken for " + j + "threds" + "is " + time_execution_took / 1_000_000.0 + " ms");



            
        }





    }


    }







