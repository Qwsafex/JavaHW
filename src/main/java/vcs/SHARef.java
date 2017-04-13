package vcs;

import java.io.IOException;
import java.io.Serializable;

interface SHARef extends Serializable{
    @SuppressWarnings("unused")
    GitObject getObject() throws IOException, ClassNotFoundException;
}
