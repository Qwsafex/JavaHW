import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by ilya on 10/25/16.
 */
public class PredicateTest {
    @Test
    public void testConstants(){
        assertEquals(Boolean.TRUE, Predicate.ALWAYS_TRUE.apply(new Object()));
        assertEquals(Boolean.FALSE, Predicate.ALWAYS_FALSE.apply(new Object()));
    }
    @Test
    public void not() throws Exception {
        Predicate<Integer> is3 = param -> param == 3;
        assertTrue(is3.apply(3));
        assertFalse(is3.apply(1));
        assertFalse(is3.apply(2));
        assertFalse(is3.apply(4));
        Predicate<Integer> isNot3 = is3.not();
        assertFalse(isNot3.apply(3));
        assertTrue(isNot3.apply(1));
        assertTrue(isNot3.apply(2));
        assertTrue(isNot3.apply(4));
    }

    @Test
    public void or() throws Exception {
        Predicate<Integer> is3 = param -> param == 3;
        Predicate<Integer> is5 = param -> param == 5;
        Predicate<Integer> is3or5 = is3.or(is5);
        assertTrue(is3or5.apply(3));
        assertTrue(is3or5.apply(5));
        assertFalse(is3or5.apply(2));
        assertFalse(is3or5.apply(4));
    }

    @Test
    public void and() throws Exception {
        Predicate<Integer> less6 = param -> param < 6;
        Predicate<Integer> greater2 = param -> param > 2;
        Predicate<Integer> between3and5 = less6.and(greater2);
        for (int i = 3; i <= 5; i++) {
            assertTrue(between3and5.apply(i));
        }
        assertFalse(between3and5.apply(1));
        assertFalse(between3and5.apply(2));
        assertFalse(between3and5.apply(100));
        assertFalse(between3and5.apply(-1));
    }

}