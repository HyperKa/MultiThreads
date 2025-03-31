import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.locks.ReentrantLock;

public class Result_OOP {
    private ArrayList<Integer> numbers;    // не final, т.к. есть сеттер
    //private Semaphore semaphore = new Semaphore(1);

    Result_OOP() {
        this.numbers = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        //super.addAll(Arrays.asList(1, 2, 3, 4, 5));
    }

    Result_OOP(ArrayList<Integer> numbers) {
        this.numbers = numbers;
        //super.addAll(numbers);
    }

    public Integer get(int index) {
        return numbers.get(index);
        //return this.get(index);
    }

    public void set(ArrayList<Integer> numbers) {
        this.numbers = numbers;
    }

    public int getSize() {
        return numbers.size();
    }

    public int getProduct() {
        int product = 1;
        for (int num : numbers) {
            product *= num;
        }
        return product;
    }

    public int getSum() {
        int sum = 0;

        for (int num : numbers) {
            sum += num;
        }
        return sum;
    }

    public double getAverage() {
        double avg = numbers.isEmpty() ? 0 : (double) getSum() / numbers.size();
        System.out.println("DEBUG: Среднее значение вычислено: " + avg);
        return avg;
    }

    public ArrayList<Integer> getSortedList() {
        ArrayList<Integer> sortedList = new ArrayList<>(numbers);
        Collections.sort(sortedList);
        return sortedList;

    }
}
