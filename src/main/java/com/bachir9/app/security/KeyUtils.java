package com.bachir9.app.security;

import java.io.FileNotFoundException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

public class KeyUtils {
    private KeyUtils() {}

    public static PrivateKey loadPrivateKey(final String filePath) throws Exception {
        final var key = readKeyFromResource(filePath)
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s+", "");

        final byte[] decoded = Base64.getDecoder().decode(key);
        final var spec = new PKCS8EncodedKeySpec(decoded);
        return KeyFactory.getInstance("RSA").generatePrivate(spec);
    }

    public static PublicKey loadPublicKey(final String filePath) throws Exception {
        final var key = readKeyFromResource(filePath)
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s+", "");

        final byte[] decoded = Base64.getDecoder().decode(key);
        final var spec = new PKCS8EncodedKeySpec(decoded);
        return KeyFactory.getInstance("RSA").generatePublic(spec);
    }

    private static String readKeyFromResource(String filePath) throws Exception {
        try(final var is  = KeyUtils.class.getClassLoader().getResourceAsStream(filePath)){
            if(is == null){
                throw new IllegalArgumentException("File not found: " + filePath);
            }
            return new String(is.readAllBytes());
        }
    }
}
