public class MyHashMap {

    // хеш-таблица, использующая список
    // ключами и значениями выступают строки
    // стандартный способ получить хеш объекта -- вызвать у него метод hashCode()
    // сейчас все методы бросают исключение
    // это сделано, чтобы код компилировался, в конечном коде такого исключения быть не должно

    MyLinkedList[] lists;

    int keyCount;
    int capacity;

    public MyHashMap(int capacity){
        this.capacity = capacity;
        keyCount = 0;
        lists = new MyLinkedList[capacity];
    }

    public int size() {
        return keyCount;
    }

    public boolean contains(String key) {
        MyLinkedList list = lists[key.hashCode() % capacity];
        if (list == null){
            return false;
        }
        MyNode current = list.getHead();
        while (current != null){
            if (current.first.compareTo(key) == 0){
                return true;
            }
            current = current.next;
        }
        return false;
    }

    public String get(String key) {
        MyLinkedList list = lists[key.hashCode() % capacity];
        if (list == null){
            return null;
        }
        MyNode current = list.getHead();
        while (current != null){
            if (current.first.compareTo(key) == 0){
                return current.second;
            }
            current = current.next;
        }
        return null;
    }

    public String put(String key, String value) {
        if(!contains(key)){
            keyCount++;
        }
        MyLinkedList list = lists[key.hashCode() % capacity];
        if (list == null){
            list = new MyLinkedList();
            lists[key.hashCode() % capacity] = list;
        }
        MyNode current = list.getHead();
        while (current != null){
            if (current.first.compareTo(key) == 0){
                String result = current.second;
                current.second = value;
                return result;
            }
            current = current.next;
        }
        list.add(key, value);
        return null;
    }

    public String remove(String key) {
        MyLinkedList list = lists[key.hashCode() % capacity];
        if (list == null){
            return null;
        }
        MyNode current = list.getHead();
        while (current != null){
            if (current.first.compareTo(key) == 0){
                list.remove(current);
                if(list.empty()){
                    keyCount--;
                }
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
