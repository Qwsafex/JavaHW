package vcs;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class BlobSHARef implements SHARef {
    private static final Path BLOB_DIR = Paths.get("blobs");
    private String blobSHA;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BlobSHARef that = (BlobSHARef) o;

        return blobSHA != null ? blobSHA.equals(that.blobSHA) : that.blobSHA == null;

    }

    @Override
    public int hashCode() {
        return blobSHA != null ? blobSHA.hashCode() : 0;
    }

    @Override
    public GitObject getObject() throws IOException, ClassNotFoundException {
        return (GitObject) VCSFiles.readObject(BLOB_DIR.resolve(blobSHA));
    }
}
