package vcs;

import java.io.IOException;

interface SHARef {
    @SuppressWarnings("unused")
    GitObject getObject() throws IOException, ClassNotFoundException;
}
