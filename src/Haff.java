import java.util.*;

public class Haff {
    private String originString;
    private Map<Character, Double> origin = new TreeMap<>();
    private Map<Character, StringBuilder> haffCod = new TreeMap<>();
    Data[] data;
    class Data{
        private char symbol;
        private double probability;

        Data(char s, double p){
            symbol = s;
            probability = p;
        }
    }

    private class Struct{
        private StringBuilder symbol = new StringBuilder();
        private double probability;
        private StringBuilder code;

        Struct(char s, double p){
            symbol.append(s);
            probability = p;
        }

        Struct(Struct struct){
            symbol = new StringBuilder(struct.symbol);
            probability = struct.probability;
        }

        private void merge(Struct second){
            symbol.append(second.symbol);
            probability += second.probability;
        }
    }

    Haff(String os){
        originString = os.toLowerCase(Locale.ENGLISH).trim();
        for(int i = 0; i < originString.length(); i++){
            if(origin.containsKey(originString.charAt(i))){
                origin.put(originString.charAt(i), origin.get(originString.charAt(i)) + 1);
            }
            else{
                origin.put(originString.charAt(i), 1d);
            }
        }
        Iterator<Map.Entry<Character, Double>> it = origin.entrySet().iterator();
        Map.Entry<Character, Double> entry;
        data = new Data[origin.size()];
        for (int i = 0; it.hasNext(); i++){
            entry = it.next();
            data[i] = new Data(entry.getKey(), entry.getValue());
        }

        Comparator<Data> comparator = Comparator.comparing(obj -> obj.probability);
        Arrays.sort(data, comparator.reversed());

        for(int i = 0; i < data.length; i++){
            data[i].probability /= originString.length();
        }

        Vector<Struct> vector = new Vector<>();
        for(int i = 0; i < data.length; i++) {
            vector.add(new Struct(data[i].symbol, data[i].probability));
        }
        makeHaff(vector);
        for(Struct temp: vector){
            haffCod.put(temp.symbol.charAt(0), temp.code);
        }
    }


    private void makeHaff(Vector<Struct> originVector){
        Comparator<Struct> comparator = Comparator.comparing(obj->obj.probability);
        originVector.sort(comparator.reversed());
        if(originVector.size() == 2){
            originVector.get(0).code = new StringBuilder("0");
            originVector.get(1).code = new StringBuilder("1");
        }
        else{
            Vector<Struct> vector = new Vector<>();
            for(Struct tempStruct: originVector){
                vector.add(new Struct(tempStruct));
            }
            vector.get(vector.size() - 2).merge(vector.get(vector.size() - 1));
            vector.remove(vector.size() - 1);
            makeHaff(vector);
            for(Struct tempOrigin: originVector){
                for(Struct temp: vector){
                    if(temp.symbol.indexOf(tempOrigin.symbol.toString()) != -1){
                        tempOrigin.code = new StringBuilder(temp.code);
                        break;
                    }
                }
            }
            originVector.get(originVector.size() - 1).code.append(1);
            originVector.get(originVector.size() - 2).code.append(0);
        }
    }

    private double midLength(Map<Character, StringBuilder> map){
        double result = 0;
        for(int i = 0; i < data.length; i++){
            result += map.get(data[i].symbol).length() * data[i].probability;
        }
        return result;
    }

    public double entropie(){
        double res = 0;
        for(int i = 0; i < data.length; i++){
            res += -(Math.log(data[i].probability) / Math.log(2)) * data[i].probability;
        }
        return res;
    }

    public double getLForHaff(){
        return midLength(haffCod);
    }

    public void printHaffCode(){
        for(int i = 0; i < originString.length(); i++){
            System.out.print(haffCod.get(originString.charAt(i)));
            if(i % 35 == 0){
                System.out.println();
            }
        }
    }
}