package vcs;

import java.io.Serializable;
import java.math.BigInteger;

interface GitObject extends Serializable{
    String SHA1 = "SHA-1";
    SHARef getSHARef();

    static String byteArrayToHex(byte[] array) {
        return (new BigInteger(1, array)).toString(16);
    }
}
