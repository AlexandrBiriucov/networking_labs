public class test {
    public static void main(String[] java){
    int countLines = 50000000;
    int totalLines = countLines;
    int partitions[] = new int[7];
    
    int index=1;
    partitions[0]=totalLines;
    // 1, 2, 4, 8, 16, 32 and 64 
    for (int i=2;i<=64;i*=2){
        partitions[index]=totalLines/i;
        index++;
    }
    

    for(int i=0;i<partitions.length;i++){
        System.out.print(partitions[i]);
    }


}
}
