package vcs;

public class BlobSHARef implements SHARef {
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
    public GitObject getObject() {
        throw new UnsupportedOperationException();
    }
}
