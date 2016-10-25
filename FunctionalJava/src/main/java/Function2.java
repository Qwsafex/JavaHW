/**
 * Created by ilya on 10/25/16.
 */
public interface Function2<Return, Param1, Param2> {
    Return apply(Param1 x, Param2 y);

    default <R> Function2<R, Param1, Param2> compose(Function1<? extends R, ? super Return> g){
        return (param1, param2) -> g.apply(apply(param1, param2));
    }
    default Function1<Return, Param2> bind1(Param1 param1){
        return param2 -> apply(param1, param2);
    }
    default Function1<Return, Param1> bind2(Param2 param2){
        return param1 -> apply(param1, param2);
    }
    default Function1<Function1<Return, Param2>, Param1> curry(){
        return this::bind1;
    }
}

