
import java.util.Comparator;

public class comparator implements Comparator <newdata>{
    public int compare(newdata a, newdata b){
            return a.word.compareTo(b.word);
    }
}