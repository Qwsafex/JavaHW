import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by ilya on 10/25/16.
 */
public class Function2Test {
    @Test
    public void compose() throws Exception {
        Function2<Integer, Integer, Integer> multiply = (param1, param2) -> param1 * param2;
        Function1<Integer, Integer> add2 = param -> param + 2;
        Function2<Integer, Integer, Integer> multiplyAdd3 = multiply.compose(add2);
        assertEquals(Integer.valueOf(12), multiplyAdd3.apply(2,5));
    }

    @Test
    public void bind1() throws Exception {
        Function2<Integer, Integer, Integer> multiply = (param1, param2) -> param1 * param2;
        Function1<Integer, Integer> multiplyBy2 = multiply.bind1(2);
        assertEquals(Integer.valueOf(12), multiplyBy2.apply(6));
    }

    @Test
    public void bind2() throws Exception {
        Function2<Integer, Integer, Integer> multiply = (param1, param2) -> param1 * param2;
        Function1<Integer, Integer> multiplyBy2 = multiply.bind2(2);
        assertEquals(Integer.valueOf(12), multiplyBy2.apply(6));
    }

    @Test
    public void curry() throws Exception {
        Function2<Integer, Integer, Integer> multiply = (param1, param2) -> param1 * param2;
        Function1<Integer, Integer> multiplyBy2 = multiply.curry().apply(2);
        assertEquals(Integer.valueOf(12), multiplyBy2.apply(6));

    }

}