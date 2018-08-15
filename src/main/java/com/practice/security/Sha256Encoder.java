package com.practice.security;

import com.google.common.hash.Hashing;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.nio.charset.StandardCharsets;

public class Sha256Encoder implements PasswordEncoder {
    @Override
    public String encode(CharSequence charSequence) {
        return Hashing.sha256()
                .hashString(charSequence, StandardCharsets.UTF_8)
                .toString().toUpperCase();
    }

    @Override
    public boolean matches(CharSequence charSequence, String s) {
        String hash = encode(charSequence);
        boolean res = hash.equals(s);
        return res;
    }
}
