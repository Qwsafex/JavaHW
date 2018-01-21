public class MyLinkedList {
    private MyNode head;
    public boolean empty() {
        return head == null;
    }

    public MyNode getHead() {
        return head;
    }

    public void add(String key, String value) {
        MyNode newHead = new MyNode(key, value, null, head);
        if (head != null) head.prev = newHead;
        head = newHead;
    }

    public void remove(MyNode node) {
        if (node == head){
            head = node.next;
        }
        if (node.next != null){
            node.next.prev = node.prev;
        }
        if (node.prev != null){
            node.prev.next = node.next;
        }

    }
}