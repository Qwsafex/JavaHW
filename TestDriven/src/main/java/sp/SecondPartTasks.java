package sp;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class SecondPartTasks {

    private SecondPartTasks() {}

    // Найти строки из переданных файлов, в которых встречается указанная подстрока.
    public static List<String> findQuotes(List<String> paths, CharSequence sequence) {
        return paths.stream()
                    .flatMap(path -> {
                        try{
                            return Files.lines(Paths.get(path));
                        }
                        catch (IOException e) {
                            return null;
                        }
                    })
                    .filter(line -> line.contains(sequence))
                    .collect(Collectors.toList());
    }

    // В квадрат с длиной стороны 1 вписана мишень.
    // Стрелок атакует мишень и каждый раз попадает в произвольную точку квадрата.
    // Надо промоделировать этот процесс с помощью класса java.util.Random и посчитать, какова вероятность попасть в мишень.
    public static double piDividedBy4() {
        final int ITERATIONS = 1000000;
        Random random = new Random();
        double radius = 0.5;
        return IntStream
                .iterate(1, n -> n + 1)
                .limit(ITERATIONS)
                .filter(i -> {
                    double x = random.nextDouble() * 2 * radius - radius;
                    double y = random.nextDouble() * 2 * radius - radius;
                    return x * x + y * y < radius * radius;
                })
                .count() / (.0 + ITERATIONS);
    }

    // Дано отображение из имени автора в список с содержанием его произведений.
    // Надо вычислить, чья общая длина произведений наибольшая.
    public static String findPrinter(Map<String, List<String>> compositions) {
        return compositions.entrySet()
                           .stream()
                           .max(Comparator.comparing(entry -> entry.getValue()
                                                                   .stream()
                                                                   .mapToInt(String::length)
                                                                   .sum())).get().getKey();
    }

    // Вы крупный поставщик продуктов. Каждая торговая сеть делает вам заказ в виде Map<Товар, Количество>.
    // Необходимо вычислить, какой товар и в каком количестве надо поставить.
    public static Map<String, Integer> calculateGlobalOrder(List<Map<String, Integer>> orders) {
        return orders.stream().reduce(new HashMap<String, Integer>(),
                (map1, map2) -> {
                    map2.forEach((key, val) -> {
                        map1.compute(key, (k,v) -> (v == null) ? val : val + v);
                    });
                    return map1;
                });
    }
}
