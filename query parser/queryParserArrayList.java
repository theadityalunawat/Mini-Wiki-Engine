import java.io.*;
import java.util.*;

public class queryParserArrayList {
        /* TODO : 1.Convert offset + index variables from int -> long
     2.Query parsing for field not proper. t:abc pqr def
     3.if we search till the last word then endoffset=startoffset+1;
     4.Move the sparse index filling outside.
      */

    static String sparseIndexFileName = "sparseIndex.txt";
    static String denseIndexFileName = "denseIndex.txt";
    static String semiSparseIndexFileName  = "semiSparseIndex.txt" ;
    static String sparseDocIdTitleFileName = "docIdTitleSparseIndex.txt";
    static String denseDocIdTitleFileName = "docIdTitleDenseIndex.txt";

    static final int categoryB=1;
    static final int categoryT=400;
    static final int categoryI=20;
    static final int categoryE=10;
    static final int categoryR=15;
    static final int categoryC=20;

    static StringBuilder documentId= new StringBuilder() ;
    static StringBuilder tempNumber=new StringBuilder() ;
    static StringBuilder currCategoryFreq= new StringBuilder();

    static ArrayList<DocIdWeightObject> listsOfObjects = new ArrayList<DocIdWeightObject>();
    static HashMap<String, Integer> hmIdWeight= new HashMap<String, Integer>();

    static void displaytop10Results(ArrayList<String> top10Ids) throws IOException {
        //System.out.println("--- Displaying top 10 results --- ");
        StringBuilder finalResult = new StringBuilder();

        BufferedReader br = new BufferedReader(new FileReader(sparseDocIdTitleFileName));

        ArrayList<Long> words = new ArrayList<Long>();
        ArrayList<Long> offset = new ArrayList<Long>();

        String line;
        String[] splittedLine = null;
        for ( int i=0 ; (line = br.readLine()) != null ; i++) {
            splittedLine = line.split(":");

            words.add(Long.parseLong(splittedLine[0]));
            offset.add(Long.parseLong(splittedLine[1]));
        }
        

        RandomAccessFile randomAccessFile = new RandomAccessFile(denseDocIdTitleFileName, "r");
        Long queryWord = null;
        for(String ids : top10Ids ){
            queryWord=Long.parseLong(ids);

            try{
                int index= Collections.binarySearch(words, queryWord);

                /* ---------------------------------------------------------- */

                long startOffset = 0, endOffset=0;

                if( index<0 ){
                    //Word not found
                    startOffset = offset.get((index + 2) * (-1));
                    endOffset = offset.get((index + 1) * (-1));
                }
                else{
                    startOffset=offset.get(index);
                    endOffset=startOffset+1;//so that the loop iterates only once.
                }

                /* ------------------ Search in Dense Index ----------------------------- */

                //System.out.println("Searching for query word : " + queryWord );

                randomAccessFile.seek(startOffset);

                boolean exactMatchFound=false;
                while( (line=randomAccessFile.readLine())!=null && endOffset>startOffset  ){

                    startOffset=startOffset+line.length();

                    splittedLine = line.split(":");
                    if( Long.parseLong(splittedLine[0])==queryWord ){
                     // * Term matched found, so take the line read. */
                        exactMatchFound=true;
                        break;
                    }

                }
            }
            catch( Exception e ){

            }

             finalResult.append(queryWord + "--" + splittedLine[1] + "\n");

        }
        

        System.out.println(finalResult);

    }

