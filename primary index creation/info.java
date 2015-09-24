
import static java.lang.Character.isLetter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.StringTokenizer;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author sambhav
 */
public class info {

    static String stop_words[] = {
        "a", "about", "above", "across", "after", "again", "against", "all", "almost", "alone", "along", "already", "also",
        "although", "always", "among", "an", "and", "another", "any", "anybody", "anyone", "anything", "anywhere", "are", "area",
        "areas", "around", "as", "ask", "asked", "asking", "asks", "at", "away", "b", "back", "backed", "backing", "backs", "be",
        "became", "because", "become", "becomes", "been", "before", "began", "behind", "being", "beings", "best", "better",
        "between", "big", "both", "but", "by", "c", "came", "can", "cannot", "case", "cases", "certain", "certainly", "clear",
        "clearly", "come", "could", "d", "did", "differ", "different", "differently", "do", "does", "done", "down", "down",
        "downed", "downing", "downs", "during", "e", "each", "early", "either", "end", "ended", "ending", "ends", "enough",
        "even", "evenly", "ever", "every", "everybody", "everyone", "everything", "everywhere", "f", "face", "faces", "fact",
        "facts", "far", "felt", "few", "find", "finds", "first", "for", "four", "from", "full", "fully", "further", "furthered",
        "furthering", "furthers", "g", "gave", "general", "generally", "get", "gets", "give", "given", "gives", "go", "going",
        "good", "goods", "got", "great", "greater", "greatest", "group", "grouped", "grouping", "groups", "h", "had", "has", "have",
        "having", "he", "her", "here", "herself", "high", "high", "high", "higher", "highest", "him", "himself", "his", "how",
        "however", "i", "if", "important", "in", "interest", "interested", "interesting", "interests", "into", "is", "it", "its",
        "itself", "j", "just", "k", "keep", "keeps", "kind", "knew", "know", "known", "knows", "l", "large", "largely", "last", "later",
        "latest", "least", "less", "let", "lets", "like", "likely", "long", "longer", "longest", "m", "made", "make", "making", "man",
        "many", "may", "me", "member", "members", "men", "might", "more", "most", "mostly", "mr", "mrs", "much", "must", "my", "myself",
        "n", "necessary", "need", "needed", "needing", "needs", "never", "new", "new", "newer", "newest", "next", "no", "nobody", "non",
        "noone", "not", "nothing", "now", "nowhere", "number", "numbers", "o", "of", "off", "often", "old", "older", "oldest", "on",
        "once", "one", "only", "open", "opened", "opening", "opens", "or", "order", "ordered", "ordering", "orders", "other", "others", "our", "out", "over", "p", "part", "parted", "parting", "parts", "per", "perhaps", "place", "places", "point", "pointed",
        "pointing", "points", "possible", "present", "presented", "presenting", "presents", "problem", "problems", "put", "puts", "q",
        "quite", "r", "rather", "really", "right", "right", "room", "rooms", "s", "said", "same", "saw", "say", "says", "second", "seconds",
        "see", "seem", "seemed", "seeming", "seems", "sees", "several", "shall", "she", "should", "show", "showed", "showing", "shows", "side",
        "sides", "since", "small", "smaller", "smallest", "so", "some", "somebody", "someone", "something", "somewhere", "state", "states",
        "still", "still", "such", "sure", "t", "take", "taken", "than", "that", "the", "their", "them", "then", "there", "therefore", "these",
        "they", "thing", "things", "think", "thinks", "this", "those", "though", "thought", "thoughts", "three", "through", "thus", "to",
        "today", "together", "too", "took", "toward", "turn", "turned", "turning", "turns", "two", "u", "under", "until", "up", "upon", "us",
        "use", "used", "uses", "v", "very", "w", "want", "wanted", "wanting", "wants", "was", "way", "ways", "we", "well", "wells", "went", "were",
        "what", "when", "where", "whether", "which", "while", "who", "whole", "whose", "why", "will", "with", "within", "without", "work",
        "worked", "working", "works", "would", "x", "y", "year", "years", "yet", "you", "young", "younger", "youngest", "your", "yours", "z", "http",
        "www", "com", "in", "org", "ca", "de", "fr", "io", "it", "ht", "mr", "nl", "pt", "vo", "references", "reflist", "external", "links", "br", "box",
        "nbsp", "blockquote", "quot", "miles", "mile", "km", "mt", "background", "color", "font", "size", "align", "style", "center", "height",
        "px", "nm", "la", "mpr", "noh", "nah", "mi", "sq", "sqmi", "ei", "id", "qr", "dp", "ds", "en", "wa", "aaaaaa", "jpeg", "jpg", "png", "gif", "cellpadding", "cellspacing", "amp", "inline", "dms", "infobox", "com", "www", "http", "image", "category"
    };
    //HashMap<String , Boolean> hm = new HashMap<>();
    static HashSet<String> stopWordHM = new HashSet<>();

