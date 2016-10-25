import java.util.*;

/**
 * Created by ilya on 10/5/16.
 */

public class HashMultiset<E> implements Multiset<E>{
    public static class HashMultisetIterator<E> implements Iterator<E>{
        private Iterator<Map.Entry<E, EntryImpl<E>>> entryIterator;
        private Iterator<E> innerIterator;

        public HashMultisetIterator(Iterator<Map.Entry<E, EntryImpl<E>>> iter){
            entryIterator = iter;
            if (entryIterator.hasNext()) {
                innerIterator = entryIterator.next().getValue().elements.iterator();
            }
            else{
                innerIterator = null;
            }
        }

        @Override
        public boolean hasNext() {
            if (innerIterator == null){
                return false;
            }
            if (innerIterator.hasNext()) {
                return true;
            }
            else {
                return entryIterator.hasNext();
            }
        }

        @Override
        public E next() {
            if (innerIterator == null){
                throw new NoSuchElementException();
            }

            if (!innerIterator.hasNext())
                innerIterator = entryIterator.next().getValue().elements.iterator();
            return innerIterator.next();
        }
    }
    private static class EntryImpl<E> implements Entry<E>{
        private ArrayList<E> elements;

        private EntryImpl(){
            this.elements = new ArrayList<>();
        }

        @Override
        public E getElement() {
            return elements.get(0);
        }

        @Override
        public int getCount() {
            return elements.size();
        }
    }
    private HashMap<E, EntryImpl<E>> entries;
    private HashSet<E> elementSet;
    private HashSet<Entry<E>> entrySet;
    private int size = 0;

    public HashMultiset(){
        entries = new HashMap<>();
        entrySet = new HashSet<>();
        elementSet = new HashSet<>();
    }

    @Override
    public int count(Object element) {
        if (!entries.containsKey(element)){
            return 0;
        }
        return entries.get(element).getCount();
    }

    @Override
    public Set<E> elementSet() {
        return elementSet;
    }

    @Override
    public Set<Entry<E>> entrySet() {
        return entrySet;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public boolean contains(Object o) {
        return entries.containsKey(o);
    }

    @Override
    public Iterator<E> iterator() {
        return new HashMultisetIterator<E>(entries.entrySet().iterator());
    }

    @Override
    public Object[] toArray() {
        ArrayList<Object> result = new ArrayList<>();
        Iterator<E> iterator = iterator();
        while(iterator.hasNext()){
            result.add(iterator.next());
        }
        return result.toArray();
    }

    @Override
    public <T> T[] toArray(T[] ts) {
        ArrayList<T> result = new ArrayList<>();
        Iterator<E> iterator = iterator();
        while(iterator.hasNext()){
            result.add((T) iterator.next());
        }
        return result.toArray(ts);
    }

    @Override
    public boolean add(E e) {
        if (!entries.containsKey(e)){
            entries.put(e, new EntryImpl<>());
        }
        EntryImpl<E> entry = entries.get(e);
        entry.elements.add(e);
        size++;
        entrySet.add(entry);
        elementSet.add(e);
        return true;
    }

    @Override
    public boolean remove(Object o) {
        EntryImpl<E> entry = entries.get(o);
        if (entry == null) {
            return false;
        }
        entry.elements.remove(entry.elements.size() - 1);
        if (entry.getCount() == 0) {
            entries.remove(o);
            elementSet.remove(o);
            entrySet.remove(o);
        }
        size--;
        return true;
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        for(Object element : collection) {
            if (!contains(element)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> collection) {
        boolean changed = false;
        for(E element : collection) {
            changed |= add(element);
        }
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        boolean changed = false;
        for (Object element : collection){
            changed |= remove(element);
        }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> collection) {

        return false;
    }

    @Override
    public void clear() {
        entries.clear();
    }
}
