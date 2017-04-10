package vcs;

import java.io.IOException;

public interface SHARef {
    GitObject getObject() throws IOException, ClassNotFoundException;
}
