import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by ilya on 10/25/16.
 */
public class Collections {
    public static <Return, Param> List<Return> map(Function1<? super Param, ? extends Return> f, Iterable<Param> iterable){
        List<Return> result = new ArrayList<>();
        iterable.forEach(param -> result.add(f.apply(param)));
        return result;
    }
    public static <Param> List<Param> filter(Predicate<? super Param> f, Iterable<Param> iterable){
        List<Param> result = new ArrayList<>();
        for(Param param : iterable){
            if (f.apply(param)){
                result.add(param);
            }
        }
        return result;
    }
    public static <Param> List<Param> takeWhile(Predicate<? super Param> f, Iterable<Param> iterable){
        List<Param> result = new ArrayList<>();
        for(Param param : iterable){
            if (f.apply(param)){
                result.add(param);
            }
            else{
                break;
            }
        }
        return result;
    }
    public static <Param> List<Param> takeUnless(Predicate<? super Param> f, Iterable<Param> iterable){
        return takeWhile(f.not(), iterable);
    }
    public static <Return> Return foldl(Function2<? super Return, ? super Return, ? extends Return> f, Return ini, Iterable<Return> iterable){
        Return result = ini;
        for(Return a : iterable){
            result = f.apply(result, a);
        }
        return result;
    }
    public static <Return> Return foldr(Function2<? super Return, ? super Return, ? extends Return> f, Return ini, Iterable<Return> iterable){
        return foldr(f, ini, iterable.iterator());
    }
    private static <Return> Return foldr(Function2<? super Return, ? super Return, ? extends Return> f, Return ini, Iterator<Return> iterator) {
        if(!iterator.hasNext()){
            return ini;
        }
        Return first = iterator.next();
        return f.apply(first, foldr(f, ini, iterator));
    }
}
