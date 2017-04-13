package vcs;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Class that contains all VCS interface methods.
 */

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

    /**
     * Creates new VCS instance in current directory or loads one if it already exists.
     * @throws VCSFilesCorruptedException if VCS files are absent or corrupted
     * @throws IOException if for some reason VCS files cannot be read or written
     */
    public VCS() throws IOException, VCSFilesCorruptedException {
        VCSFiles.init();
        try {
            index = new RepoState(INDEX_FILENAME);
            staged = new RepoState(STAGED_FILENAME);
        } catch (ClassNotFoundException e) {
            throw new VCSFilesCorruptedException();
        }
        Path headPath = Paths.get(HEAD_FILENAME);
        if (!VCSFiles.exists(headPath)) {
            VCSFiles.create(headPath);
            Commit initial = new Commit("initial", new ArrayList<>(), null);
            try {
                VCSFiles.writeObject(headPath, Branches.create(MASTER_BRANCH, initial.getSHARef()));
            } catch (BranchAlreadyExistsException e) {
                throw new VCSFilesCorruptedException();
            }
        }
        try {
            head = CommitRef.readRef(HEAD_FILENAME);
        } catch (ClassNotFoundException e) {
            throw new VCSFilesCorruptedException();
        }
    }

    /**
     * Sets HEAD to revison and makes files consistent with revision state.
     * @param revision name of a commit or a branch to checkout to
     * @throws IOException if for some reason VCS files cannot be read or written
     * @throws BranchNotFoundException if passed revision is branch and there's no such branch
     * @throws VCSFilesCorruptedException if VCS files are absent or corrupted
     */
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
        // TODO: move to method of RepoState
        HashMap<String, ContentlessBlob> indexFiles = index.getFiles();
        for (Map.Entry<String, ContentlessBlob> blob : indexFiles.entrySet()) {
            byte[] content = blob.getValue().getContentfulBlob().getContent();
            VCSFiles.writeToRoot(Paths.get(blob.getKey()), content);
        }
        VCSFiles.writeObject(Paths.get(HEAD_FILENAME), head);
    }
    /**
     * Adds file to stage for the next commit.
     * @param pathString file to add
     * @throws IOException if for some reason VCS files cannot be read or written
     */
    public void add(String pathString) throws IOException {
        Path filePath = Paths.get(pathString);
        if (Files.isDirectory(filePath)) {
            throw new UnsupportedOperationException();
        }
        ContentfulBlob newBlob = new ContentfulBlob(filePath);
        staged.add(newBlob.getContentlessBlob());
    }
    /**
     * Creates new branch pointing to same commit as HEAD.
     * @param branchName name for a newly created branch
     * @throws IOException if for some reason VCS files cannot be read or written
     * @throws BranchAlreadyExistsException if branchName already exists
     */
    public void createBranch(String branchName) throws IOException, BranchAlreadyExistsException {
        Branches.create(branchName, head.getCommitSHA())
    }

    /**
     * Deletes specified branch.
     * @param branchName branch to be deleted
     * @throws IOException if for some reason VCS files cannot be read or written
     * @throws DeleteActiveBranchException if branchName is active branch
     * @throws VCSFilesCorruptedException if VCS files are absent or corrupted
     * @throws BranchNotFoundException if branchName does not exist
     */    public void deleteBranch(String branchName) throws BranchNotFoundException, IOException,
            VCSFilesCorruptedException, DeleteActiveBranchException {
        if (head.equals(Branches.get(branchName))) {
            throw new DeleteActiveBranchException(branchName);
        }
        Branches.delete(branchName);
    }
    /**
     * Creates new commit from all staged files.
     * @param commitMessage a commit message
     * @throws NothingToCommitException if there no files staged for commit
     * @throws IOException if for some reason VCS files cannot be read or written
     */
    public void commit(String commitMessage) throws NothingToCommitException, EmptyCommitMessageException, IOException {
        if (commitMessage.isEmpty()) {
            throw new EmptyCommitMessageException();
        }
        if (staged.empty()) {
            throw new NothingToCommitException();
        }
        index.updateWith(staged);
        Commit commit = new Commit(commitMessage, staged.getFiles().values().stream().collect(Collectors.toList()), head.getCommitSHA());
        staged.clear();
        head = head.addCommitAfter(commit);
    }
    /**
     * Returns list of all commits in the current branch.
     * @return list of all commits in the current branch
     * @throws IOException if for some reason VCS files cannot be read or written
     */
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
    /**
     * Merges branchName into current branch.
     *
     * Conflicting file contents are merged with separator.
     * @param branchName name of branch to be merge into current
     * @return list of conflicting paths
     * @throws BranchNotFoundException if there's no branch with the specified name
     * @throws VCSFilesCorruptedException if one of .vcs/ files is absent or contains corrupted data
     * @throws IOException if for some reason VCS files cannot be read or written
     */
    public List<String> merge(String branchName) throws MergeWhenStagedNotEmptyException, VCSFilesCorruptedException,
            BranchNotFoundException, IOException, ClassNotFoundException {
        if (!staged.empty()) {
            throw new MergeWhenStagedNotEmptyException();
        }
        Branch mergingBranch = Branches.get(branchName);
        RepoState curState = index;
        RepoState mergingState = RepoState.getFromCommit(mergingBranch.getCommit());
        Map<String, ContentlessBlob> curBlobs = curState.getFiles();
        List<ContentlessBlob> mergingBlobs = mergingState.getFiles().values().stream().collect(Collectors.toList());

        List<String> conflicts = new ArrayList<>();
        for (ContentlessBlob newBlob : mergingBlobs) {
            if (curBlobs.containsKey(newBlob.getPath())) {
                ContentlessBlob oldBlob = curBlobs.get(newBlob.getPath());
                if (!oldBlob.getSHARef().equals(newBlob.getSHARef())) {
                    // TODO: Bad from design point of view
                    mergeBlobs(oldBlob, newBlob);
                    conflicts.add(oldBlob.getPath());
                }
            }
            else {
                staged.add(newBlob);
            }
        }

        return conflicts;
    }

    private void mergeBlobs(ContentlessBlob oldBlob, ContentlessBlob newBlob) throws IOException, ClassNotFoundException {
        // TODO: looks not right
        Path path = Paths.get(oldBlob.getPath());
        Files.write(path, oldBlob.getContentfulBlob().getContent());
        Files.write(path, MERGED_FILES_SEPARATOR, StandardOpenOption.APPEND);
        Files.write(path, newBlob.getContentfulBlob().getContent(), StandardOpenOption.APPEND);
    }
}
