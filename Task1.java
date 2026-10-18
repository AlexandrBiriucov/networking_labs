import java.lang.Thread;
// import java.io.FileReader;
// import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.File;                  // Import the File class
import java.io.FileNotFoundException; 
public  class Task1{


public static void oneThread(){
    
    File myObj = new File("number.txt");

    // try-with-resources: Scanner will be closed automatically
    try (Scanner myReader = new Scanner(myObj)) {
      while (myReader.hasNextLine()) {
        String data = myReader.nextLine();
        System.out.println(data);
      }
    } catch (FileNotFoundException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();





}
}
}