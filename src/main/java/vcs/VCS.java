package vcs;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class VCS {
    private static final String INDEX_FILENAME = "index";
    private static final String STAGED_FILENAME = "staged";
    private static final String HEAD_FILENAME = "HEAD";
    private static final byte[] MERGED_FILES_SEPARATOR = "\n===============\n".getBytes();
    private static final String MASTER_BRANCH = "master";
    private CommitRef head;
    private RepoState index;
    private RepoState staged;

    // TODO: take String[] as args and check them

    public VCS() throws IOException, ClassNotFoundException {
        VCSFiles.init();
        index = new RepoState(INDEX_FILENAME);
        staged = new RepoState(STAGED_FILENAME);
        Path headPath = Paths.get(HEAD_FILENAME);
        if (!VCSFiles.exists(headPath)) {
            VCSFiles.create(headPath);
            Commit initial = new Commit("initial", new ArrayList<>(), null);
            VCSFiles.writeObject(headPath, Branches.create(MASTER_BRANCH, initial.getSHARef()));
        }
        head = CommitRef.readRef(HEAD_FILENAME);
    }

    public void checkout(String revision) throws CheckoutStagedNotEmptyException, VCSFilesCorruptedException,
            BranchNotFoundException, IOException, ClassNotFoundException {
        if (!staged.empty()) {
            throw new CheckoutStagedNotEmptyException();
        }
        if (Branches.exists(revision)) {
            Branch newBranch = Branches.get(revision);
            head = newBranch;
            index = RepoState.getFromCommit(newBranch.getCommit());
        }
        else {
            Commit headCommit = Commit.get(revision);
            head = headCommit.getSHARef();
            index = RepoState.getFromCommit(headCommit);
        }
        VCSFiles.writeObject(Paths.get(HEAD_FILENAME), head);
    }
    public void add(String pathString) throws IOException {
        Path filePath = Paths.get(pathString);
        if (Files.isDirectory(filePath)) {
            throw new UnsupportedOperationException();
        }
        ContentfulBlob newBlob = new ContentfulBlob(filePath);
        staged.add(newBlob.getContentlessBlob());
    }
    public void createBranch(String branchName) throws IOException {
        Branches.create(branchName, head.getCommitSHA());
    }
    public void deleteBranch(String branchName) throws BranchNotFoundException, IOException, VCSFilesCorruptedException {
        if (head.equals(Branches.get(branchName))) {
            throw new BranchNotFoundException(branchName);
        }
        Branches.delete(branchName);
    }
    public void commit(String commitMessage) throws NothingToCommitException, EmptyCommitMessageException, IOException {
        if (commitMessage.isEmpty()) {
            throw new EmptyCommitMessageException();
        }
        if (staged.empty()) {
            throw new NothingToCommitException();
        }
        index.updateWith(staged);
        Commit commit = new Commit(commitMessage, staged.getFiles(), head.getCommitSHA());
        head = head.addCommitAfter(commit);
    }
    public List<String> log() throws IOException, ClassNotFoundException {
        CommitRef currentCommitRef = head;
        List<String> result = new ArrayList<>();
        while (currentCommitRef != null) {
            Commit currentCommit = currentCommitRef.getCommit();
            result.add(currentCommit.toString());
            currentCommitRef = currentCommit.getPrevCommit();
        }
        Collections.reverse(result);
        return result;
    }
    public List<String> merge(String branchName) throws MergeWhenStagedNotEmptyException, VCSFilesCorruptedException,
            BranchNotFoundException, IOException, ClassNotFoundException {
        if (!staged.empty()) {
            throw new MergeWhenStagedNotEmptyException();
        }
        Branch mergingBranch = Branches.get(branchName);
        RepoState curState = index;
        RepoState mergingState = RepoState.getFromCommit(mergingBranch.getCommit());
        Map<String, ContentlessBlob> curBlobs = curState.getFiles().stream().collect(Collectors.toMap(ContentlessBlob::getPath, Function.identity()));
        List<ContentlessBlob> mergingBlobs = mergingState.getFiles();
        for (ContentlessBlob newBlob : mergingBlobs) {
            if (curBlobs.containsKey(newBlob.getPath())) {
                ContentlessBlob oldBlob = curBlobs.get(newBlob.getPath());
                if (!oldBlob.getSHARef().equals(newBlob.getSHARef())) {
                    // TODO: Bad from design point of view
                    mergeBlobs(oldBlob, newBlob);
                }
            }
            else {
                staged.add(newBlob);
            }
        }

        return null;
    }

    private void mergeBlobs(ContentlessBlob oldBlob, ContentlessBlob newBlob) throws IOException, ClassNotFoundException {
        // TODO: looks not right
        Path path = Paths.get(oldBlob.getPath());
        Files.write(path, oldBlob.getContentfulBlob().getContent());
        Files.write(path, MERGED_FILES_SEPARATOR, StandardOpenOption.APPEND);
        Files.write(path, newBlob.getContentfulBlob().getContent(), StandardOpenOption.APPEND);
    }
}
