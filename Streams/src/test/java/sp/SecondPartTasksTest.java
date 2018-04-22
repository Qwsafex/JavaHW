package sp;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static sp.SecondPartTasks.*;

public class SecondPartTasksTest {

    @Test
    public void testFindQuotes() throws IOException {
        TemporaryFolder temporaryFolder = new TemporaryFolder();
        temporaryFolder.create();
        File file1 = temporaryFolder.newFile("abc");
        String line1 = "afk sdfadoij asdpfoijasdf";
        String line2 = "asdfaweo asidhfa adpuj";
        String line3 = "asodifafkaosdjf";
        writeToFile(file1, line1, line2, line3);

        File file2 = temporaryFolder.newFile("cde");
        String line4 = "afkafkafk";
        String line5 = "propeirtg";
        String line6 = "adofjg foigjdfoj";
        writeToFile(file2, line4, line5, line6);

        File file3 = temporaryFolder.newFile("fgh");
        String line7 = "afk";
        String line8 = "";
        String line9 = "asodijf";
        writeToFile(file3, line7, line8, line9);

        final List<String> paths = Arrays.asList(file1.getPath(), file2.getPath(), file3.getPath());
        final List<String> correctLines = Arrays.asList(line1, line3, line4, line7);
        final List<String> assumedLines = findQuotes(paths, "afk");
        assertEquals(correctLines.size(), assumedLines.size());
        Collections.sort(correctLines);
        Collections.sort(assumedLines);
        assertEquals(correctLines, assumedLines);
    }

    private void writeToFile(File file1, String... lines) throws FileNotFoundException {
        PrintStream ps = new PrintStream(file1);
        for (String line : lines) {
            ps.println(line);
        }
        ps.close();
    }

    @Test
    public void testPiDividedBy4() {
        final int TEST_COUNT = 20;
        for (int i = 0; i < TEST_COUNT; i++) {
            assertEquals(Math.PI / 4, piDividedBy4(), 1e-2);
        }
    }

    @Test
    public void testFindPrinter() {
        HashMap<String, List<String>> authors = new HashMap<>();
        authors.put("Mike", Collections.singletonList("a"));
        authors.put("Jack", Collections.singletonList("ab"));
        authors.put("Dave", Arrays.asList("a", "b", "c"));
        authors.put("Matt", Arrays.asList("a", "b", "c", "d"));
        authors.put("John", Arrays.asList("a", "b", "c", "d", "e"));
        assertEquals("John", findPrinter(authors));
    }

    @Test
    public void findPrinterSingleAndEmpty(){
        HashMap<String, List<String>> authors = new HashMap<>();
        authors.put("Mike", Collections.emptyList());
        authors.put("Jack", Collections.singletonList(""));
        authors.put("Dave", Collections.singletonList("abcd"));
        authors.put("Matt", Collections.singletonList("abcdef"));
        authors.put("John", Collections.singletonList("abcdefghi"));
        assertEquals("John", findPrinter(authors));
    }

    @Test
    public void testCalculateGlobalOrder() {
        ArrayList<Map<String, Integer>> orders = new ArrayList<>();
        orders.add(ImmutableMap.<String,Integer>builder().put("a", 1).put("b", 1).put("c", 1).build());
        orders.add(ImmutableMap.<String,Integer>builder().put("a", 2).put("b", 3).put("c", 5).build());
        orders.add(ImmutableMap.<String,Integer>builder().put("a", 3).put("b", 4).put("c", 6).build());
        Map<String, Integer> globalOrder = calculateGlobalOrder(orders);
        assertEquals(6, (long) globalOrder.get("a"));
        assertEquals(8, (long) globalOrder.get("b"));
        assertEquals(12, (long) globalOrder.get("c"));
    }
}