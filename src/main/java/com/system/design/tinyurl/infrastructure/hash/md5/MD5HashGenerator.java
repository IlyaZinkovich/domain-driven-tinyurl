package com.system.design.tinyurl.infrastructure.hash.md5;

import com.system.design.tinyurl.domain.hash.HashGenerator;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

public class MD5HashGenerator implements HashGenerator {

    private static final Charset SUPPORTED_CHARSET = Charset.forName("UTF-8");

    private final MessageDigest messageDigest;

    public MD5HashGenerator() {
        try {
            this.messageDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new MD5HashingAlgorithmIsNotFoundException(e);
        }
    }

    @Override
    public String hash(String input) {
        final byte[] bytesOfMessage = input.getBytes(SUPPORTED_CHARSET);
        final byte[] digest = messageDigest.digest(bytesOfMessage);
        final byte[] bytesToEncode = Arrays.copyOfRange(digest, 0, 6);
        final byte[] encodedBytes = Base64.getUrlEncoder().encode(bytesToEncode);
        return new String(encodedBytes, SUPPORTED_CHARSET);
    }
}
