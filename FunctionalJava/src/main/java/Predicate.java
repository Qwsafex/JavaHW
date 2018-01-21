/**
 * Created by ilya on 10/25/16.
 */
public interface Predicate<Param> extends Function1<Param, Boolean>{
    Predicate<Object> ALWAYS_TRUE = x -> true;
    Predicate<Object> ALWAYS_FALSE = x -> false;

    default Predicate<Param> not(){
        return param -> !apply(param);
    }
    default Predicate<Param> or(Predicate<? super Param> g){
        return param -> apply(param) || g.apply(param);
    }
    default Predicate<Param> and(Predicate<? super Param> g){
        return param -> apply(param) && g.apply(param);
    }
}
