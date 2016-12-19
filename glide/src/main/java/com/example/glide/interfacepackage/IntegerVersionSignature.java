package com.example.glide.interfacepackage;

import com.bumptech.glide.load.Key;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

/**
 * 使用自定义Signature
 */
public class IntegerVersionSignature implements Key {

    private int currentVersion;

    public IntegerVersionSignature(int currentVersion) {
        this.currentVersion = currentVersion;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof IntegerVersionSignature) {
            IntegerVersionSignature other = (IntegerVersionSignature) o;
            return currentVersion == other.currentVersion;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return currentVersion;
    }

    @Override
    public void updateDiskCacheKey(MessageDigest messageDigest) throws UnsupportedEncodingException {
//        messageDigest.update(ByteBuffer.allocate(Integer.SIZE)
//                .putInt(signature).array());
    }
}
