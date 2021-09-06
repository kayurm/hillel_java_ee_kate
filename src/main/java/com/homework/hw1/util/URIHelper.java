package com.homework.hw1.util;

import java.net.URI;

public class URIHelper {

    public static boolean isSingleKeyInUri(URI uri, String endpointName) {
        String lastSegment = getLastPathSegment(uri);
        return !lastSegment.equals(endpointName);
    }

    public static Integer getIdFromUri(URI uri) {
        return Integer.parseInt(getLastPathSegment(uri));
    }

    private static String getLastPathSegment(URI uri) {
        String path = uri.getPath();
        return path.substring(path.lastIndexOf('/') + 1);
    }
}
