package vcs;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class VCS {
    private static final Path GIT_DIRECTORY = Paths.get(".vcs/");
    private static final Path REFS_DIRECTORY = GIT_DIRECTORY.resolve("refs/");
    private static final Path OBJS_DIRECTORY = GIT_DIRECTORY.resolve("objects/");
    private static final Path HEAD_FILE = GIT_DIRECTORY.resolve("HEAD");
    private static final Path INDEX_FILE = GIT_DIRECTORY.resolve("index");
    private static final VCSRef MASTER_BRANCH = new VCSRef("master");
    private static final String CONFLICT_SEPARATOR = "\n=======================\n";
    private final Map<String, GitObject> objects = new HashMap<>();
    private final Map<String, String> refs = new HashMap<>();
    private String head = null;
    private ArrayList<LightBlob> index = null;

    /**
     *
     * @param branchName name of branch to be merge into current
     * @return list of conflicting pathes
     * @throws BranchNotFoundException
     * @throws VCSFilesCorruptedException
     * @throws RefNotFoundException
     * @throws IOException
     */
    public List<String> merge(String branchName) throws VCSException, IOException {
        if (!branchExists(branchName)) {
            throw new BranchNotFoundException(branchName);
        }
        List<LightBlob> currentBlobs = getBlobsRecursively(getHeadCommit());
        List<LightBlob> newBlobs = getBlobsRecursively(getRefContent(new VCSRef(branchName)));
        Map<String, LightBlob> presentBlobs = new HashMap<>();
        currentBlobs.forEach(blob -> presentBlobs.put(blob.getPath(), blob));
        List<String> conflicts = new ArrayList<>();
        for (LightBlob lightBlob : newBlobs) {
            if (presentBlobs.containsKey(lightBlob.getPath()) &&
                    !presentBlobs.get(lightBlob.getPath()).getHash().equals(lightBlob.getHash())) {
                conflicts.add(lightBlob.getPath());
                Blob blob = (Blob) getObject(lightBlob.getHash());
                Files.write(Paths.get(lightBlob.getPath()), CONFLICT_SEPARATOR.getBytes(), StandardOpenOption.APPEND);
                Files.write(Paths.get(lightBlob.getPath()), blob.getContent(), StandardOpenOption.APPEND);
            }
            else {
                putBlobOnDisk(lightBlob);
                addBlobToIndex((Blob) getObject(lightBlob.getHash()));
            }
        }
        return conflicts;
    }

    private List<LightBlob> getBlobsRecursively(String commitHash) throws IOException, VCSFilesCorruptedException {
        Map<String, LightBlob> blobs = new HashMap<>();
        String currentCommitHash = commitHash;
        while (currentCommitHash != null) {
            Commit currentCommit = (Commit) getObject(currentCommitHash);
            currentCommit.getBlobs().forEach(blob -> blobs.putIfAbsent(blob.getPath(), blob));
            currentCommitHash = currentCommit.getPrevCommit();
        }
        List<LightBlob> result = new ArrayList<>();
        blobs.forEach((key, blob) -> result.add(blob));
        return result;
    }

    public void checkout(String revision) throws IOException, BranchNotFoundException, VCSFilesCorruptedException, RefNotFoundException {
        if (branchExists(revision)) {
            VCSRef branch = new VCSRef(revision);
            if (getHead().equals(branch.toString())) {
                return;
            }
            setHead(branch.toString());
        }
        else {
            if (getHead().equals(revision)) {
                return;
            }
            setHead(revision);
        }

        List<LightBlob> blobs = getBlobsRecursively(getHeadCommit());
        for (LightBlob blob : blobs) {
            putBlobOnDisk(blob);
        }
    }

    private void putBlobOnDisk(LightBlob lightBlob) throws IOException, VCSFilesCorruptedException {
        Path blobPath = Paths.get(lightBlob.getPath());
        Path blobParent = blobPath.getParent();
        if (blobParent != null && Files.notExists(blobParent)) {
            Files.createDirectories(blobParent);
        }
        Blob blob = (Blob) getObject(lightBlob.getHash());
        Files.write(blobPath, blob.getContent());
    }

    public VCS() throws IOException, VCSException {
        try {
            if (Files.notExists(GIT_DIRECTORY)) {
                Files.createDirectory(GIT_DIRECTORY);
            }
            if (Files.notExists(REFS_DIRECTORY)) {
                Files.createDirectory(REFS_DIRECTORY);
            }
            if (Files.notExists(OBJS_DIRECTORY)) {
                Files.createDirectory(OBJS_DIRECTORY);
            }
        }
        catch (IOException e) {
            // TODO: throw custom exception?
            throw new IOException("Cannot create VCS directories!");
        }
        if (Files.notExists(INDEX_FILE)) {
            index = new ArrayList<>();
            try {
                writeObject(index, INDEX_FILE.toFile());
            } catch (IOException e) {
                throw new IOException("Cannot create index file!");
            }
        }
        if (Files.notExists(HEAD_FILE)) {
            try {
                writeObject(MASTER_BRANCH.toString(), HEAD_FILE.toFile());
            } catch (IOException e) {
                throw new IOException("Cannot create or write to HEAD file!");
            }
            head = MASTER_BRANCH.toString();
            Commit initialCommit = new Commit("initial", new ArrayList<>(), null);
            writeGitObject(initialCommit);
            setRefContent(MASTER_BRANCH.getName(), initialCommit.getSHA());
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
        writeObject(getHeadCommit(), branchPath.toFile());
    }
    public void deleteBranch(String branchName) throws VCSException, IOException {
        assertBranchExists(branchName);
        if (branchName.equals(getHead())) {
            throw new VCSException("Cannot delete branch '" + branchName + "' while you are currently on.");
        }
        Files.delete(REFS_DIRECTORY.resolve(branchName));
    }

    public void add(Path filePath) throws IOException, VCSException {
        if (Files.isDirectory(filePath)) {
            throw new UnsupportedOperationException();
        }
        Blob newBlob = new Blob(filePath);
        writeGitObject(newBlob);
        addBlobToIndex(newBlob);
    }

    private void addBlobToIndex(Blob blob) throws IOException, VCSException {
        ArrayList<LightBlob> index = getIndex();
        boolean found = false;
        for (int i = 0; i < index.size(); i++) {
            LightBlob currentBlob = index.get(i);
            if (blob.getPath().equals(currentBlob.getPath())) {
                if (!blob.getSHA().equals(currentBlob.getHash())) {
                    index.set(i, blob.getLightBlob());
                }
                else {
                    return;
                }
                found = true;
                break;
            }
        }
        if (!found) {
            index.add(blob.getLightBlob());
        }
        setIndex(index);
    }

    public List<Commit> log() throws IOException, VCSFilesCorruptedException, RefNotFoundException {
        List<Commit> result = new ArrayList<>();
        String lastCommitHash = null;
        if (VCSRef.isRef(getHead())) {
            lastCommitHash = getRefContent(VCSRef.fromString(getHead()));
        }
        else {
            lastCommitHash = getHead();
        }
        while (lastCommitHash != null) {
            Commit lastCommit = (Commit) getObject(lastCommitHash);
            result.add(lastCommit);
            lastCommitHash = lastCommit.getPrevCommit();
        }
        Collections.reverse(result);
        return result;
    }

    public void commit(String commitMessage) throws NothingToCommitException, IOException, VCSException {
        List<LightBlob> index = getIndex();
        if (index.size() == 0) {
            throw new NothingToCommitException("Nothing to commit");
        }
        String prevCommit = getHeadCommit();
        Commit commit = new Commit(commitMessage, index, prevCommit);
        writeGitObject(commit);
        if (VCSRef.isRef(getHead())) {
            setRefContent(getHead(), commit.getSHA());
        }
        else {
            setHead(commit.getSHA());
        }
        setIndex(new ArrayList<>());
    }

    private GitObject getObject(String hash) throws VCSFilesCorruptedException, IOException {
        if (!objects.containsKey(hash)) {
            try {
                objects.put(hash, readGitObject(hash));
            } catch (ClassNotFoundException e) {
                throw new VCSFilesCorruptedException();
            } catch (IOException e) {
                throw new IOException("Cannot get object from '"+ hash +"' file");
            }
        }
        return objects.get(hash);
    }



    private boolean branchExists(String branchName) {
        return Files.exists(REFS_DIRECTORY.resolve(branchName));
    }

    private void assertBranchExists(String branchName) throws BranchNotFoundException {
        if (!branchExists(branchName)) {
            throw new BranchNotFoundException(branchName);
        }
    }

    private String getHead() throws IOException, VCSFilesCorruptedException {
        if (head == null) {
            try {
                head = (String) readObject(HEAD_FILE.toFile());
            } catch (IOException e) {
                // TODO: throw custom exception?
                throw e;
                //throw new IOException("Cannot read HEAD file!");
            } catch (ClassNotFoundException e) {
                throw new VCSFilesCorruptedException();
            }
        }
        return head;
    }

    private String getHeadCommit() throws IOException, RefNotFoundException, VCSFilesCorruptedException {
        String head = getHead();
        if (VCSRef.isRef(head)) {
            return getRefContent(VCSRef.fromString(head));
        }
        else
            return head;
    }

    private String getRefContent(VCSRef ref) throws RefNotFoundException, IOException, VCSFilesCorruptedException {
        if (!refs.containsKey(ref.getName())) {
            Path refPath = REFS_DIRECTORY.resolve(ref.getName());
            if (Files.notExists(refPath)) {
                throw new RefNotFoundException();
            }
            try {
                String refContent = (String) readObject(refPath.toFile());
                refs.put(ref.getName(), refContent);
            } catch (IOException e) {
                throw new IOException("Cannot read ref file for "+ ref);
            } catch (ClassNotFoundException e) {
                throw new VCSFilesCorruptedException();
            }
        }
        return refs.get(ref.getName());
    }

    private void setHead(String head) throws IOException {
        this.head = head;
        try {
            writeObject(head, HEAD_FILE.toFile());
        }
        catch (IOException e) {
            throw new IOException("Cannot write to HEAD file!");
        }
    }

    private ArrayList<LightBlob> getIndex() throws VCSException, IOException {
        if (index == null) {
            try {
                FileInputStream indexIn = new FileInputStream(INDEX_FILE.toFile());
                ObjectInputStream indexObjIn = new ObjectInputStream(indexIn);
                //noinspection unchecked
                index = (ArrayList<LightBlob>) indexObjIn.readObject();
            } catch (FileNotFoundException e) {
                // TODO: throw more specialized exception
                throw new VCSException("Index file not found!");
            } catch (IOException e) {
                // TODO: throw another exception?
                throw new IOException("Unable to read from index file!");
            } catch (ClassNotFoundException e) {
                throw new VCSFilesCorruptedException();
            }
        }
        return index;
    }

    private void setIndex(ArrayList<LightBlob> index) throws VCSException, IOException {
        this.index = index;
        try {
            writeObject(index, INDEX_FILE.toFile());
        }
        catch (FileNotFoundException e) {
            // TODO: throw more specialized exception
            throw new VCSException("Index file not found!");
        } catch (IOException e) {
            // TODO: throw another exception?
            throw new IOException("Unable to write to index file!");
//            throw new IOException("Unable to write to index file!");
        }
    }
    private void setRefContent(String ref, String hash) throws IOException {
        if (VCSRef.isRef(ref)) {
            ref = VCSRef.fromString(ref).getName();
        }
        refs.put(ref, hash);
        writeObject(hash, REFS_DIRECTORY.resolve(ref).toFile());
    }

    private void writeGitObject(GitObject gitObject) throws VCSException {
        // Destination determines object content
        try {
            Path path = OBJS_DIRECTORY.resolve(gitObject.getSHA());
            if (Files.exists(path)) {
                return;
            }
            writeObject(gitObject, path.toFile());
        } catch (IOException e) {
            throw new VCSException("Can't write "+ gitObject.getSHA() +" object to file!");
        }
    }
    private GitObject readGitObject(String hash) throws IOException, ClassNotFoundException {
        return (GitObject) readObject(OBJS_DIRECTORY.resolve(hash).toFile());
    }
    private void writeObject(Object object, File destination) throws IOException {
        FileOutputStream outStream = new FileOutputStream(destination);
        ObjectOutputStream objOutStream = new ObjectOutputStream(outStream);
        objOutStream.writeObject(object);
        objOutStream.close();
        outStream.close();
    }
    private Object readObject(File source) throws IOException, ClassNotFoundException {
        FileInputStream inStream = new FileInputStream(source);
        ObjectInputStream objInStream = new ObjectInputStream(inStream);
        Object object = objInStream.readObject();
        objInStream.close();
        inStream.close();
        return object;

    }
}
