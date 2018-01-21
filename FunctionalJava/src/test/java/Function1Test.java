import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by ilya on 10/25/16.
 */
public class Function1Test {
    @Test
    public void compose() throws Exception {
        Function1<Integer, Integer> square = param -> param * param;
        Function1<Integer, Integer> add3 = param -> param + 3;
        Function1<Integer, Integer> sqrAdd3 = square.compose(add3);

        assertEquals(Integer.valueOf(12), sqrAdd3.apply(3));
    }

}