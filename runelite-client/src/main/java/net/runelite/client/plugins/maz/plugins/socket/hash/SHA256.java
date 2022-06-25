/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.client.plugins.maz.plugins.socket.hash;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class SHA256 {
    public static String encrypt(String origin) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] originBytes = origin.getBytes(StandardCharsets.UTF_8);
            byte[] encodedBinary = digest.digest(originBytes);
            return new String(Base64.getEncoder().encode(encodedBinary));
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return origin;
        }
    }
}

