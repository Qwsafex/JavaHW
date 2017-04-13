package vcs;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;

interface Blob extends GitObject, Serializable{
    Path BLOB_DIR = Paths.get("blobs");

    String getPath();
    ContentfulBlob getContentfulBlob() throws IOException, ClassNotFoundException;
    ContentlessBlob getContentlessBlob();
}
