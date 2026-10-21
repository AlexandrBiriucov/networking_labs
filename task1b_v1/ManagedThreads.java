package task1b_v1;

import java.lang.Thread;


public class ManagedThreads {

private final Thread thread;
private final MyRunnableB task;

public ManagedThreads(int[] arr, int start,int end){

this.task = new MyRunnableB(arr,start,end);
this.thread = new Thread(task);

}


public Thread getThread(){
    return thread;
}

public MyRunnableB getTask(){

    return  task;
}

}
