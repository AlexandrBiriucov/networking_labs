import java.lang.Thread;

public class ManagedThread {
 
    public final Thread thread;
    private final MyRunnable task;


     public ManagedThread(int startingLine, int endLine) {
        task = new MyRunnable(startingLine, endLine);
        thread = new Thread(task);
    }



    public Thread getThread() {
        return thread;
    }

    public MyRunnable getTask() {
        return task;
    }

}
