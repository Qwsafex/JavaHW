package vcs;

import java.io.Serializable;
import java.math.BigInteger;

abstract class GitObject implements Serializable{
    @SuppressWarnings("WeakerAccess")
    protected static final String SHA1 = "SHA-1";

    /**
     * Computes and returns SHA-1 hash of this object.
     * @return hash String generated with SHA-1 algorithm
     */
    public abstract String getSHA();

    String byteArrayToHex(byte[] array) {
        return (new BigInteger(1, array)).toString(16);
    }
}
