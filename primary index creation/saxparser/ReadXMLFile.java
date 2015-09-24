import com.sun.xml.internal.ws.message.stream.StreamAttachment;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.net.*;
import org.xml.sax.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import static java.nio.file.Files.delete;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

public class ReadXMLFile {
    public static StringBuilder sbText = new StringBuilder();
    public static StringBuilder sbTitle = new StringBuilder();
    public static ArrayList<BufferedWriter> filePointer = new ArrayList<>();
    public static info obj = new info();
    public static File fa ;
    public static BufferedWriter output ;
    public static int totalNoOfFiles=0;
    static int pageCount =0;
    public static HashMap< String , HashMap<String , countOfData> > finalIndex= new HashMap<>();
    public static HashMap<String , Boolean> stopWordHM = new HashMap<>();
   public static void main(String args[]) {
    try {
        
	SAXParserFactory factory = SAXParserFactory.newInstance();
	SAXParser saxParser = factory.newSAXParser();
	DefaultHandler handler = new DefaultHandler() {
	boolean text = false;
        boolean page = false;
        boolean title = false;
        boolean first = true;
        boolean id = false;
        public void startElement(String uri, String localName,String qName,Attributes attributes) throws SAXException {
            if(qName.equalsIgnoreCase("PAGE")){
                page = true;
            }
            if(qName.equalsIgnoreCase("TEXT")){
                text = true;
            }
            if(qName.equalsIgnoreCase("ID")){
                id = true;
            }
            if(qName.equalsIgnoreCase("TITLE")){
                title = true;
            }
	}
        
	public void characters(char ch[], int start, int length) throws SAXException {
            if(id){
                if(first){
                    obj.id.append(new String(ch,start , length));
                    first = false;
                }
                id = false;
            }
            if(text){
                
                    obj.sbText.append(new String(ch,start , length).toLowerCase());
                
            }
            
            if(title){
                obj.sbTitle.append(new String(ch, start , length).toLowerCase());
                obj.sbTitle.append(" ");
                title = false;
            }
	}
        
        public void endElement(String uri, String localName,String qName) throws SAXException {
                if(qName.equalsIgnoreCase("PAGE")){
                    obj.parseingOfData();
                    pageCount++;
                    if( pageCount%1000==0 ){
                        
                        String s = "";
                        s = s+totalNoOfFiles+".txt";
                        fa = new File(s);
                        
                        try {
                            output = new BufferedWriter(new FileWriter(fa));
                            
                        } catch (IOException ex) {
                            Logger.getLogger(ReadXMLFile.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        totalNoOfFiles++;
                        
                        TreeMap<String , HashMap<String , countOfData>> treeMap = new TreeMap<>();
                        treeMap.putAll(finalIndex);
                        StringBuilder temp = new StringBuilder();
                        for (Map.Entry<String, HashMap<String, countOfData>> e : treeMap.entrySet()) {
                        HashMap<String , countOfData> s1 = e.getValue();
                            temp.setLength(0);
                            temp.append(e.getKey());
                            temp.append(" ");
                        for (Map.Entry<String, countOfData> e2 : s1.entrySet()){
                            temp.append(e2.getKey());
                            temp.append(":");
                            countOfData tempo = e2.getValue();
                            
                            if(tempo.title_count!=0){
                                temp.append("T");
                                temp.append(tempo.title_count);
                            }
                            if(tempo.infobox_count!=0){
                                temp.append("I");
                                temp.append(tempo.infobox_count);
                            }
                            if(tempo.category_count!=0){
                                temp.append("C");
                                temp.append(tempo.category_count);
                            }
                            
                            if(tempo.body_count!=0){
                                temp.append("B");
                                temp.append(tempo.body_count);
                            }
                            if(tempo.external_link_count!=0){
                                temp.append("E");
                                temp.append(tempo.external_link_count);
                            }
                            temp.append(";");
                        }
                            try {
                                output.write(temp.toString());
                            } catch (IOException ex) {
                                Logger.getLogger(ReadXMLFile.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            try {
                                output.newLine();
                            } catch (IOException ex) {
                                Logger.getLogger(ReadXMLFile.class.getName()).log(Level.SEVERE, null, ex);
                            }
                    }
                        finalIndex.clear();
                        treeMap.clear();
                    }
                    page = false;
                    obj.sbTitle.setLength(0);
                    obj.sbText.setLength(0);
                    obj.id.setLength(0);
                    obj.infobox.setLength(0);
                    obj.sbExternalLinks.setLength(0);
                    first = true;
                }
                if(qName.equalsIgnoreCase("TEXT")) {
                    text = false ;
                }
        }
     };
       info f = new info();
       f.initialize();
       File file = new File("wiki_small.xml");
       InputStream inputStream= new FileInputStream(file);
       Reader reader = new InputStreamReader(inputStream,"UTF-8");
       InputSource is = new InputSource(reader);
       is.setEncoding("UTF-8");
       saxParser.parse(is, handler);
       //Add the remaining pages to the file if any left//
        //System.out.println(totalNoOfFiles);
        //System.out.println();
       

        //----------------Merge files and make final index file--------------
       //public static BufferedWriter output ;fa = new File(s); output = new BufferedWriter(new FileWriter(fa));
       //PrintWriter writer = new PrintWriter( "output.txt" , "UTF-8" );
       File fileN = new File("denseIndex.txt");
       BufferedWriter writer = new BufferedWriter(new FileWriter(fileN)); 
       ArrayList <BufferedReader> finalBr = new ArrayList<>();
       ArrayList <Boolean> fileStatus= new ArrayList<>();
       Comparator<newdata> com = new comparator();
       //totalNoOfFiles++;
       //System.out.println(totalNoOfFiles);
       PriorityQueue <newdata> Q = new PriorityQueue<newdata>( totalNoOfFiles, com ); 
       String fileName ="";
       String p="";
       String arr[];
       newdata putInQueue ,temp;
       for(int j = 0 ;j<totalNoOfFiles ;j++){
                fileName = "";
                fileName = j + ".txt";
                finalBr.add( new BufferedReader(new FileReader(fileName)));
                fileStatus.add(Boolean.FALSE);
                p = finalBr.get(j).readLine();
                arr = p.split(" ") ;
                putInQueue = new newdata();
                putInQueue.fileNo = j;
                putInQueue.word = arr[0];
                putInQueue.postingList= p;
                Q.add(putInQueue);
       }
        int start =0, end=0;
        int x;
        String postToMerge;
        while(Q.size() != 0)
        {
        temp=Q.remove();
        x=temp.fileNo;
        try {
        if(Q.size() > 0 && temp.word.equals(Q.peek().word)){
        start=temp.postingList.indexOf(' ')+1;
        end=temp.postingList.length();
        postToMerge=temp.postingList.substring(start,end);
        Q.peek().postingList=Q.peek().postingList+postToMerge;
        if(fileStatus.get(x).equals(Boolean.TRUE))
        {
            continue;
        }
        if((p=finalBr.get(x).readLine()) == null)
        {
            finalBr.get(x).close();
            fileStatus.set(x, Boolean.TRUE);
            continue;
        }
        else{
            putInQueue = new newdata();
            putInQueue.postingList=p;
            putInQueue.word=p.substring(0,p.indexOf(' '));
            putInQueue.fileNo=x;
            Q.add(putInQueue);
        }
        }
        else
        {
            writer.write(temp.postingList);
            writer.newLine();
            writer.flush();
        //writer.println(temp.postingList);
        if(fileStatus.get(x).equals(Boolean.TRUE))
        {
        continue;
        }

        if((p=finalBr.get(x).readLine()) == null)
        {
            finalBr.get(x).close();
            fileStatus.set(x, Boolean.TRUE);
            continue;
        }
        else
        {
           putInQueue= new newdata();
            putInQueue.postingList=p;
            putInQueue.word=p.substring( 0, p.indexOf(' ') );
            putInQueue.fileNo=x;
            Q.add(putInQueue);
        }
    }
//System.out.println(queue.remove().sortString);
    }catch (Exception ex) {
    //Logger.getLogger(ReadXMLFile.class.getName()).log(Level.SEVERE, null, ex);
    continue;
    }
    }
        writer.close();
     } catch (Exception e) {
       e.printStackTrace();
     }
     createIndex.createFirstSparseIndex();
   }
}