    static void assignWeightToDocs( StringBuilder postingList, int fieldOn ) throws IOException {
        //System.out.println(postingList);

        //If fieldOn==-1 then no field Search is disable, else enabled.


        int i=0;
        char c;
        long a, b;

        a=System.currentTimeMillis();

        int dashCount=0;
        c=postingList.charAt(i);
        //Skip the word
        try{
            for( ; dashCount<2 ; dashCount++ ){
                while(c!='-' ){
                    i++;
                    c=postingList.charAt(i);
                }
                i++;
            }
        }catch(Exception e){

        }


        documentId.setLength(0);
        tempNumber.setLength(0);
        currCategoryFreq.setLength(0);

        int currCategoryType=0, docWeight=0;

        for( ; i<postingList.length() ; i++ ){


            try{
                docWeight=0;
                documentId.setLength(0);

                c=postingList.charAt(i);
                while(c>='0' && c<='9'){
                    //documentid
                    documentId.append(c);
                    i++;
                    c=postingList.charAt(i);

                }
                //i++; //skip the colon


                while(c!=':'){
                    c=postingList.charAt(i++);

                    if( c=='B'){
                        currCategoryType=categoryB;
                    }else if( c=='T'){
                        currCategoryType=categoryT;
                    }else if( c=='C'){
                        currCategoryType=categoryC;
                    }else if( c=='I'){
                        currCategoryType=categoryI;
                    }else if( c=='E'){
                        currCategoryType=categoryE;
                    }

                    c=postingList.charAt(i);
                    currCategoryFreq.setLength(0);
                    while(c>='0' && c<='9'){
                        //frequency count for that category-type
                        currCategoryFreq.append(c);
                        i++;
                        c=postingList.charAt(i);
                    }

                    if( fieldOn<0 ){
                        //ie no field search, consider all fields equal
                        docWeight=docWeight+currCategoryType*Integer.parseInt(currCategoryFreq.toString());
                    }
                    else if( currCategoryType==fieldOn ){
                        docWeight=docWeight+currCategoryType*Integer.parseInt(currCategoryFreq.toString());
                    }

                }

                if( docWeight>50 ){
                    if( !hmIdWeight.containsKey(documentId.toString() ) ){
                        hmIdWeight.put(documentId.toString(), docWeight);
                    }else{
                        Integer temp = hmIdWeight.get(documentId.toString() );

                        hmIdWeight.put(documentId.toString(), 2*(temp+docWeight) );
                        //since the document is relevant to more than 1 query term, so increase its weightage.
                    }
                }



            }
            catch(Exception e ){

            }

        }//END for()

        b=System.currentTimeMillis();

        //System.out.println("--- assignWeightDocs time : " + (b-a) );
    }



