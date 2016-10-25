/**
 * Created by ilya on 10/24/16.
 */
public interface Function1<Return, Param> {
    Return apply(Param xw);

    default <R> Function1<R, Param> compose(Function1<? extends R, ? super Return> g) {
        return param -> g.apply(apply(param));
    }
}
