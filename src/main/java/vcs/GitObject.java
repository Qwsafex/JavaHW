package vcs;

import java.io.Serializable;

public interface GitObject extends Serializable{
    SHARef getSHARef();
}
