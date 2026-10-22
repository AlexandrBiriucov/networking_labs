package task1b_v2;

public class MyRunnableB implements Runnable {
    
    //counters are declared static to make them shard by all the threads
    static  int positiveCounter;
    static int negativeCounter;
    static int zeroCounter;
    private int[] numbersArr;
    private int startIndex;
    private int endIndex;
    static final Object lock = new Object();

    public MyRunnableB(int[] nubmersArr,int  startIndex,int  endIndex){
        this.numbersArr=nubmersArr;
        this.startIndex= startIndex;
        this.endIndex = endIndex;


    }




   public void run(){
        
        // MyRunnableB.positiveCounter = 0;
        // MyRunnableB.negativeCounter = 0;
        // MyRunnableB.zeroCounter = 0;


        for (int i = startIndex; i < endIndex; i++) {
        synchronized(lock){
        if (numbersArr[i] > 0) {
            positiveCounter++;
        } else if (numbersArr[i] < 0) {
            negativeCounter++;
        } else {
            zeroCounter++;
                }
            }
        }   
    }


    public int getPositiveCount() {
    return positiveCounter;
            }

    public int getNegativeCount() {
        return negativeCounter;
            }

    public int getZeroCount() {
        return zeroCounter;
            }



}