    public static void main(String[] args) throws IOException {

        
//Read the query file
String inputFileName="input.txt";
BufferedReader in = new BufferedReader(new FileReader(inputFileName));

String lineInt,fullQuery;
lineInt=in.readLine();

int kk=Integer.parseInt(lineInt);

 RandomAccessFile randomAccessFile = new RandomAccessFile(semiSparseIndexFileName , "r");
 RandomAccessFile randomAccessFileDense = new RandomAccessFile(denseIndexFileName, "r");

//System.out.println(line);



        String line;
        //--------------------------------------------------------------
        long start, end, b, a;

       
        /* Read the sparse-index file & get it into a hashmap */
        BufferedReader br = new BufferedReader(new FileReader(sparseIndexFileName));

        ArrayList<String> words = new ArrayList<String>();
        ArrayList<Integer> offset = new ArrayList<Integer>();


        String[] splittedLine = null;
        for ( int i=0 ; (line = br.readLine()) != null ; i++) {
            splittedLine = line.split("-");

            words.add(splittedLine[0]);
            offset.add(Integer.parseInt(splittedLine[1]));
        }
      // br.close();

        /*-------------- Enter full query --------------*/
	for( int yu=0 ; yu<kk ; yu++){
            fullQuery=in.readLine();	
            System.out.println(fullQuery);
            start = System.currentTimeMillis();
        //String fullQuery="worldcup australia";

        String[] tempsplittedQueries = fullQuery.trim().split("\\s+");
        String[] field = null;
        int fieldOn;

        Porter stu = new Porter();
        int index;
        long startOffset = 0, endOffset=0;
        StringBuilder stringBuilder = new StringBuilder();

        for( String queryWord : tempsplittedQueries ){

            fieldOn=-1;

            //System.out.println("--- Parsing Query Term : "  + queryWord );

            a=System.currentTimeMillis();
            if( queryWord.contains(":")){
                field=queryWord.split(":");

                System.out.println(field[0] + " " + field[1]);

                queryWord=field[1];

                if( field[0]=="t" ){
                    fieldOn=categoryT;
                }
                else if( field[0]=="b" ){
                    fieldOn=categoryB;
                }
                else if( field[0]=="i" ){
                    fieldOn=categoryI;
                }
                else if( field[0]=="r" ){
                    fieldOn=categoryR;
                }
                else if( field[0]=="c" ){
                    fieldOn=categoryC;
                }
                else if( field[0]=="e" ){
                    fieldOn=categoryE;
                }
            }

            queryWord = queryWord.toLowerCase();


            stu.add(queryWord, queryWord.length());
            queryWord=stu.stem();

            index= Collections.binarySearch(words, queryWord);

            /* ----------------- Input Query Parsing ------------------- */


            if( index<0 ){
                //Word not found
                startOffset = offset.get((index + 2) * (-1));
                endOffset = offset.get((index + 1) * (-1));
            }
            else{
                startOffset=offset.get(index);
                endOffset=startOffset+1;//so that the loop iterates only once.
            }


            //System.out.println("Searching for query word : " + queryWord );

            randomAccessFile.seek(startOffset);

            line=null;
            int queryWordlength=queryWord.length();
            int lineLength;

            boolean exactMatchFound=false;
            for( int i=0, j=0 ; (line=randomAccessFile.readLine())!=null && endOffset>startOffset ; ){

                lineLength=line.length();
                //endOffset=endOffset-lineLength;
                startOffset=startOffset+lineLength;
                //System.out.println(line);


                while( i<lineLength && j<queryWordlength
                        && line.charAt(i)==queryWord.charAt(j) ){
                    i++;
                    j++;

                }

                if( j==queryWordlength && line.charAt(i)=='-'){
                    // * Term matched found, so take the line read. */
                    exactMatchFound=true;
                    break;
                }

            }

            if( exactMatchFound ){

                //System.out.println("--- Found in semiSparselist : " + line + " Now jumping in denseIndex");
                splittedLine = line.split("-");
                startOffset=Long.parseLong(splittedLine[1]);


                randomAccessFileDense.seek(startOffset);

                /*
                StringBuilder stringBuilder = new StringBuilder();
                for( int i=0 ; i<100 ; i++ ){
                    stringBuilder.append(randomAccessFile.readChar());

                }
                */
                //line = randomAccessFile.readLine();
                //System.out.println(line);

                /* Now read the posting list of the query word char by char.
                 * We read only first 100 entries.
                 */

                char c = 0;
                int delimiterCounter=0;



                stringBuilder.setLength(0);
                while( delimiterCounter<1000 && c!='\n' ){
                    c = (char)randomAccessFileDense.readByte() ;
                    if( c==':' ){
                        delimiterCounter++;
                    }

                    stringBuilder.append(c);

                }
                //System.out.println("--- Printing first 1000 or less doc-ids of the matched query : ");
                //System.out.println(stringBuilder);

                b=System.currentTimeMillis();

                //System.out.println("Time for query's index search + go to 2 files offset + read 50 inverted index : " + (b-a) );

                assignWeightToDocs(stringBuilder, fieldOn);


                //System.out.println("Searching : " + queryWord + " Time taken: " + (b-a));

            }
            else{
                System.out.println("ERROR : 404 - Not Found !");
            }

        }


       
        /* ------------- Finished parsing all the query terms from the query -------------- */

        /* Convert the entire map into objects, so that we can sort all of them based on their weights */

        long objStart=System.currentTimeMillis();
        for( Map.Entry<String, Integer> hmEntry: hmIdWeight.entrySet() ){
            DocIdWeightObject obj = new DocIdWeightObject();
            obj.setId(hmEntry.getKey());
            obj.setWeight(hmEntry.getValue());

            listsOfObjects.add(obj);

        }
		 hmIdWeight.clear();
        //Sort all the objects based on their weights.
        Collections.sort(listsOfObjects, DocIdWeightObject.WeightComparator);

        //Get top 10 doc-IDs based on weight and display their titles
        DocIdWeightObject tempObj = null;
        ArrayList<String> top10Ids = new ArrayList<String>();

        for(int j=0 ; j<10 && j<listsOfObjects.size() ; j++ ){
            tempObj = listsOfObjects.get(j);
            //System.out.println(tempObj.getId() + "--" + tempObj.getWeight());

            top10Ids.add(tempObj.getId());
        }

        long objEnd = System.currentTimeMillis();
        //System.out.println("--- Time for map object convert" + (objEnd-objStart)  );

        a=System.currentTimeMillis();
        displaytop10Results(top10Ids);
        b=System.currentTimeMillis();

        //System.out.println("Time for display : " + (b-a) );

        end=System.currentTimeMillis();

        System.out.println("Time(in millisecs) : " + (end-start) );

        hmIdWeight.clear();
            stringBuilder.setLength(0);
            field=null;splittedLine=null;tempsplittedQueries=null;top10Ids.clear();fullQuery=null;
            listsOfObjects.clear();
            
    }
        br.close();
         randomAccessFile.close();
         randomAccessFileDense.close();
         randomAccessFile.close();
         in.close();
   }
}
