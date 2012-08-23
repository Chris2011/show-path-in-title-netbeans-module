/**
 * Copyright 2012 markiewb
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

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import java.util.Map;
import java.util.prefs.Preferences;
import org.openide.util.NbPreferences;

/**
 * Simple POJO for loading/saving options. Note: No setter/getter were
 * introduced for the ease of usage. It is a plain data holder.
 *
 * @author markiewb
 */
public class Options {

    public static final String SHOWFILENAME = "showFileName";
    public static final String SHOWIDEVERSION = "showIDEVersion";
    public static final String SHOWPROJECTNAME = "showProjectName";
    public static final String SHOWRELATIVEFILENAME = "showRelativeFilename";
    public static final String USENODEASREFERENCE = "useNodeAsReference";
    public static final String USEEDITORASREFERENCE = "useEditorAsReference";
    public static final String SIMPLE_MODE = "simpleMode";
    public static final String PROP_ADVANCEDMODE = "advancedMode";
    public static final String CUSTOMFORMAT = "format";
    public static final String CUSTOMFORMAT_DEFAULT = "${project} ${filename_rel} ${version}";
    public static final String PROP_SIMPLEMODE = "simpleMode";
    public boolean showProjectName;
    public boolean showFileName;
    public boolean showIDEVersion;
    public boolean showRelativeFilename;
    public boolean useNodeAsReference;
    public boolean useEditorAsReference;
    public boolean simpleMode;
    public String format;

    public String getDefinedFormat() {
        if (simpleMode) {
            StringBuilder sb = new StringBuilder();
            if (showProjectName) {
                sb.append("${project}");
                sb.append(" ");
            }
            if (showFileName) {
                if (showRelativeFilename) {
                    sb.append("${filename_rel}");
                } else {
                    sb.append("${filename_abs}");
                }
                sb.append(" ");
            }
            if (showIDEVersion) {
                sb.append("${version}");
                sb.append(" ");
            }
            return sb.toString();
        } else {
            return format;
        }
    }

    public static Options load() {
        Preferences pref = NbPreferences.forModule(Options.class);

        Options options = new Options();
        options.showProjectName = pref.getBoolean(SHOWPROJECTNAME, true);
        options.showFileName = pref.getBoolean(SHOWFILENAME, true);
        options.showIDEVersion = pref.getBoolean(SHOWIDEVERSION, true);
        options.showRelativeFilename = pref.getBoolean(SHOWRELATIVEFILENAME, true);
        options.useNodeAsReference = pref.getBoolean(USENODEASREFERENCE, true);
        options.useEditorAsReference = pref.getBoolean(USEEDITORASREFERENCE, false);
        options.format = pref.get(CUSTOMFORMAT, CUSTOMFORMAT_DEFAULT);
        options.simpleMode = pref.getBoolean(SIMPLE_MODE, true);

        return options;
    }

    public void save() {
        Preferences pref = NbPreferences.forModule(Options.class);
        pref.putBoolean(SIMPLE_MODE, simpleMode);
        pref.putBoolean(SHOWPROJECTNAME, showProjectName);
        pref.putBoolean(SHOWFILENAME, showFileName);
        pref.putBoolean(SHOWIDEVERSION, showIDEVersion);
        pref.putBoolean(SHOWRELATIVEFILENAME, showRelativeFilename);
        pref.putBoolean(USENODEASREFERENCE, useNodeAsReference);
        pref.putBoolean(USEEDITORASREFERENCE, useEditorAsReference);
        pref.put(CUSTOMFORMAT, format);
    }

    public void reset() {
        simpleMode = true;
        showProjectName = true;
        showFileName = true;
        showIDEVersion = true;
        showRelativeFilename = true;
        useNodeAsReference = true;
        useEditorAsReference = false;
        format = CUSTOMFORMAT_DEFAULT;
    }
}
