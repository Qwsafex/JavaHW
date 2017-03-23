package vcs;

/**
 * Signals that VCS files are absent when they should be present or their content is corrupted.
 */
public class VCSFilesCorruptedException extends VCSException {
    VCSFilesCorruptedException(String s) {
        super(s);
    }
}
