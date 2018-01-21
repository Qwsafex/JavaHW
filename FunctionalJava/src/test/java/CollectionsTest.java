import org.junit.Test;

import java.util.*;
//import java.util.Collections;

import static org.junit.Assert.assertEquals;

/**
 * Created by ilya on 10/25/16.
 */
public class CollectionsTest {
    @Test
    public void mapSimple() throws Exception {
        List<Integer> array = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> squaredArray = Collections.map(xw -> xw*xw, array);
        assertEquals(Arrays.asList(1, 4, 9, 16, 25), squaredArray);
    }

    @Test
    public void mapEmpty() throws Exception {
        List<Integer> array = java.util.Collections.emptyList();
        List<Integer> mappedArray = Collections.map(xw -> xw*xw, array);
        assertEquals(java.util.Collections.emptyList(), mappedArray);
    }

    @Test
    public void filterSimple() throws Exception {
        List<Integer> array = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        List<Integer> filteredArray = Collections.filter(xw -> xw % 2 == 0, array);
        assertEquals(Arrays.asList(2, 4, 6, 8, 10), filteredArray);
    }

    @Test
    public void filterEmpty() throws Exception {
        List<Integer> array = java.util.Collections.emptyList();
        List<Integer> filteredArray = Collections.filter(xw -> xw % 2 == 0, array);
        assertEquals(java.util.Collections.emptyList(), filteredArray);
    }


    @Test
    public void takeWhile() throws Exception {
        List<Integer> array = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        List<Integer> takenArray = Collections.takeWhile(xw -> xw <= 5, array);
        assertEquals(Arrays.asList(1, 2, 3, 4, 5), takenArray);
    }

    @Test
    public void takeWhileEmpty() throws Exception {
        List<Integer> array = java.util.Collections.emptyList();
        List<Integer> takenArray = Collections.takeWhile(xw -> xw <= 5, array);
        assertEquals(java.util.Collections.emptyList(), takenArray);
    }


    @Test
    public void takeUnless() throws Exception {
        List<Integer> array = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        List<Integer> takenArray = Collections.takeUnless(xw -> xw > 5, array);
        assertEquals(Arrays.asList(1, 2, 3, 4, 5), takenArray);
    }

    @Test
    public void takeUnlessEmpty() throws Exception {
        List<Integer> array = java.util.Collections.emptyList();
        List<Integer> takenArray = Collections.takeUnless(xw -> xw > 5, array);
        assertEquals(java.util.Collections.emptyList(), takenArray);
    }

    @Test
    public void foldlSimple() throws Exception {
        List<String> array = Arrays.asList("1", "2", "3", "4", "5");
        String result = Collections.foldl(String::concat, "0", array);
        assertEquals("012345", result);
    }
    @Test
    public void foldlEmpty() throws Exception {
        List<Integer> array = java.util.Collections.emptyList();
        int result = Collections.foldl((x, y) -> x + y, 0, array);
        assertEquals(0, result);
    }
    @Test
    public void foldrSimple() throws Exception {
        List<String> array = Arrays.asList("1", "2", "3", "4", "5");
        String result = Collections.foldr(String::concat, "0", array);
        assertEquals("123450", result);
    }
    @Test
    public void foldrEmpty() throws Exception {
        List<Integer> array = java.util.Collections.emptyList();
        int result = Collections.foldr((x, y) -> x + y, 0, array);
        assertEquals(0, result);
    }

}