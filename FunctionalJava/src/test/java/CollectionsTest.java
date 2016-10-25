import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by ilya on 10/25/16.
 */
public class CollectionsTest {
    @Test
    public void mapSimple() throws Exception {
        List<Integer> array = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> squaredArray = Collections.map(new Function1<Integer, Integer>() {
            @Override
            public Integer apply(Integer xw) {
                return xw*xw;
            }
        }, array);
        assertEquals(Arrays.asList(1, 4, 9, 16, 25), squaredArray);
    }

    @Test
    public void mapEmpty() throws Exception {
        List<Integer> array = new ArrayList<>();
        List<Integer> mappedArray = Collections.map(new Function1<Integer, Integer>() {
            @Override
            public Integer apply(Integer xw) {
                return xw*xw;
            }
        }, array);
        assertEquals(new ArrayList<Integer>(), mappedArray);
    }

    @Test
    public void filterSimple() throws Exception {
        List<Integer> array = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        List<Integer> filteredArray = Collections.filter(new Predicate<Integer>() {
            @Override
            public Boolean apply(Integer xw) {
                return xw % 2 == 0;
            }
        }, array);
        assertEquals(Arrays.asList(2, 4, 6, 8, 10), filteredArray);
    }

    @Test
    public void filterEmpty() throws Exception {
        List<Integer> array = new ArrayList<>();
        List<Integer> filteredArray = Collections.filter(new Predicate<Integer>() {
            @Override
            public Boolean apply(Integer xw) {
                return xw % 2 == 0;
            }
        }, array);
        assertEquals(new ArrayList<Integer>(), filteredArray);
    }


    @Test
    public void takeWhile() throws Exception {
        List<Integer> array = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        List<Integer> takenArray = Collections.takeWhile(new Predicate<Integer>() {
            @Override
            public Boolean apply(Integer xw) {
                return xw <= 5;
            }
        }, array);
        assertEquals(Arrays.asList(1, 2, 3, 4, 5), takenArray);
    }

    @Test
    public void takeWhileEmpty() throws Exception {
        List<Integer> array = new ArrayList<>();
        List<Integer> takenArray = Collections.takeWhile(new Predicate<Integer>() {
            @Override
            public Boolean apply(Integer xw) {
                return xw <= 5;
            }
        }, array);
        assertEquals(new ArrayList<Integer>(), takenArray);
    }


    @Test
    public void takeUnless() throws Exception {
        List<Integer> array = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        List<Integer> takenArray = Collections.takeUnless(new Predicate<Integer>() {
            @Override
            public Boolean apply(Integer xw) {
                return xw > 5;
            }
        }, array);
        assertEquals(Arrays.asList(1, 2, 3, 4, 5), takenArray);
    }

    @Test
    public void takeUnlessEmpty() throws Exception {
        List<Integer> array = new ArrayList<>();
        List<Integer> takenArray = Collections.takeUnless(new Predicate<Integer>() {
            @Override
            public Boolean apply(Integer xw) {
                return xw > 5;
            }
        }, array);
        assertEquals(new ArrayList<Integer>(), takenArray);
    }

    @Test
    public void foldlSimpty() throws Exception {
        List<Integer> array = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        int result = Collections.foldl(new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer x, Integer y) {
                return x + y;
            }
        }, 0, array);
        assertEquals(55, result);
    }
    @Test
    public void foldlEmpty() throws Exception {
        List<Integer> array = new ArrayList<>();
        int result = Collections.foldl(new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer x, Integer y) {
                return x + y;
            }
        }, 0, array);
        assertEquals(0, result);
    }
    @Test
    public void foldrSimpty() throws Exception {
        List<Integer> array = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        int result = Collections.foldr(new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer x, Integer y) {
                return x + y;
            }
        }, 0, array);
        assertEquals(55, result);
    }
    @Test
    public void foldrEmpty() throws Exception {
        List<Integer> array = new ArrayList<>();
        int result = Collections.foldr(new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer x, Integer y) {
                return x + y;
            }
        }, 0, array);
        assertEquals(0, result);
    }

}