    public static void initialize() {
        for (int i = 0; i < stop_words.length; i++) {
            stopWordHM.add(stop_words[i]);
        }
    }

    //stopWordHM = stopWordMap.makeHashMapOfStopWords();
    StringBuilder sbTitle = new StringBuilder();
    StringBuilder sbText = new StringBuilder();
    StringBuilder sbCategory = new StringBuilder();
    StringBuilder id = new StringBuilder();
    StringBuilder infobox = new StringBuilder();
    StringBuilder sbExternalLinks = new StringBuilder();

    public void parseingOfData() {

        int textLength = sbText.length();
        info temp = new info();
        temp.sbTitle = sbTitle;
        temp.id = id;
        char c;
        try {
            for (int i = 0; i < textLength; i++) {
                try {
                    c = sbText.charAt(i);
                    if (((int) c >= 'a' && (int) c <= 'z') || c == ' ') {
                        temp.sbText.append(c);

                    } else if (c == '{') {
                        if (i + 9 < textLength && sbText.substring(i + 1, i + 9).equals("{infobox")) {
                            int count = 0;
                            for (; i < textLength; i++) {
                                c = sbText.charAt(i);
                                temp.infobox.append(c);
                                if (c == '{') {
                                    count++;
                                } else if (c == '}') {
                                    count--;
                                }
                                if (count == 0 || (c == '=' && i + 1 < textLength && sbText.charAt(i + 1) == '=')) {
                                    if (c == '=') {
                                        temp.infobox.deleteCharAt(infobox.length() - 1);
                                    }
                                    i--;
                                    break;
                                }
                            }
                        }
                    } else if (c == '[') {
                        if (i + 11 < textLength && sbText.substring(i + 1, i + 11).equals("[category:")) {
                            int count = 0;
                            //i =i+10;
                            for (; i < textLength; i++) {
                                c = sbText.charAt(i);
                                if (c == '[' || c == ']') {
                                    temp.sbCategory.append(' ');
                                } else {
                                    temp.sbCategory.append(c);
                                }
                                if (c == '[') {
                                    count++;
                                } else if (c == ']') {
                                    count--;
                                }
                                if (count == 0 || (c == '=' && i + 1 < textLength && sbText.charAt(i + 1) == '=')) {
                                    if (c == '=') {
                                        temp.sbCategory.deleteCharAt(sbCategory.length() - 1);
                                    }
                                    i--;
                                    break;
                                }
                            }
                        }

                    } else if (c == '=' && i + 1 < textLength && sbText.charAt(i + 1) == '=') {
                        i += 2;
                        while (i < textLength && ((c = sbText.charAt(i)) == ' ' || (c = sbText.charAt(i)) == '\t')) {
                            i++;
                        }

                        if (i + 16 < textLength && sbText.substring(i, i + 16).equalsIgnoreCase("external links==")) {
                            i += 17;
                            c = sbText.charAt(i);
                            //System.out.println(sbText.charAt(i));
                            if (c == '*') {
                                i++;
                                if (i < textLength) {
                                    c = sbText.charAt(i);
                                }
                                while (c == ' ') {
                                    i++;
                                    c = sbText.charAt(i);
                                }
                                if (c == '[') {
                                    i++;
                                    while (i < textLength && c != ']') {
                                        c = sbText.charAt(i);
                                        if (c == ']') {
                                            break;
                                        }
                                        temp.sbExternalLinks.append(c);
                                        i++;
                                    }
                                }
                            }

                        }

                        //sbExternalLinks.setLength(0);
                    } else {
                        temp.sbText.append(' ');
                    }
                } catch (Exception e) {
                    return;
                }
            }
        } catch (Exception e) {
            return;
        }
        //System.out.println(temp.sbExternalLinks);
        try {
            removeUnwantedWords(temp);

        } catch (Exception e) {
            return;
        }
    }

