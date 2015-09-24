
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author tushan
 */
public class createIndex {
    public static void createFirstSparseIndex() {
        long byteCount =0;
        int lineCount=0;
        try{
        File fileN = new File("sparseIndex.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileN)); 
        String p="";
        String S="";
        BufferedReader br = new BufferedReader(new FileReader("denseIndex.txt"));
        while( (p = br.readLine()) != null) {
            if( lineCount % 200 ==0 ){
                lineCount=0;
                writer.write(p.substring(0, p.indexOf(" ")+1));
                writer.write(Long.toString(byteCount));
                writer.newLine();
            }
            byteCount +=p.getBytes().length +1;
            lineCount++;
        }
        br.close();
        writer.close();
        }catch(Exception e){
            return ;
        }
    }
}