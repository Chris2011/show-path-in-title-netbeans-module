/**
 * Copyright 2016 markiewb
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package de.markiewb.netbeans.plugin.showpathintitle;

/**
 *
 * @author markiewb
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

    public static boolean isEmpty(String string) {
        return null == string || "".equals(string);
    }

    public static String join_nullignore(Iterable<String> list, String separator) {
        boolean first = true;
        StringBuilder a = new StringBuilder();
        for (String string : list) {
            if (isEmpty(string)) {
                continue;
            }
            if (!first) {
                a.append(separator);
            }
            a.append(string);
            first = false;
        }
        return a.toString();
    }
}
