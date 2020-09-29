package com.elias.util;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author chengrui
 * <p>create at: 2020/9/22 5:17 下午</p>
 * <p>description: </p>
 */
public class Person {
    private String name;
    private Integer age;

    public Person(String name, Integer age){
        this.name = name;
        this.age = age;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Person) {
            System.out.println("equals invoked...");
            if (this == o) return true;
            Person person = (Person) o;
            return Objects.equals(name, person.name) &&
                    Objects.equals(age, person.age);
        }
        return false;
    }

    @Override
    public int hashCode() {
        System.out.println("hashCode invoked...");
        return Objects.hash(age);
    }

    public static void main(String[] args) {
        Person p1 = new Person("zhangsan", 23), p2 = new Person("lisi", 23);
        Set<Person> personSet = new HashSet<>();
        personSet.add(p1);
        personSet.add(p2);
    }
}
