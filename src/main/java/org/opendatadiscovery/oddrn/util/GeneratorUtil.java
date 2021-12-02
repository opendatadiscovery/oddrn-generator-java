package org.opendatadiscovery.oddrn.util;

public class GeneratorUtil {
    public static String escape(String value) {
        return value.replace("/", "\\\\");
    }

    public static String unescape(String value) {
        return value.replace("\\\\", "/");
    }
}
