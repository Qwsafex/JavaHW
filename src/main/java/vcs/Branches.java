package vcs;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

class Branches {
    static final Path BRANCHES_DIR = Paths.get("branches");
    static boolean exists(String branchName) {
        return VCSFiles.exists(getPath(branchName));
    }
    static Branch create(String branchName, CommitSHARef headCommit) throws IOException {
        Path branchPath = getPath(branchName);
        VCSFiles.create(branchPath);
        Branch newBranch = new Branch(branchName, headCommit);
        VCSFiles.writeObject(branchPath, newBranch);
        return newBranch;
    }
    static void delete(String branchName) throws IOException, BranchNotFoundException {
        if (!exists(branchName)) {
            throw new BranchNotFoundException(branchName);
        }
        VCSFiles.delete(getPath(branchName));
    }

    static Branch get(String branchName) throws BranchNotFoundException, IOException, VCSFilesCorruptedException {
        if (!exists(branchName)) {
            throw new BranchNotFoundException(branchName);
        }
        try {
            return (Branch) VCSFiles.readObject(getPath(branchName));
        } catch (ClassNotFoundException e) {
            throw new VCSFilesCorruptedException();
        }
    }

    private static Path getPath(String branchName) {
        return BRANCHES_DIR.resolve(branchName);
    }
}
