import java.lang.Thread;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;


public  class ThreadManager{

    // MyRunnable myRunnable = new MyRunnable();
    // Thread thread = new Thread(myRunnable);
    // thread.start(); 



    
//    public static Thread createThread(int starting_line,int end_line){
//     MyRunnable myRunnable = new MyRunnable(starting_line,end_line);
//     Thread thread = new Thread(myRunnable);
//     // thread.start();
//     return thread;
//    }




   public static ArrayList<ManagedThread> getThreads(int numberOfThreads,int partition){
// Thread threadArr[];    
    // int arr[] = {1, 2, 4, 8, 16, 32, 64};
    
    ArrayList<ManagedThread> threadList = new ArrayList<>();



    for (int i =0 ; i<numberOfThreads;i++){
        
        int starting_line = (partition * i)+1;
        int end_line=starting_line+partition-1; 
        /*
        for ex thread =4 
        i=0  => 0
        i=1  => 12,5
        i=2  => 25
        i=3 => 37,5
        i=4 stop

        */
         threadList.add(new ManagedThread(starting_line, end_line));   

        
        
    }
    
    return threadList;
   
    }


}    