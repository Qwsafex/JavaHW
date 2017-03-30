package vcs;

import java.io.Serializable;

public interface Blob extends Serializable{
    BlobSHARef getSHA();
    String getPath();
    ContentfulBlob getContentfulBlob();
    ContentlessBlob getContentlessBlob();
}
