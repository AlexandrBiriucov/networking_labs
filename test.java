



public class test{
    

    public static void main(String[] args) {
        String newStr = "ORDER|coffee|5";
        String newStr1 = "MENU"; 
    String[] splitStr = newStr.split("\\|");
    for (String str : splitStr){
        System.out.println(str);

    }

    System.out.println(splitStr[1]);


    }


}