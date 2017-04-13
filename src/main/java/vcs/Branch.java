package vcs;

import java.io.IOException;
import java.io.Serializable;

class Branch implements CommitRef, Serializable {
    private CommitSHARef headCommit;
    private String name;


    Branch(String name, CommitSHARef headCommit) {
        this.name = name;
        this.headCommit = headCommit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Branch branch = (Branch) o;

        return name.equals(branch.name);

    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public CommitSHARef getCommitSHA() {
        return headCommit.getCommitSHA();
    }

    @Override
    public Commit getCommit() throws IOException, ClassNotFoundException {
        return headCommit.getCommit();
    }

    /**
     *
     * @param commit to be added after
     * @return new head
     */
    @Override
    public CommitRef addCommitAfter(Commit commit) {
        headCommit =  commit.getSHARef();
        writeToDisk();
        return this;
    }

    @Override
    public void writeToDisk() {
        throw new UnsupportedOperationException();
    }

}
