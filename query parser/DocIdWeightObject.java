import java.util.Comparator;

public class DocIdWeightObject {

    String id;
    int weight;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }


    //Comparator
    public static Comparator<DocIdWeightObject> WeightComparator = new Comparator<DocIdWeightObject>(){

        @Override
        public int compare(DocIdWeightObject o1, DocIdWeightObject o2) {
            if( o1.getWeight() > o2.getWeight() ){
                return -1;
            }else if(o1.getWeight() < o2.getWeight()){
                return 1;
            }

            return 0;
        }
    };
}
