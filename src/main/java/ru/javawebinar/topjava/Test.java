package ru.javawebinar.topjava;

import java.util.stream.Stream;

public class Test {

    public static void main(){

        Stream.of("dd2", "aa2", "bb1", "bb3", "cc4")
            .map(s -> {
                System.out.println("map: " + s);
                return s.toUpperCase();
            })
            .anyMatch(s -> {
                System.out.println("anyMatch: " + s);
                return s.startsWith("A");
            });
    }
}
