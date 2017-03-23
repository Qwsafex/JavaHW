package vcs;

import java.io.Serializable;
import java.math.BigInteger;

@SuppressWarnings("WeakerAccess")
public abstract class GitObject implements Serializable{
    protected static final String SHA1 = "SHA-1";

    public abstract String getSHA();

    protected String byteArrayToHex(byte[] array) {
        return (new BigInteger(1, array)).toString(16);
    }
}
