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
        lists = new MyLinkedList[capacity];
    }

    public int size() {
        return keyCount;
    }

    public boolean contains(String key) {
        MyLinkedList list = lists[key.hashCode() % capacity];
        MyNode current = list.getHead();
        while (current != null){
            if (current.first == key){
                return true;
            }
            current = current.next;
        }
        return false;
    }

    public String get(String key) {
        MyLinkedList list = lists[key.hashCode() % capacity];
        MyNode current = list.getHead();
        while (current != null){
            if (current.first == key){
                return current.second;
            }
            current = current.next;
        }
        return null;
    }

    public String put(String key, String value) {
        if(!contains(key)){
            keyCount++;
            return null;
        }
        MyLinkedList list = lists[key.hashCode() % capacity];
        MyNode current = list.getHead();
        while (current != null){
            if (current.first == key){
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
        MyNode current = list.getHead();
        while (current != null){
            if (current.first == key){
                current.kick();
                return current.second;
            }
            current = current.next;
        }

        if(!contains(key)){
            keyCount--;
        }

        return null;
    }

    public void clear() {
        lists = new MyLinkedList[capacity];
    }
}
