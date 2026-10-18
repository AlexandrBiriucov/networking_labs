import java.io.FileWriter;
import java.io.IOException;

public class NumberGenerator {

    public static void main(String[] args){

        int min = -1000000;
        int max = 1000000;

        try{

            FileWriter fw = new FileWriter("numbers.txt");
            for (int i=0;i<50000000;i++){

            int newNumber = (int)(((max-min)+1) * Math.random())+min;
            // System.out.println(newNumber);
            fw.write(String.valueOf(newNumber));
            fw.write("\n");
        }
        fw.close();
        }
        catch(IOException e){
            System.out.println("error");

        }
    }

    }


