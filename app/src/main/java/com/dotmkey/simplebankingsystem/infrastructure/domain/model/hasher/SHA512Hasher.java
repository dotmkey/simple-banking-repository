package com.dotmkey.simplebankingsystem.infrastructure.domain.model.hasher;

import com.dotmkey.simplebankingsystem.domain.model.Hasher;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA512Hasher implements Hasher {

    private static final String ALGORITHM = "SHA-512";

    @Override
    public String hash(String message) {
        String digest;
        try {
            MessageDigest md = MessageDigest.getInstance(ALGORITHM);
            byte[] bytes = md.digest(message.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte aByte : bytes) {
                sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
            }
            digest = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(String.format("Algorithm %s is not supported", ALGORITHM));
        }

        return digest;
    }
}
