package task1b_v1;

import java.util.ArrayList;

public class ThreadManagerB {
    

    public static ArrayList<ManagedThreads> getThreads(int[] numbersArr){
        ArrayList<ManagedThreads> threadList = new ArrayList<>(); 
        
        //i have 14 logical processorsș
        // int logical_processors = 14;
       int numberOfProcesors = Runtime.getRuntime().availableProcessors();

       
       for  (int i = 0 ; i < numberOfProcesors ; i++){
         int startIndex = (numbersArr.length * i) / numberOfProcesors;
        int endIndex = (numbersArr.length * (i + 1)) / numberOfProcesors;
            threadList.add(new ManagedThreads(numbersArr,startIndex, endIndex));
            
    }

        return threadList;

    }




    
}
