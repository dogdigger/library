package com.elias.mq.utils;

import com.elias.mq.message.Person;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author chengrui
 * <p>create at: 2020/9/29 10:25 上午</p>
 * <p>description: </p>
 */
public class JSONUtils {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> String objToJson(T t) {
        try{
            return objectMapper.writeValueAsString(t);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    public static <T> T jsonToObj(String s, Class<T> clazz) throws IOException {
        return objectMapper.readValue(s, clazz);
    }

    public static <T> T objToJson(String s, TypeReference<T> reference) {
        try{
            return objectMapper.readValue(s, reference);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    public static void main(String[] args) {
//        Person person = new Person("张飞", '男', 34);
//        String json = objToJson(person);
//        System.out.println(json);
//        Person p = strToObj(json, Person.class);
//        System.out.println(p);
        List<Person> ls = new ArrayList<>();
        ls.add(new Person("张飞", '男', 34));
        ls.add(new Person("关羽", '男', 35));
        ls.add(new Person("刘备", '男', 44));

        String s = objToJson(ls);
        System.out.println(s);
        System.out.println(objToJson(s, new TypeReference<List<Person>>(){}));
    }
}
