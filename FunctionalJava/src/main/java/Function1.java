/**
 * Created by ilya on 10/24/16.
 */
public interface Function1<Param, Return> {
    Return apply(Param xw);

    default <R> Function1<Param, R> compose(Function1<? super Return, ? extends R> g) {
        return param -> g.apply(apply(param));
    }
}
