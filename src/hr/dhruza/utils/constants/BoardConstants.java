package hr.dhruza.utils.constants;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BoardConstants {
    public static final Map<Integer, Integer> BOMBS = Stream.of(new Integer[][]{
            {  10, 3 },
            { 17, 7 },
            { 62, 19 },
            { 56, 34 },
            { 77, 55 },
            { 70, 60 },
            { 45, 36 },
    }).collect(Collectors.toMap(data -> (Integer) data[0], data -> (Integer) data[1]));

    public static final Map<Integer, Integer> WINGS = Stream.of(new Integer[][]{
            { 8, 14 },
            { 15, 31 },
            { 30, 42 },
            { 41, 65 },
            { 50, 74 }
    }).collect(Collectors.toMap(data -> (Integer) data[0], data -> (Integer) data[1]));

    private BoardConstants(){}

}
