package vcs;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Paths;

public interface CommitRef extends Serializable{
    static CommitRef readRef(String filename) throws IOException, ClassNotFoundException {
        return (CommitRef) VCSFiles.readObject(Paths.get(filename));
    }

    CommitSHARef getCommitSHA();
    Commit getCommit() throws IOException, ClassNotFoundException;
    CommitRef addCommitAfter(Commit commit) throws IOException;

}
