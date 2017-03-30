package vcs;

/**
 * Signals that exception inside VCS of some sort occurred.
 */
public class VCSException extends Exception {
    VCSException(){}
    VCSException(String s) {
        super(s);
    }
}
