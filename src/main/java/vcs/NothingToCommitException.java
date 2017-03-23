package vcs;

/**
 * Signals an attempt to commit when no files were staged for commit.
 */
public class NothingToCommitException extends VCSException {
    NothingToCommitException(String s) {
        super(s);
    }
}
