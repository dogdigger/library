package com.elias.config;

/**
 * @author chengrui
 * <p>create at: 2020/9/25 6:45 下午</p>
 * <p>description: </p>
 */
public class AdminInfoContext {
    private static final ThreadLocal<String> admin = new ThreadLocal<>();

    public static void setCurrentAdmin(String adminAccount) {
        admin.set(adminAccount);
    }

    public static String getCurrentAdmin() {
        return admin.get();
    }
}
