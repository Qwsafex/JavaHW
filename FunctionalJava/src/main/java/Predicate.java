/**
 * Created by ilya on 10/25/16.
 */
public interface Predicate<Param> extends Function1<Boolean, Param>{
    Predicate ALWAYS_TRUE = x -> true;
    Predicate ALWAYS_FALSE = x -> false;

    default Predicate<Param> not(){
        return param -> !apply(param);
    }
    default Predicate<Param> or(Predicate<Param> g){
        return param -> apply(param) || g.apply(param);
    }
    default Predicate<Param> and(Predicate<Param> g){
        return param -> apply(param) && g.apply(param);
    }
}
