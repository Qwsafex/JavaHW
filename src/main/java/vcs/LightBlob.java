package vcs;

import java.io.Serializable;

public class LightBlob implements Serializable{
    private String path;
    private String hash;

    public LightBlob(String path, String hash) {
        this.path = path;
        this.hash = hash;
    }

    public String getHash() {
        return hash;
    }

    public String getPath() {
        return path;
    }

}
