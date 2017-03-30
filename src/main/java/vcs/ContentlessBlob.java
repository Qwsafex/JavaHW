package vcs;

public class ContentlessBlob implements Blob{
    private String path;
    private BlobSHARef blobRef;

    @Override
    public BlobSHARef getSHA() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getPath() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ContentfulBlob getContentfulBlob() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ContentlessBlob that = (ContentlessBlob) o;

        if (path != null ? !path.equals(that.path) : that.path != null) return false;
        return blobRef != null ? blobRef.equals(that.blobRef) : that.blobRef == null;

    }

    @Override
    public int hashCode() {
        int result = path != null ? path.hashCode() : 0;
        result = 31 * result + (blobRef != null ? blobRef.hashCode() : 0);
        return result;
    }

    @Override
    public ContentlessBlob getContentlessBlob() {
        throw new UnsupportedOperationException();
    }
}
