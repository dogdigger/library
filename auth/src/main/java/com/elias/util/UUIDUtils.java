package com.elias.util;

import java.util.UUID;

/**
 * @author chengrui
 * <p>create at: 2020/9/2 8:18 下午</p>
 * <p>description: </p>
 */
public final class UUIDUtils {
    private UUIDUtils(){}

    public static UUID randomUUID(){
        return UUID.randomUUID();
    }
}
