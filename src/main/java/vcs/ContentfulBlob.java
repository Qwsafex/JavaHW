package vcs;

import java.nio.file.Path;

public class ContentfulBlob implements Blob{
    private String path;
    private byte[] content;

    public ContentfulBlob(Path filePath) {
        throw new UnsupportedOperationException();
    }


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
        return this;
    }

    @Override
    public ContentlessBlob getContentlessBlob() {
        throw new UnsupportedOperationException();
    }
}
