package com.elias.config;

/**
 * @author chengrui
 * <p>create at: 2020/9/5 10:20 上午</p>
 * <p>description: </p>
 */
public final class PathDefinition {
    private PathDefinition(){}

    public static final String URI_API_AUTH = "/api/auth";

    public static final String URI_API_CLIENT = URI_API_AUTH + "/client";

    public static final String URI_API_USER = URI_API_AUTH + "/user";

    public static final String URI_API_TOKEN = URI_API_AUTH + "/token";
}