    public void removeUnwantedWords(info temp) {
        //---------------------removal of stop words and special character for title---------
        try {
            int length = temp.sbTitle.length();
            String s;
            StringBuilder tempSB = new StringBuilder();
            char c;
            int i;
            for (i = 0; i < length; i++) {
                c = temp.sbTitle.charAt(i);
                if (isLetter(c)) {
                    tempSB.append(c);
                } else {
                    s = new String(tempSB);
                    if (s.length() > 2 && s.length() < 20) {
                        removeStopWordAndStemTitle(s, temp.id.toString());
                    }
                    tempSB.setLength(0);
                }
            }

            //---------------removal of special character and stop words for text----------
            tempSB.setLength(0);
            length = temp.sbText.length();
            for (i = 0; i < length; i++) {
                c = temp.sbText.charAt(i);
                if (isLetter(c)) {
                    tempSB.append(c);
                } else {
                    s = new String(tempSB);
                    if (s.length() > 2 && s.length() < 20) {
                        removeStopWordAndStemBody(s, temp.id.toString());
                    }
                    tempSB.setLength(0);
                }
            }

            //--------------removal of special character and stop word for category-----------
            tempSB.setLength(0);
            length = temp.sbCategory.length();
            for (i = 0; i < length; i++) {
                c = temp.sbCategory.charAt(i);
                if (isLetter(c)) {
                    tempSB.append(c);
                } else {
                    s = new String(tempSB);

                    if (s.length() > 2 && s.length() < 20 ) {

                        removeStopWordAndStemCategory(s, temp.id.toString());
                    }
                    tempSB.setLength(0);
                }
            }
            //-----------removal of special character and stop words for infobox------------//
            s = "";
            tempSB.setLength(0);
            length = temp.infobox.length();
            int count = 0;
            for (i = 0; i < length; i++) {
                c = temp.infobox.charAt(i);
                if (c == '[') {
                    count++;
                }
                if (c == ']') {
                    count--;
                }
                if (count == 2) {

                    if (isLetter(c)) {
                        tempSB.append(c);
                    } else {
                        s = new String(tempSB);
                        if (s.length() > 2 && s.length() < 20) {
                            removeStopWordAndStemInfobox(s, temp.id.toString());
                        }
                        tempSB.setLength(0);
                    }
                }
            }
            //----------removal of special character and stop words for external links------------//
            s = "";
            tempSB.setLength(0);
            length = temp.sbExternalLinks.length();
            for (i = 0; i < length; i++) {
                c = temp.sbExternalLinks.charAt(i);
                if (isLetter(c)) {
                    tempSB.append(c);
                } else {
                    s = new String(tempSB);

                    if (s.length() > 2 && s.length() < 20) {
                        removeStopWordAndStemExternalLinks(s, temp.id.toString());
                    }
                    tempSB.setLength(0);
                }
            }
        } catch (Exception e) {
            return;
        }
    }

    public void removeStopWordAndStemTitle(String s, String id) {
        try {
            Stemmer objStemmer = new Stemmer();
//        if(  !stopWordHM.contains(s)  ){
            objStemmer.add(s.toCharArray(), s.length());
            String sReturn = objStemmer.stem();
            //------------------------ put value in hashMap----------------------//

            if (ReadXMLFile.finalIndex.containsKey(sReturn)) {
                if (!ReadXMLFile.finalIndex.get(sReturn).containsKey(id)) {
                    countOfData temp = new countOfData();
                    temp.title_count++;
                    ReadXMLFile.finalIndex.get(sReturn).put(id, temp);
                } else {
                    ReadXMLFile.finalIndex.get(sReturn).get(id).title_count++;
                }
            } else {
                countOfData temp = new countOfData();
                temp.title_count++;
                HashMap<String, countOfData> tempHM = new HashMap<>();
                tempHM.put(id, temp);
                ReadXMLFile.finalIndex.put(sReturn, tempHM);
            }
        } catch (Exception e) {
            return;
        }
    }

