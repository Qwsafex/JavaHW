package vcs;

import java.io.Serializable;

class LightBlob implements Serializable{
    private String path;
    private String hash;

    LightBlob(String path, String hash) {
        this.path = path;
        this.hash = hash;
    }

    String getHash() {
        return hash;
    }

    String getPath() {
        return path;
    }

}
