package com.elias.mq.exception;
import java.io.Closeable;
/**
 * @author chengrui
 * <p>create at: 2020/8/5 5:30 下午</p>
 * <p>description: </p>
 */
public class ResourceOne implements AutoCloseable{
    @Override
    public void close() throws Exception {
        System.out.println("执行ResourceOne的close方法");
    }

    public void use(){
        System.out.println("使用资源ResourceOne......");
    }
}
