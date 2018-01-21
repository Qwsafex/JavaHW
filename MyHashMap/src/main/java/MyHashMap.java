public class MyHashMap {

    // хеш-таблица, использующая список
    // ключами и значениями выступают строки
    // стандартный способ получить хеш объекта -- вызвать у него метод hashCode()
    // сейчас все методы бросают исключение
    // это сделано, чтобы код компилировался, в конечном коде такого исключения быть не должно

    private MyLinkedList[] lists;

    private int keyCount;
    private int capacity;

    public MyHashMap(int capacity){
        this.capacity = capacity;
        keyCount = 0;
        lists = new MyLinkedList[capacity];
    }

    public int size() {
        return keyCount;
    }

    private int getIndex(String key){
        return key.hashCode() % capacity;
    }

    public boolean contains(String key) {
        MyLinkedList list = lists[getIndex(key)];
        if (list == null){
            return false;
        }
        MyNode current = list.getHead();
        while (current != null){
            if (current.first.equals(key)){
                return true;
            }
            current = current.next;
        }
        return false;
    }

    public String get(String key) {
        MyLinkedList list = lists[getIndex(key)];
        if (list == null){
            return null;
        }
        MyNode current = list.getHead();
        while (current != null){
            if (current.first.equals(key)){
                return current.second;
            }
            current = current.next;
        }
        return null;
    }

    public String put(String key, String value) {
        MyLinkedList list = lists[getIndex(key)];
        if (list == null){
            list = new MyLinkedList();
            lists[getIndex(key)] = list;
        }
        MyNode current = list.getHead();
        while (current != null){
            if (current.first.equals(key)){
                String result = current.second;
                current.second = value;
                return result;
            }
            current = current.next;
        }
        list.add(key, value);
        keyCount++;
        return null;
    }

    public String remove(String key) {
        MyLinkedList list = lists[getIndex(key)];
        if (list == null){
            return null;
        }
        MyNode current = list.getHead();
        while (current != null){
            if (current.first.equals(key)){
                list.remove(current);
                keyCount--;
                return current.second;
            }
            current = current.next;
        }

        return null;
    }

    public void clear() {
        lists = new MyLinkedList[capacity];
        keyCount = 0;
    }
}