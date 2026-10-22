package task1c;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;




public class Reader {
    

    final private static int BATCH_SIZE = 10000; 



    public static BufferedReader startReader() throws IOException{

        BufferedReader br = new BufferedReader(new FileReader("numbers.txt"));

        return br;
    }


    public static int[] readBatches(BufferedReader br) throws IOException {
        int[] batchArr = new int[BATCH_SIZE];
        int count = 0;

        for (int i = 0; i < BATCH_SIZE; i++) {
            String line = br.readLine();

            if (line == null) {
                break;
            }

            batchArr[count] = Integer.parseInt(line);
            count++;
            
        }

        return Arrays.copyOf(batchArr, count);

    }





}
