import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import static org.junit.Assert.*;

/**
 * Created by ilya on 10/5/16.
 */
public class TrieImplTest {
    @Test
    public void addContains() throws Exception {
        TrieImpl trie = new TrieImpl();
        assertFalse(trie.contains("ab"));
        String hello = "hello";
        trie.add(hello);
        for (int i = 1; i < hello.length(); i++){
            assertFalse(trie.contains(hello.substring(0,i)));
        }
        assertTrue(trie.contains(hello));

        trie.add(hello.substring(0,1));
        for (int i = 2; i < hello.length(); i++){
            assertFalse(trie.contains(hello.substring(0,i)));
        }
        assertTrue(trie.contains(hello));
        assertTrue(trie.contains(hello.substring(0,1)));
    }

    @Test
    public void addRemove() throws Exception {
        TrieImpl trie = new TrieImpl();
        trie.add("abaca");
        assertTrue(trie.contains("abaca"));
        trie.remove("abaca");
        assertFalse(trie.contains("abaca"));
        trie.remove("abaca");
        assertFalse(trie.contains("abaca"));

        trie.add("abaca");
        trie.add("abaca");
        assertTrue(trie.contains("abaca"));
        trie.remove("abaca");
        assertFalse(trie.contains("abaca"));
    }

    @Test
    public void removePrefix() throws Exception {
        TrieImpl trie = new TrieImpl();
        assertTrue(trie.add("abacad"));
        trie.add("abadab");
        trie.add("aba");
        assertEquals(3, trie.howManyStartsWithPrefix("aba"));
        assertTrue(trie.remove("aba"));
        assertEquals(2, trie.howManyStartsWithPrefix("aba"));
        trie.add("aba");
        assertEquals(3, trie.howManyStartsWithPrefix("aba"));
        trie.remove("abacad");
        assertEquals(2, trie.howManyStartsWithPrefix("aba"));
    }

    @Test
    public void removeSize() throws Exception {
        TrieImpl trie = new TrieImpl();
        trie.add("abaca");
        trie.add("bacaba");
        assertEquals(2, trie.size());
        trie.remove("abacad");
        assertEquals(2, trie.size());
        trie.remove("abac");
        assertEquals(2, trie.size());
        trie.remove("abaca");
        assertEquals(1, trie.size());
    }

    @Test
    public void lowerUpper() throws Exception {
        TrieImpl trie = new TrieImpl();
        trie.add("a");
        trie.add("z");
        trie.add("A");
        trie.add("Z");
        assertEquals(4, trie.size());
        trie.remove("a");
        assertEquals(3, trie.size());
        trie.remove("A");
        assertEquals(2, trie.size());
        trie.remove("z");
        assertEquals(1, trie.size());
        trie.remove("Z");
        assertEquals(0, trie.size());
    }

    @Test
    public void howManyStartsWithPrefix() throws Exception {
        TrieImpl trie = new TrieImpl();
        assertEquals(0, trie.howManyStartsWithPrefix(""));

        trie.add("hello");
        assertEquals(1, trie.howManyStartsWithPrefix("hell"));
        trie.add("hell");
        assertEquals(2, trie.howManyStartsWithPrefix("hell"));
        trie.add("hella");
        assertEquals(3, trie.howManyStartsWithPrefix("hell"));

        trie.add("hello");
        assertEquals(3, trie.howManyStartsWithPrefix("hell"));
        trie.add("hell");
        assertEquals(3, trie.howManyStartsWithPrefix("hell"));
        trie.add("hella");
        assertEquals(3, trie.howManyStartsWithPrefix("hell"));
        assertEquals(3, trie.howManyStartsWithPrefix(""));
        assertEquals(0, trie.howManyStartsWithPrefix("hellb"));
    }

    @Test
    public void size() throws Exception {
        TrieImpl trie = new TrieImpl();
        assertEquals(0, trie.size());

        trie.add("a");
        assertEquals(1, trie.size());
        trie.add("a");
        assertEquals(1, trie.size());
        trie.add("ab");
        assertEquals(2, trie.size());
        trie.add("b");
        assertEquals(3, trie.size());
    }

    @Test
    public void serialization1() throws Exception { // deserialize to blank trie
        TrieImpl trie = new TrieImpl();
        trie.add("a");
        trie.add("abba");
        trie.add("bac");
        trie.add("bacardo");
        trie.add("abbc");

        ByteArrayOutputStream out = new ByteArrayOutputStream(100);
        trie.serialize(out);
        byte[] buf = out.toByteArray();
        ByteArrayInputStream in = new ByteArrayInputStream(buf);
        trie = new TrieImpl();
        trie.deserialize(in);

        assertTrue(trie.contains("a"));
        assertTrue(trie.contains("abba"));
        assertTrue(trie.contains("bac"));
        assertTrue(trie.contains("bacardo"));
        assertTrue(trie.contains("abbc"));

        assertEquals(5, trie.size());
        assertEquals(3, trie.howManyStartsWithPrefix("a"));
        assertEquals(2, trie.howManyStartsWithPrefix("bac"));
        assertEquals(1, trie.howManyStartsWithPrefix("baca"));
    }
    @Test
    public void serialization2() throws Exception { // deserialize to filled trie
        TrieImpl trie = new TrieImpl();
        trie.add("a");
        trie.add("abba");
        trie.add("bac");
        trie.add("bacardo");
        trie.add("abbc");

        ByteArrayOutputStream out = new ByteArrayOutputStream(100);
        trie.serialize(out);
        byte[] buf = out.toByteArray();
        ByteArrayInputStream in = new ByteArrayInputStream(buf);
        trie.deserialize(in);

        assertTrue(trie.contains("a"));
        assertTrue(trie.contains("abba"));
        assertTrue(trie.contains("bac"));
        assertTrue(trie.contains("bacardo"));
        assertTrue(trie.contains("abbc"));

        assertEquals(5, trie.size());
        assertEquals(3, trie.howManyStartsWithPrefix("a"));
        assertEquals(2, trie.howManyStartsWithPrefix("bac"));
        assertEquals(1, trie.howManyStartsWithPrefix("baca"));
    }

}