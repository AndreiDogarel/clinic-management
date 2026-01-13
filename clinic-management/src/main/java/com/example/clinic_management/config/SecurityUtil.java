package com.example.clinic_management.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

public final class SecurityUtil {

    private SecurityUtil() {}

    public static String currentEmail() {
        Authentication a = SecurityContextHolder.getContext().getAuthentication();
        if (a == null || a.getName() == null) {
            return null;
        }
        return a.getName();
    }

    public static boolean hasRole(String role) {
        Authentication a = SecurityContextHolder.getContext().getAuthentication();
        if (a == null) {
            return false;
        }
        String wanted = "ROLE_" + role;
        for (GrantedAuthority ga : a.getAuthorities()) {
            if (wanted.equals(ga.getAuthority())) {
                return true;
            }
        }
        return false;
    }
}
