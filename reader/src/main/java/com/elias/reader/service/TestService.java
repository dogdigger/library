package com.elias.reader.service;

import com.elias.common.annotation.LogIt;
import org.springframework.stereotype.Service;

/**
 * @author chengrui
 * <p>create at: 2020/8/6 5:47 下午</p>
 * <p>description: </p>
 */
public final class TestService implements Test{

    @LogIt
    @Override
    public String sayHello() {
        return "hello";
    }
}
