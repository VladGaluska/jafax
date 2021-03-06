package org.dxworks.dxplatform.plugins.insider.utils;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

public class MapUtils {

    public static Map<String, Integer> sortMapByValuesDesc(Map<String, Integer> unsortedMap) {
        return unsortedMap
                .entrySet()
                .stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                        LinkedHashMap::new));
    }
}
