import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class TrieImpl implements Trie, StreamSerializable {
    private static final int NODE_BEGIN = 100;
    private static final int NODE_END = 101;
    private static final int ALPHABET = 52;

    private TrieNode root;
    private int size;

    static class TrieNode{
        TrieNode[] next;
        int count;
        boolean terminal;
        private static int code(char c){
            if('a' <= c && c <= 'z') {
                return c - 'a';
            }
            else {
                return c - 'A' + 26;
            }
        }
        private TrieNode(){
            next = new TrieNode[ALPHABET];
            count = 0;
        }
    }

    public TrieImpl(){
        root = new TrieNode();
    }

    @Override
    public boolean add(String element) {
        if (contains(element)) {
            return false;
        }
        TrieNode current = root;
        for (int i = 0; i < element.length(); i++){
            int charCode = TrieNode.code(element.charAt(i));
            if (current.next[charCode] == null){
                current.next[charCode] = new TrieNode();
            }
            current.count++;
            current = current.next[charCode];
        }
        size++;
        current.terminal = true;
        current.count++;
        return true;
    }

    @Override
    public boolean contains(String element) {
        TrieNode current = root;
        for (int i = 0; i < element.length(); i++){
            int charCode = TrieNode.code(element.charAt(i));
            if (current.next[charCode] == null) {
                return false;
            }
            current = current.next[charCode];
        }
        return current.terminal;
    }

    @Override
    public boolean remove(String element) {
        if (!contains(element)){
            return false;
        }
        size--;
        TrieNode current = root;
        for (int i = 0; i < element.length(); i++){
            current.count--;
            int charCode = TrieNode.code(element.charAt(i));
            if (current.next[charCode].count == 1) {
                current.next[charCode] = null;
                return true;
            }
            current = current.next[charCode];
        }
        current.count--;
        current.terminal = false;
        return true;
    }

    @Override
    public int howManyStartsWithPrefix(String prefix) {
        TrieNode current = root;
        for (int i = 0; i < prefix.length(); i++){
            int charCode = TrieNode.code(prefix.charAt(i));
            if (current.next[charCode] == null){
                return 0;
            }
            current = current.next[charCode];
        }
        return current.count;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void deserialize(InputStream in) throws IOException {
        size = 0;
        root = parse(in);
        setCount(root);
    }

    @Override
    public void serialize(OutputStream out) throws IOException {
        output(root, out);
    }

    private void setCount(TrieNode root){
        root.count = root.terminal ? 1 : 0;
        for (int i = 0; i < ALPHABET; i++) {
            if (root.next[i] != null){
                setCount(root.next[i]);
                root.count += root.next[i].count;
            }
        }
    }
    private TrieNode parse(InputStream in) throws IOException {
        in.read();
        TrieNode result = new TrieNode();
        result.terminal = in.read() == 1;
        size += result.terminal ? 1 : 0;
        while(true){
            int edgeChar = in.read();
            if (edgeChar == NODE_END){
                return result;
            }
            else{
                result.next[edgeChar] = parse(in);
            }
        }
    }
    private void output(TrieNode current, OutputStream out) throws IOException{
        out.write(NODE_BEGIN);
        out.write(current.terminal ? 1 : 0);
        for (int i = 0; i < ALPHABET; i++) {
            if (current.next[i] != null) {
                out.write(i);
                output(current.next[i], out);
            }
        }
        out.write(NODE_END);
    }
}
