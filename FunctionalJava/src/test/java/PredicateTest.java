import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by ilya on 10/25/16.
 */
public class PredicateTest {
    private static final Predicate<Object> UNDEFINED = x -> {throw new IllegalStateException();};
    private static final Predicate<Integer> IS3 = x -> x == 3;
    @Test
    public void testConstants(){
        assertEquals(Boolean.TRUE, Predicate.ALWAYS_TRUE.apply(new Object()));
        assertEquals(Boolean.FALSE, Predicate.ALWAYS_FALSE.apply(new Object()));
    }
    @Test
    public void not() throws Exception {
        assertTrue(IS3.apply(3));
        assertFalse(IS3.apply(1));
        assertFalse(IS3.apply(2));
        assertFalse(IS3.apply(4));
        Predicate<Integer> isNot3 = IS3.not();
        assertFalse(isNot3.apply(3));
        assertTrue(isNot3.apply(1));
        assertTrue(isNot3.apply(2));
        assertTrue(isNot3.apply(4));
    }

    @Test
    public void or() throws Exception {
        Predicate<Integer> is5 = param -> param == 5;
        Predicate<Integer> is3or5 = IS3.or(is5);
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

    @Test
    public void orLaziness() {
        Predicate<Integer> is3orThrow = IS3.or(UNDEFINED);
        boolean thrown = false;
        try{
            is3orThrow.apply(4);
        }
        catch (Exception e){
            thrown = true;
        }
        finally {
            assertTrue(thrown);
        }
        assertTrue(is3orThrow.apply(3));
    }

    @Test
    public void andLaziness() {
        Predicate<Integer> isNot3orThrow = IS3.and(UNDEFINED);
        boolean thrown = false;
        try{
            isNot3orThrow.apply(3);
        }
        catch (Exception e){
            thrown = true;
        }
        finally {
            assertTrue(thrown);
        }
        assertFalse(isNot3orThrow.apply(4));
    }
}