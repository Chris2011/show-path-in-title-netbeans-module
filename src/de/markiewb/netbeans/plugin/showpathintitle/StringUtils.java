/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.markiewb.netbeans.plugin.showpathintitle;

/**
 *
 * @author Bender
 */
public class StringUtils {

    /**
     * Returns the original string if not empty or not null. Else return the
     * given default.
     */
    public static String defaultIfEmpty(String string, String defaultStr) {
        if (isEmpty(string)) {
            return defaultStr;
        }
        return string;
    }

    public static String defaultIfEmpty(String string) {
        return defaultIfEmpty(string, "");
    }

    public static boolean isEmpty(String string) {
        return null == string || "".equals(string);
    }

    public static String join(Iterable<String> list, String separator) {
        boolean first = true;
        StringBuilder sb = new StringBuilder(200);
        for (String string : list) {
            if (isEmpty(string)) {
                continue;
            }
            if (!first) {
                sb.append(separator);
            }
            sb.append(string);
            first = false;
        }
        return sb.toString();
    }
}
