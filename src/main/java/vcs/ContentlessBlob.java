package vcs;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

class ContentlessBlob implements Blob{
    @NotNull
    private String path;
    @NotNull
    private BlobSHARef blobRef;

    ContentlessBlob(@NotNull String path, @NotNull BlobSHARef ref) {

        this.path = path;
        this.blobRef = ref;
    }

    @Override
    public BlobSHARef getSHARef() {
        return blobRef;
    }

    @NotNull
    @Override
    public String getPath() {
        return path;
    }

    @NotNull
    @Override
    public ContentfulBlob getContentfulBlob() throws IOException, ClassNotFoundException {
        return (ContentfulBlob) VCSFiles.readObject(BLOB_DIR.resolve(blobRef.toString()));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ContentlessBlob that = (ContentlessBlob) o;

        return blobRef.equals(that.blobRef);

    }

    @Override
    public int hashCode() {
        int result = path.hashCode();
        result = 31 * result + blobRef.hashCode();
        return result;
    }

    @Override
    public ContentlessBlob getContentlessBlob() {
        return this;
    }
}
