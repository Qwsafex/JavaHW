/**
 * Created by ilya on 10/25/16.
 */
public interface Function2<Param1, Param2, Return> {
    Return apply(Param1 x, Param2 y);

    default <R> Function2<Param1, Param2, R> compose(Function1<? super Return, ? extends R> g){
        return (param1, param2) -> g.apply(apply(param1, param2));
    }
    default Function1<Param2, Return> bind1(Param1 param1){
        return param2 -> apply(param1, param2);
    }
    default Function1<Param1, Return> bind2(Param2 param2){
        return param1 -> apply(param1, param2);
    }
    default Function1<Param1, Function1<Param2, Return>> curry(){
        return this::bind1;
    }
}

