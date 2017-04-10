package vcs;

import java.io.Serializable;

public class Branch implements CommitRef, Serializable {
    private CommitSHARef headCommit;
    private String name;


    public Branch(String name, CommitSHARef headCommit) {
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
    public Commit getCommit() {
        return headCommit.getCommit();
    }

    /**
     *
     * @param commit
     * @return new head
     */
    @Override
    public CommitRef addCommitAfter(Commit commit) {
        headCommit = commit.getSHARef();
        return this;
    }

}
