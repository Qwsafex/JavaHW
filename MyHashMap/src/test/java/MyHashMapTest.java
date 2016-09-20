import org.junit.Test;
import org.omg.PortableInterceptor.Interceptor;

import static org.junit.Assert.*;

public class MyHashMapTest {
    @Test
    public void size() throws Exception {
        MyHashMap hashMap = new MyHashMap(10);
        for (int i = 0; i < 1; i++) {
            hashMap.put(Integer.toString(i), "x");
        }
        assertEquals(1, hashMap.size());
        for (int i = 0; i < 1; i++) {
            hashMap.remove(Integer.toString(i));
        }
        assertEquals(0, hashMap.size());
        //hashMap.remove(Integer.toString(3));
      //  assertEquals(5, hashMap.size());
    }

    @Test
    public void contains() throws Exception {
        MyHashMap hashMap = new MyHashMap(10);
        assertFalse(hashMap.contains("heh"));
        for (int i = 0; i < 100; i++){
            hashMap.put(Integer.toString(i), "heh");
            assertTrue(hashMap.contains(Integer.toString(i)));
        }
        assertTrue(hashMap.contains("1"));
    }

    @Test
    public void get() throws Exception {
        MyHashMap hashMap = new MyHashMap(10);
        assertNull(hashMap.get("heh"));
        for (int i = 0; i < 100; i++){
            hashMap.put(Integer.toString(i), "heh");
            assertEquals("heh", hashMap.get(Integer.toString(i)));
        }
    }

    @Test
    public void put() throws Exception {
        MyHashMap hashMap = new MyHashMap(10);
        for (int i = 0; i < 100; i++){
            hashMap.put("meh", "heh");
            assertEquals(1, hashMap.size());
        }
    }

    @Test
    public void remove() throws Exception {
        MyHashMap hashMap = new MyHashMap(10);
        assertNull(hashMap.remove("heh"));
        for (int i = 0; i < 100; i++){
            hashMap.put(Integer.toString(i), "heh");
            hashMap.remove(Integer.toString(i));
            assertEquals(0, hashMap.size());
        }

    }

    @Test
    public void clear() throws Exception {
        MyHashMap hashMap = new MyHashMap(10);
        for (int i = 0; i < 100; i++) {
            hashMap.put(Integer.toString(i), "x");
        }
        hashMap.clear();
        assertEquals(0, hashMap.size());
    }

}