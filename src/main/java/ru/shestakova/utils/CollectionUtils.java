package ru.shestakova.utils;

import ru.shestakova.model.*;

import java.time.ZonedDateTime;
import java.util.ArrayDeque;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CollectionUtils {
    public static Collection generateRandomCollection(int booksAmount) {
        List<Book> books = Stream
                .generate (CollectionUtils::generateRandomBook)
                .limit (booksAmount)
                .collect (Collectors.toList ( ));

        return new Collection (new ArrayDeque<> (books));
    }

    public static Book generateRandomBook() {
        return new Book (
                getRandomString (10), getRandomString (5),
                1 + (int) (Math.random ( ) * 512),
                BookSize.values ( )[(int) (BookSize.values ( ).length * Math.random ( ))],
                BookColor.values ( )[(int) (BookColor.values ( ).length * Math.random ( ))],
                BookGenre.values ( )[(int) (1 + Math.random ( ) * (BookGenre.values ( ).length - 1))],
                ZonedDateTime.now());
    }

    private static String getRandomString(int width) {
        StringBuilder builder = new StringBuilder ( );
        for (int i = 0; i < width; i++) {
            builder.append (
                    (char) ((Math.random ( ) > 0.5D ? 'A' : 'a')
                            + ((char) (Math.random ( ) * ('z' - 'a'))))
            );
        }

        return builder.toString ( );
    }
}
