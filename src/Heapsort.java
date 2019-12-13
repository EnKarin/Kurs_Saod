import java.util.ArrayList;

class HeapSort {
   
    static void heapify(int[] array, ArrayList<Struct> list, int length, int i) {
    int leftChild = 2*i+1;
    int rightChild = 2*i+2;
    int largest = i;

    if (leftChild < length && list.get(array[leftChild]).getLaw().compareTo(list.get(array[largest]).getLaw()) > 0) {
        largest = leftChild;
    }

    if(leftChild < length && list.get(array[leftChild]).getLaw().compareTo(list.get(array[largest]).getLaw()) == 0){
        if (list.get(array[leftChild]).getSum() >= list.get(array[largest]).getSum()) {
            largest = leftChild;
        }
    }

    if (rightChild < length && list.get(array[rightChild]).getLaw().compareTo(list.get(array[largest]).getLaw()) > 0) {
        largest = rightChild;
    }

    if(rightChild < length && list.get(array[rightChild]).getLaw().compareTo(list.get(array[largest]).getLaw()) == 0){
        if (list.get(array[rightChild]).getSum() >= list.get(array[largest]).getSum()) {
            largest = rightChild;
        }
    }

    if (largest != i) {
        int temp = array[i];
        array[i] = array[largest];
        array[largest] = temp;
        heapify(array, list, length, largest);
    }
}

    public static void heapSort(int[] array, ArrayList<Struct> list) {
        if (array.length == 0) return;

        int length = array.length;
        for (int i = length / 2 - 1; i >= 0; i--)
            heapify(array, list, length, i);

        for (int i = length - 1; i >= 0; i--) {
            int temp = array[0];
            array[0] = array[i];
            array[i] = temp;

            heapify(array, list, i, 0);
        }
    }
}