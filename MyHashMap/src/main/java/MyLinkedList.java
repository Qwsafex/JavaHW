public class MyLinkedList {
    MyNode head;
    public boolean empty() {
        return true;
    }

    public MyNode getHead() {
        return null;
    }

    public void add(String key, String value) {
        MyNode newHead = new MyNode(key, value, null, head);
        head.prev = newHead;
        head = newHead;
    }
}
