import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

// метод для вычисления произведения элементов
    // метод для вычисления суммы элементов
    // метод для вычисления среднего значения
    // метод для вывода отсортированного массива

    // реализовать в виде внутр. методов, методов из классов,
    // из других java файлов и классов-потоков

class Result_2 extends Thread {
    private final Result numbers;
    private final String math_oper;

    Result_2(Result numbers, String math_oper) {
        this.math_oper = math_oper;
        this.numbers = numbers;
    }

    public void run() {
        System.out.println("DEBUG: Поток запущен - " + math_oper);
        switch(math_oper) {
            case "sum":
                System.out.println("Сумма элементов: " + numbers.getSum());
                break;
            case "product":
                System.out.println("Произведение элементов: " + numbers.getProduct());
                break;
            case "sort":
                System.out.println("Отсортированный список: " + numbers.getSortedList());
                break;
            case "average":
                System.out.println("Среднее значение списка: " + numbers.getAverage());
                break;
            case "size":
                System.out.println("Размер списка: " + numbers.getSize());
                break;
            default:
                for(int num : numbers) {
                    System.out.println(num);
                }
                System.out.print('\n');
        }
        System.out.println("DEBUG: Поток завершён - " + math_oper);
    }
}


class Result extends ArrayList<Integer> {  // не парился и импортировал get структуры из класса ArrayList
    private ArrayList<Integer> numbers;    // не final, т.к. есть сеттер
    //private Semaphore semaphore = new Semaphore(1);
    private final ReentrantLock lock = new ReentrantLock();

    Result() {
        this.numbers = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        //super.addAll(Arrays.asList(1, 2, 3, 4, 5));
    }

    Result(ArrayList<Integer> numbers) {
        this.numbers = numbers;
        //super.addAll(numbers);
    }

    @Override
    public void forEach(Consumer<? super Integer> action) {
        if (numbers != null) {
            numbers.forEach(action);
        } else {
            System.out.println("Список пуст.");
        }
    }

    @Override
    public synchronized Integer get(int index) {
        return numbers.get(index);
        //return this.get(index);
    }

    public synchronized void set(ArrayList<Integer> numbers) {
        this.numbers = numbers;
    }

    public synchronized int getSize() {
        return numbers.size();
    }

    public int getProduct() {
        int product = 1;
        try {
            //semaphore.acquire();
            lock.lock();
            /*
            for (int num : numbers) {
                product *= num;
            }
            */
            return numbers.stream().reduce(1, (a, b) -> a * b);
        }

        // блок catch (InterruptedException e) { e.printStackTrace(); }
        // не нужен при реализации
        /*
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        */

        finally {
            lock.unlock();
            //semaphore.release();
        }
        //return product;
    }

    public int getSum() {
        int sum = 0;
        try {
            lock.lock();
            /*
            for (int num : numbers) {
                sum += num;
            }
            */

            return numbers.stream().reduce(0, (a, b) -> a + b);
        }
        finally {
            lock.unlock();
        }

        //return sum;
    }

    public double getAverage() {
        try {
            lock.lock();
            double avg = numbers.isEmpty() ? 0 : (double) getSum() / numbers.size();
            System.out.println("DEBUG: Среднее значение вычислено: " + avg);
            return avg;
        }
        finally {
            lock.unlock();
        }
    }

    public ArrayList<Integer> getSortedList() {
        try {
            lock.lock();
            ArrayList<Integer> sortedList = new ArrayList<>(numbers);
            Collections.sort(sortedList);
            return sortedList;
        }
        finally {
            lock.unlock();
        }
    }
}


public class Main {
    public static void main(String[] args) {
        ArrayList<Integer> numbers = new ArrayList<>(Arrays.asList(12, 15, 37, -23, 42));
        numbers.addAll(Arrays.asList(12, 12));

        Result result1 = new Result(numbers);
        System.out.println(result1.getProduct());  //-926432640

        Result result2 = new Result();
        result2.getSortedList();  //

        Result_OOP result3 = new Result_OOP();

        System.out.println("Это ООП вывод:");
        for (int num : result2) {
            System.out.println(num);
        }
        System.out.println("ООП вывод завершен" + '\n');
        /*
        for (int i = 0; i < result1.getSize(); i++) {
            System.out.println(result1.get(i));
        }
        */

        //result1.forEach(num -> System.out.println(num));
        System.out.println("Проверка:");
        result1.forEach(num -> System.out.println("Элемент: " + num));



        for (int i = 0; i < result2.getSize(); i++) {
            int finalI = i;  // костыль из-за необходимости использования неизм. переменной в лямбда
            Runnable task = () -> System.out.println(result2.get(finalI));
            task.run();
        }

        ExecutorService executor = Executors.newFixedThreadPool(4);
        executor.submit(() -> System.out.println("Сумма: " + result1.getSum()));
        executor.submit(() -> System.out.println("Произведение: " + result1.getProduct()));
        executor.submit(() -> System.out.println("Среднее значение: " + result1.getAverage()));
        executor.submit(() -> System.out.println("Отсортированный список: " + result1.getSortedList()));

        executor.shutdown();

        /*
        Result_2 thread1 = new Result_2(result1, "sum");
        Result_2 thread2 = new Result_2(result2, "sum");
        Result_2 thread3 = new Result_2(result1, "sort");
        Result_2 thread4 = new Result_2(result2, "average");
        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
        // Ждем завершения потоков
        try {
            thread1.join(5000);  // Ждать не больше 0.5 секунд
            thread2.join(5000);
            thread3.join(5000);
            thread4.join(5000);
        } catch (InterruptedException e) {
            System.err.println("Ожидание потоков прервано");
            e.printStackTrace();
        }
        System.out.println("Все потоки завершены. Программа завершает работу.");
        // Вывод информации о потоках
        Thread.getAllStackTraces().keySet().forEach(thread ->
            System.out.println(thread.getName() + " (Daemon: " + thread.isDaemon() + ", State: " + thread.getState() + ")")
        );
        */
    }
}
