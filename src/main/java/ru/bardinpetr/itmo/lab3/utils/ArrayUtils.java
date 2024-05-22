package ru.bardinpetr.itmo.lab3.utils;

import java.util.Iterator;
import java.util.Objects;
import java.util.stream.Stream;

public class ArrayUtils {

    public static <T> Stream<T> streamFromIterator(Iterator<T> src) {
        return Stream
                .generate(() -> src.hasNext() ? src.next() : null)
                .takeWhile(Objects::nonNull);
    }
}
