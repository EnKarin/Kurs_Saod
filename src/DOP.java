import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Comparator;

public class DOP{

    private Element root = null;
    private int weightTree = 0;

    class Element {
        private ArrayDeque<Struct> data;
        private int weight;
        private Element left, right;

        private Element(Data dat, Element left, Element right){
            data = dat.getValue();
            weight = dat.getWeight();
            this.left = left;
            this.right = right;
        }

        private void printLeft(){
            if(left != null){
                left.printLeft();
            }
            for(Struct temp: data){
                temp.print();
            }
            if (right != null) {
                right.printLeft();
            }
        }
    }

    private void setWeightTree(Data[] data){
        for(int i = 0; i < data.length; i++){
            weightTree += data[i].getWeight();
        }
    }

    void addA2(Data[] data){
        setWeightTree(data);
        Comparator<Data> comparator = Comparator.comparing(obj -> obj.getValue().getFirst().getFio());
        Arrays.sort(data, comparator);
        root = localAdd(data, 0, data.length);
    }

    private Element localAdd(Data[] data, int first, int second){
        if(first == second){
            return null;
        }
        int sumL = 0, sumR = 0;
        for(int i = first; i < second; i++){
            sumR += data[i].getWeight();
        }
        for(int i = first; i < second - 1; i++, sumL += data[i - 1].getWeight(), sumR -= data[i].getWeight()) {
            if(sumL >= sumR) {
                return new Element(data[i], localAdd(data, first, i), localAdd(data, i + 1, second));
            }
        }
        return new Element(data[second - 1], localAdd(data, first, second - 1), null);
    }

    public ArrayDeque<Struct> search(String key){
        Element now = this.root;
        while(now != null && !now.data.getFirst().getFio().trim().toLowerCase().equals(key)) {
            if (now.data.getFirst().getFio().trim().toLowerCase().compareTo(key) > 0) {
                now = now.left;
            } else if (now.data.getFirst().getFio().trim().toLowerCase().compareTo(key) < 0) {
                now = now.right;
            }
        }
        return (now == null? null: now.data);
    }

    void printLeft(){
        if(root != null) {
            root.printLeft();
        }
        else{
            System.out.println("Tree is empty!");
        }
    }
}

class Data {
    private int weight;
    private ArrayDeque<Struct> value;
    private String key;

    Data(int w, Struct v) {
        weight = w;
        value = new ArrayDeque<>();
        value.add(v);
        key = v.getFio();
    }

    public ArrayDeque<Struct> getValue() {
        return value;
    }

    public void add(Struct arc) {
        value.add(arc);
    }

    public int getWeight() {
        return weight;
    }

    public String getKey() {
        return key;
    }
}
