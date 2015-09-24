
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;
import java.util.TreeMap;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author tushan
 */
public class processQuery {

    public static void main(String args[]) {
        
        try {
            //--------------HashMap to Store Full Sparse Index-----------------------------
            String p;
            BufferedReader pointerSparse = new BufferedReader(new FileReader("sparseIndex.txt"));
            ArrayList<String> word = new ArrayList<>();
            ArrayList<Long> offset = new ArrayList<>();
            String tempArr[];
            while ((p = pointerSparse.readLine()) != null) {
                tempArr = p.split(" ");
                word.add(tempArr[0]);
                offset.add(Long.parseLong(tempArr[1]));
                tempArr = null;
            }
            pointerSparse.close();
            //--------------Reading query and displaying result---------------------------
            Scanner scan = new Scanner(System.in);
            String query = scan.nextLine();
            query = query.toLowerCase();
            //System.out.println(query);
            String queryArray[];
            queryArray = query.split(" ");

            int i = 0, j = 0;
            int index = 0;
           
            RandomAccessFile raf = new RandomAccessFile("denseIndex.txt", "r");
            long tempOffset;
            while (i < queryArray.length) {

                //System.out.println();
                index = Collections.binarySearch(word, queryArray[i].trim()); // search in word 
                if (index < 0) {
                    index = -index - 2;
                }
                tempOffset = offset.get(index);
                
                //---we got index now we search in denseIndex.txt file for final posting list----------
                
                i++;
            }
            

        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
