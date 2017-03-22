import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VCS {
    private static final Path GIT_DIRECTORY = Paths.get(".git/");
    private static final Path REFS_DIRECTORY = GIT_DIRECTORY.resolve("refs/");
    private static final Path HEAD_FILE = GIT_DIRECTORY.resolve("HEAD");
    private static final String REF_PREFIX = "ref: ";
    private final Map<String, GitObject> objects = new HashMap<>();
    private final Map<String, String> refs = new HashMap<>();
    private String head = null;

    VCS() throws IOException {
        try {
            if (Files.notExists(HEAD_FILE)) {
                Files.createFile(HEAD_FILE);
                Files.write(HEAD_FILE, "master".getBytes());
                head = "master";
            }
            if (Files.notExists(GIT_DIRECTORY)) {
                Files.createDirectory(GIT_DIRECTORY);
            }
            if (Files.notExists(REFS_DIRECTORY)) {
                Files.createDirectory(REFS_DIRECTORY);
            }
        }
        catch (IOException e) {
            // TODO: throw custom exception?
            throw new IOException("Cannot create VCS directories!");
        }
    }

    public void add(Path filePath){
        throw new UnsupportedOperationException();
    }
    public void commit(String commitMessage){
        throw new UnsupportedOperationException();
    }

    public void checkout(String branchName) throws IOException, VCSException {
        if (getHead().equals(branchName)) {
            return;
        }
        assertBranchExists(branchName);
        setHead(branchName);
    }

    private void assertBranchExists(String branchName) throws VCSException {
        if (Files.notExists(REFS_DIRECTORY.resolve(branchName))) {
            throw new VCSException("Branch with name " + branchName + " doesn't exist!");
        }
    }

    public void createBranch(String branchName) throws VCSException, IOException {
        Path branchPath = REFS_DIRECTORY.resolve(branchName);
        if (Files.exists(branchPath)) {
            throw new VCSException("A branch '" + branchName + "' already exists!");
        }
        try {
            Files.createFile(branchPath);
        } catch (IOException e) {
            throw new IOException("Cannot create ref for branch " + branchName);
        }
        // TODO: handle ref not found
        Files.write(branchPath, getHeadCommit().getBytes());
    }
    public void deleteBranch(String branchName) throws VCSException, IOException {
        assertBranchExists(branchName);
        if (branchName.equals(getHead())) {
            throw new VCSException("Cannot delete branch '" + branchName + "' while you are currently on.");
        }
        Files.delete(REFS_DIRECTORY.resolve(branchName););
    }
    public List<String> log(){
        throw new UnsupportedOperationException();
    }
    public void merge(String branchName){
        throw new UnsupportedOperationException();
    }

    private String getHead() throws IOException {
        if (head == null) {
            try {
                head = readString(HEAD_FILE);
            } catch (IOException e) {
                // TODO: throw custom exception?
                throw new IOException("Cannot read HEAD file!");
            }
        }
        return head;
    }

    private String getHeadCommit() throws IOException, VCSRefNotFoundException {
        String head = getHead();
        if (head.startsWith(REF_PREFIX))
            return getRefContent(head.substring(REF_PREFIX.length()));
        else
            return head;
    }

    private String getRefContent(String ref) throws VCSRefNotFoundException, IOException {
        if (!refs.containsKey(ref)) {
            Path refPath = REFS_DIRECTORY.resolve(ref);
            if (Files.notExists(refPath)) {
                throw new VCSRefNotFoundException();
            }
            try {
                String refContent = readString(refPath);
                refs.put(ref, refContent);
            } catch (IOException e) {
                throw new IOException("Cannot read ref file for "+ ref);
            }
        }
        return refs.get(ref);
    }

    private String readString(Path path) throws IOException {
        return new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
    }

    private void setHead(String head) throws IOException {
        this.head = head;
        try {
            Files.write(HEAD_FILE, head.getBytes());
        }
        catch (IOException e) {
            throw new IOException("Cannot write to HEAD file!");
        }
    }
}
