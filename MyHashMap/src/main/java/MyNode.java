public class MyNode {
    public String first;
    public String second;
    public MyNode next;
    public MyNode prev;
    public MyNode(String first, String second, MyNode prev, MyNode next){
        this.first = first;
        this.second = second;
        this.next = next;
        this.prev = prev;
    }

    public void kick() {
        if (next != null){
            next.prev = prev;
        }
        if (prev != null){
            prev.next = next;
        }
    }
}
