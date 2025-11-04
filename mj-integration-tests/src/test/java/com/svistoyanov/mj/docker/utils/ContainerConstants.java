package com.svistoyanov.mj.docker.utils;

public interface ContainerConstants {

    interface Mysql8 {
        String IMAGE_NAME = "docker.dev.brandmaker.com/cs/mysql";
        String VERSION    = "8.0.33";
        int    DB_PORT    = 3306;
    }
}
