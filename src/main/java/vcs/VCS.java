package vcs;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class VCS {
    private CommitRef head;
    private RepoState index;
    private RepoState staged;

    // TODO: take String[] as args and check them

    public VCS(){
        throw new UnsupportedOperationException();
    }

    public void checkout(String revision) throws CheckoutStagedNotEmpty {
        if (!staged.empty()) {
            throw new CheckoutStagedNotEmpty();
        }
        if (Branches.exists(revision)) {
            Branch newBranch = Branches.get(revision);
            head = newBranch;
            index = RepoState.getFromCommit(newBranch.getCommit());
        }
        else {
            Commit headCommit = Commit.get(revision);
            head = headCommit.getRef();
            index = RepoState.getFromCommit(headCommit);
        }
    }
    public void add(String pathString) {
        Path filePath = Paths.get(pathString);
        if (Files.isDirectory(filePath)) {
            throw new UnsupportedOperationException();
        }
        ContentfulBlob newBlob = new ContentfulBlob(filePath);
        staged.add(newBlob.getContentlessBlob());
    }
    public void rm(String pathString) {
        throw new UnsupportedOperationException();
    }
    public void createBranch(String branchName) {
        Branches.create(branchName);
    }
    public void deleteBranch(String branchName) {
        Branches.delete(branchName);
    }
    public void commit(String commitMessage) throws NothingToCommitException, EmptyCommitMessageException {
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
    public List<String> log() {
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
    public List<String> merge(String branchName) throws MergeWhenStagedNotEmptyException {
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
                if (!oldBlob.getSHA().equals(newBlob.getSHA())) {
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

    private void mergeBlobs(ContentlessBlob oldBlob, ContentlessBlob newBlob) {
        throw new UnsupportedOperationException();
    }
}
