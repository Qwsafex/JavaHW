package vcs;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class VCSTest {
    private static final TestFile A = new TestFile("A.txt", "A text");
    private static final TestFile A1 = new TestFile("A.txt", "A1 text");
    private static final TestFile B = new TestFile("B.txt", "B text");
    private static final TestFile C = new TestFile("C.txt", "C text");
    private static final TestFile A_B = new TestFile("A/B.txt", "A/B.txt text");
    private static final TestFile A_B_C = new TestFile("A/B/C.txt", "A/B/C.txt text");

    private static final String INITIAL_MSG = "initial";
    private static final String MASTER_BRANCH = "master";
    private static final Path VCS_PATH = Paths.get(".vcs/");

    /*

 2016  touch a
 2017  touch b
 2018  touch c
 2019  vcs add a
 2021  vcs commit add_A
 2027  vcs log
 2023  vcs add b
 2024  vcs commit add_B
 2027  vcs log
 2025  vcs add c
 2026  vcs commit add_C
 2027  vcs log


     */

    @Test
    public void simpleLog() throws Exception, NothingToCommitException {
        deleteRecursive(VCS_PATH);
        deleteRecursive(TestFile.TEST_DIR);
        VCS vcs = new VCS();
        List<TestFile> files = Arrays.asList(A, B, C);
        List<String> commitMessages = new ArrayList<>();
        commitMessages.add(INITIAL_MSG);
        for (int i = 0; i < files.size(); i++) {
            TestFile cur = files.get(i);
            cur.create();
            vcs.add(cur.getPath());
            String commitMessage = "add_" + cur.getPath().toString();
            vcs.commit(commitMessage);
            commitMessages.add(commitMessage);

            assertArrayEquals(commitMessages.toArray(),
                    vcs.log().stream().map(commit -> commit.getMessage()).collect(Collectors.toList()).toArray());
        }
    }

    /*
     2031  vcs branch A
     2032  vcs branch B
     2032  vcs branch С

     2033  touch A
     1111  echo "A1" > A
     2034  touch B
     1111  echo "B1" > B
     2035  touch C
     1111  echo "C1" > C

     2039  vcs checkout A
     2036  vcs add A
     2037  vcs commit add_A
     2038  vcs log

     2039  vcs checkout B
     2042  vcs add B
     2043  vcs commit add_B
     2052  vcs log

     2058  vcs checkout C
     2062  vcs add C
     2063  vcs commit add_C
     2064  vcs log

     2066  rm a b c
     2071  vcs checkout A
     1111  cat A
     2069  vcs checkout B
     1111  cat B
     2069  vcs checkout C
     1111  cat C

     */
    @Test
    public void branchesCheckout() throws Exception, NothingToCommitException, BranchAlreadyExistsException {
        deleteRecursive(TestFile.TEST_DIR);
        deleteRecursive(VCS_PATH);
        VCS vcs = new VCS();
        List<String> branches = Arrays.asList("A", "B", "C");
        List<TestFile> files = Arrays.asList(A, B, C);
        for (String branch : branches) {
            vcs.createBranch(branch);
        }
        for (int i = 0; i < branches.size(); i++) {
            TestFile cur = files.get(i);
            String branch = branches.get(i);
            cur.create();
            vcs.checkout(branch);
            vcs.add(cur.getPath());
            String commitMessage = "add_" + cur.getPath().toString();
            vcs.commit(commitMessage);
            List<Commit> log = vcs.log();
            assertEquals(2, log.size());
            assertEquals(INITIAL_MSG, log.get(0).getMessage());
            assertEquals(commitMessage, log.get(1).getMessage());
        }
        vcs.checkout(MASTER_BRANCH);
        for (TestFile file : files) {
            file.remove();
        }

        for (int i = 0; i < branches.size(); i++) {
            TestFile cur = files.get(i);
            String branch = branches.get(i);
            vcs.checkout(branch);
            cur.check();
        }
    }

    private void deleteRecursive(Path path) throws IOException {
        if (Files.notExists(path)) {
            return;
        }
        if (Files.isDirectory(path)) {
            DirectoryStream<Path> directoryStream = Files.newDirectoryStream(path);
            for (Path child : directoryStream) {
                deleteRecursive(child);
            }
        }
        Files.delete(path);
    }

}

class TestFile {
    public static final Path TEST_DIR = Paths.get("testXX/");

    private String content;
    private Path path;

    public TestFile(String path, String content){
        this.content = content;
        this.path = TEST_DIR.resolve(path);
    }
    public void create() throws IOException {
        Path parent = path.getParent();
        if (parent != null && Files.notExists(parent)) {
            Files.createDirectories(parent);
        }
        if (Files.notExists(path)) {
            Files.createFile(path);
        }
        Files.write(path, content.getBytes());
    }
    public void remove() throws IOException {
        Files.delete(path);
    }
    public boolean check() throws IOException {
        return Files.exists(path) &&
                Arrays.toString(Files.readAllBytes(path)).equals(content);
    }

    public Path getPath() {
        return path;
    }
}