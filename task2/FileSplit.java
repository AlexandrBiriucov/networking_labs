package task2;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;


public class FileSplit {
 
public static BufferedReader createR() throws IOException{      
    BufferedReader br = new BufferedReader(new FileReader("numbers.txt"));


    return br;

    }




public static int getFileLength(BufferedReader bf){

    int length=0;
    try {
    while (bf.readLine() != null){

        length++;

    }

}
    catch(IOException e){
        e.printStackTrace();
                
    }





    return length;


}  


    public static void SplitFile(){
        try{
             BufferedReader br  = createR();
            //6250000
            for (int part = 1; part <= 8; part++) {

        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter("task2\\part" + part + ".txt"))) {

            for (int i = 0; i < 6_250_000; i++) {
                String line = br.readLine();

                if (line == null) {
                    break;
                }

                writer.write(line);
                writer.newLine();
            }
        }
    }           

                
            

            
        }

     catch (IOException e ){
        e.printStackTrace();
     }



        } 





}

