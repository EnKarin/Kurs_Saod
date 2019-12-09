import java.io.*;
import java.nio.charset.Charset;
import java.util.*;

public class Main {

    public static void main(String[] args) throws Exception{
        Random rand = new Random();
        Scanner scan = new Scanner(System.in);
        File file = new File("testBase3.dat");
        ArrayList<Struct> list = new ArrayList<>();
        byte[][] b = new byte[3][30];
        DataInputStream dat = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
        while(dat.available() > 0){
            for(int i = 0; i < 30; i++){
                b[0][i] = dat.readByte();
            }
            int x = dat.readUnsignedShort();
            for(int i = 0; i < 10; i++){
                b[1][i] = dat.readByte();
            }
            for(int i = 0; i < 22; i++){
                b[2][i] = dat.readByte();
            }
            String a = new String(b[0],0,30, Charset.forName("CP866"));
            list.add(new Struct(a, x, new String(b[1],0,22, Charset.forName("CP866")), new String(b[2],0,30, Charset.forName("CP866"))));
        }

        int[] indexMas = new int[list.size()];
        for(int i = 0; i < list.size(); i++){
            indexMas[i] = i;
        }
        HeapSort.heapSort(indexMas, list);
        DOP dop = new DOP();
        boolean flag = true;
        while(flag) {
            System.out.println("Введите команду:");
            System.out.println("sort для вывода отсортированной базы");
            System.out.println("origin для вывода неотсортированной базы");
            System.out.println("search для поиска по ключу");
            System.out.println("close для выхода из программы");
            switch (scan.next()) {
                case "sort":
                    for (int i = 0, j = 20; i < list.size(); i++, j--) {
                        if (j == 0) {
                            System.out.println("Введите + для продолжения вывода");
                            if (scan.next().charAt(0) == '+') {
                                j = 20;
                            } else break;
                        }
                        System.out.print(i + 1 + ". ");
                        list.get(indexMas[i]).print();
                    }
                    break;
                case "origin":
                    for (int i = 0, j = 20; i < list.size(); i++, j--) {
                        if (j == 0) {
                            System.out.println("Введите + для продолжения вывода");
                            if (scan.next().charAt(0) == '+') {
                                j = 20;
                            } else break;
                        }
                        System.out.print(i + 1 + ". ");
                        list.get(i).print();
                    }
                    break;
                case "search":
                    System.out.print("Введите первые три буквы фамилии адвоката: ");
                    String key = scan.next().toLowerCase();
                    ArrayDeque<Struct> structArrayDeque = new ArrayDeque<>();
                    int l = 0, r = list.size() - 1;
                    for(int i = l + (r - l) / 2; l < r; i = l + (r - l) / 2){
                        if(list.get(indexMas[i]).getLaw().substring(0, 3).toLowerCase().compareTo(key) < 0){
                            l = i + 1;
                        }
                        else {
                            r = i;
                        }
                    }
                    if(list.get(indexMas[r]).getLaw().substring(0, 3).toLowerCase().equals(key)){
                        structArrayDeque.add(list.get(indexMas[r]));
                        for(int i = r + 1; i < list.size() && list.get(indexMas[i]).getLaw().substring(0, 3)
                                .toLowerCase().equals(key); i++){
                            structArrayDeque.add(list.get(indexMas[i]));
                        }
                        Iterator<Struct> it = structArrayDeque.iterator();
                        Data[] data = new Data[structArrayDeque.size()];
                        boolean forAdd;
                        Struct str;
                        for(int i = 0; it.hasNext();){
                            str = it.next();
                            forAdd = true;
                            for(int j = 0; j < i && forAdd; j++){
                                if(str.getFio().equals(data[j].getKey())){
                                    data[j].add(str);
                                    forAdd = false;
                                }
                            }
                            if(forAdd){
                                data[i] = new Data(rand.nextInt(structArrayDeque.size() * 10), str);
                                i++;
                            }
                        }
                        int lenght = 0;
                        for(int i = 0; i < data.length; i++){
                            if(data[i] == null){
                                lenght = i;
                                break;
                            }
                        }
                        Data[] newData = new Data[lenght];
                        for(int i = 0; i < lenght; i++){
                            newData[i] = data[i];
                        }
                        dop.addA2(newData);
                        boolean stop = true;
                        while (stop) {
                            System.out.println("Введите search для поиска по найденным записям по ФИО");
                            System.out.println("printSpis для вывода cписка записей ");
                            System.out.println("printTree для вывода дерева найденных записей");
                            System.out.println("или break для выхода в главное меню");
                            switch (scan.next()) {
                                case "search":
                                    System.out.println("Введите ФИО вкладчика");
                                    scan.nextLine();
                                    String search = scan.nextLine().toLowerCase();
                                    ArrayDeque<Struct> temp = dop.search(search.trim());
                                    if(temp == null){
                                        System.out.println("Записи с таким ключом нет");
                                    }
                                    else{
                                        for(Struct t: temp){
                                            t.print();
                                        }
                                    }
                                    break;
                                case "printTree":
                                    dop.printLeft();
                                    break;
                                case "printSpis":
                                    for(Struct struct: structArrayDeque){
                                        struct.print();
                                    }
                                    break;
                                case "break":
                                    stop = false;
                                    break;
                            }
                        }
                    }
                    else {
                        System.out.println("Записи с таким ключом нет");
                    }

                    break;
                case "close":
                    flag = false;
                    break;
            }
        }

    }
}

class Struct{
    private String fio;
    private int sum;
    private String dat;
    private String law;

    public Struct(String fio, int sum, String dat, String law){
        this.fio = fio;
        this.sum = sum;
        this.dat = dat;
        this.law = law;
    }

    public void print(){
        System.out.print("ФИО вкладчика: " + fio);
        System.out.print("  Сумма вклада: " + sum);
        System.out.print("  Дата вклада: " + dat);
        System.out.print("  ФИО адвоката: " + law);
        System.out.println();
    }

    public String getLaw(){
        return law;
    }

    public String getFio(){
        return fio;
    }

    public int getSum(){
        return sum;
    }
}
