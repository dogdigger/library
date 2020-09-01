package com.elias.mq.exception;

/**
 * @author chengrui
 * <p>create at: 2020/8/5 5:31 下午</p>
 * <p>description: </p>
 */
public class Test {
    public static void main(String[] args) {
        try(
                ResourceOne resourceOne = new ResourceOne();
                ResourceTwo resourceTwo = new ResourceTwo()) {
            System.out.println(1/0);
            resourceOne.use();
        } catch(Exception e) {
            System.out.println(e.getSuppressed());
        }
    }
}