    public void removeStopWordAndStemBody(String s, String id) {
        try {
            Stemmer objStemmer = new Stemmer();
            if (!stopWordHM.contains(s)) {
                objStemmer.add(s.toCharArray(), s.length());
                String sReturn = objStemmer.stem();
                //------------------------ put value in hashMap----------------------//

                if (ReadXMLFile.finalIndex.containsKey(sReturn)) {
                    if (!ReadXMLFile.finalIndex.get(sReturn).containsKey(id)) {
                        countOfData temp = new countOfData();
                        temp.body_count++;
                        ReadXMLFile.finalIndex.get(sReturn).put(id, temp);
                    } else {
                        ReadXMLFile.finalIndex.get(sReturn).get(id).body_count++;
                    }
                } else {
                    countOfData temp = new countOfData();
                    temp.body_count++;
                    HashMap<String, countOfData> tempHM = new HashMap<>();
                    tempHM.put(id, temp);
                    ReadXMLFile.finalIndex.put(sReturn, tempHM);
                }
            }
        } catch (Exception e) {
            return;
        }
    }

    public void removeStopWordAndStemCategory(String s, String id) {
        try {
            Stemmer objStemmer = new Stemmer();
            if (!stopWordHM.contains(s)) {
                objStemmer.add(s.toCharArray(), s.length());
                String sReturn = objStemmer.stem();
                //------------------------ put value in hashMap----------------------//

                if (ReadXMLFile.finalIndex.containsKey(sReturn)) {
                    if (!ReadXMLFile.finalIndex.get(sReturn).containsKey(id)) {
                        countOfData temp = new countOfData();
                        temp.category_count++;
                        ReadXMLFile.finalIndex.get(sReturn).put(id, temp);
                    } else {
                        ReadXMLFile.finalIndex.get(sReturn).get(id).category_count++;
                    }
                } else {
                    countOfData temp = new countOfData();
                    temp.category_count++;
                    HashMap<String, countOfData> tempHM = new HashMap<>();
                    tempHM.put(id, temp);
                    ReadXMLFile.finalIndex.put(sReturn, tempHM);
                }
            }
        } catch (Exception e) {
            return;
        }
    }

    public void removeStopWordAndStemInfobox(String s, String id) {
        try {
            Stemmer objStemmer = new Stemmer();
            if (!stopWordHM.contains(s)) {
                objStemmer.add(s.toCharArray(), s.length());
                String sReturn = objStemmer.stem();
                //------------------------ put value in hashMap----------------------//

                if (ReadXMLFile.finalIndex.containsKey(sReturn)) {
                    if (!ReadXMLFile.finalIndex.get(sReturn).containsKey(id)) {
                        countOfData temp = new countOfData();
                        temp.infobox_count++;
                        ReadXMLFile.finalIndex.get(sReturn).put(id, temp);
                    } else {
                        ReadXMLFile.finalIndex.get(sReturn).get(id).infobox_count++;
                    }
                } else {
                    countOfData temp = new countOfData();
                    temp.infobox_count++;
                    HashMap<String, countOfData> tempHM = new HashMap<>();
                    tempHM.put(id, temp);
                    ReadXMLFile.finalIndex.put(sReturn, tempHM);
                }
                //System.out.println(sReturn);
            }
        } catch (Exception e) {
            return;
        }
    }

    public void removeStopWordAndStemExternalLinks(String s, String id) {
        try {
            Stemmer objStemmer = new Stemmer();
            if (!stopWordHM.contains(s)) {
                objStemmer.add(s.toCharArray(), s.length());
                String sReturn = objStemmer.stem();
                //------------------------ put value in hashMap----------------------//

                if (ReadXMLFile.finalIndex.containsKey(sReturn)) {
                    if (!ReadXMLFile.finalIndex.get(sReturn).containsKey(id)) {
                        countOfData temp = new countOfData();
                        temp.external_link_count++;
                        ReadXMLFile.finalIndex.get(sReturn).put(id, temp);
                    } else {
                        ReadXMLFile.finalIndex.get(sReturn).get(id).external_link_count++;
                    }
                } else {
                    countOfData temp = new countOfData();
                    temp.external_link_count++;
                    HashMap<String, countOfData> tempHM = new HashMap<>();
                    tempHM.put(id, temp);
                    ReadXMLFile.finalIndex.put(sReturn, tempHM);
                }
                //System.out.println(sReturn);
            }
        } catch (Exception e) {
            return;
        }
    }
}
