package vcs;

import java.io.IOException;

class BlobSHARef implements SHARef {
    private String blobSHA;

    BlobSHARef(String blobSHA) {
        this.blobSHA = blobSHA;
    }

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
        return (GitObject) VCSFiles.readObject(Blob.BLOB_DIR.resolve(blobSHA));
    }

    @Override
    public String toString() {
        return blobSHA;
    }
}
