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

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import org.openide.util.Exceptions;
import org.openide.util.NbPreferences;

/**
 * Simple POJO for loading/saving options. Note: No setter/getter were
 * introduced for the ease of usage. It is a plain data holder.
 *
 * @author markiewb
 */
public class ShowPathInTitleOptions {

    private static boolean default_showProjectGroup = true;
    private static boolean default_showProjectName = true;
    private static boolean default_showFileName = true;
    private static boolean default_showIDEVersion = true;
    private static boolean default_showRelativeFilename = true;
    private static boolean default_useNodeAsReference = true;
    private static boolean default_useEditorAsReference = false;
    public static final String SHOWFILENAME = "showFileName";
    public static final String SHOWIDEVERSION = "showIDEVersion";
    public static final String SHOWPROJECTGROUP = "showProjectGroup";
    public static final String SHOWPROJECTNAME = "showProjectName";
    public static final String SHOWRELATIVEFILENAME = "showRelativeFilename";
    public static final String USENODEASREFERENCE = "useNodeAsReference";
    public static final String USEEDITORASREFERENCE = "useEditorAsReference";

    private ShowPathInTitleOptions() {
        //NOP
    }

    public boolean showProjectGroup;
    public boolean showProjectName;
    public boolean showFileName;
    public boolean showIDEVersion;
    public boolean showRelativeFilename;
    public boolean useNodeAsReference;
    public boolean useEditorAsReference;

    public static ShowPathInTitleOptions loadFrom(Preferences pref) {

        ShowPathInTitleOptions options = new ShowPathInTitleOptions();
        options.showProjectGroup = pref.getBoolean(SHOWPROJECTGROUP, default_showProjectGroup);
        options.showProjectName = pref.getBoolean(SHOWPROJECTNAME, default_showProjectName);
        options.showFileName = pref.getBoolean(SHOWFILENAME, default_showFileName);
        options.showIDEVersion = pref.getBoolean(SHOWIDEVERSION, default_showIDEVersion);
        options.showRelativeFilename = pref.getBoolean(SHOWRELATIVEFILENAME, default_showRelativeFilename);
        options.useNodeAsReference = pref.getBoolean(USENODEASREFERENCE, default_useNodeAsReference);
        options.useEditorAsReference = pref.getBoolean(USEEDITORASREFERENCE, default_useEditorAsReference);
        return options;
    }

    public void saveTo(Preferences pref) {
        pref.put("version", "2.0.0");
        pref.putBoolean(SHOWPROJECTGROUP, showProjectGroup);
        pref.putBoolean(SHOWPROJECTNAME, showProjectName);
        pref.putBoolean(SHOWFILENAME, showFileName);
        pref.putBoolean(SHOWIDEVERSION, showIDEVersion);
        pref.putBoolean(SHOWRELATIVEFILENAME, showRelativeFilename);
        pref.putBoolean(USENODEASREFERENCE, useNodeAsReference);
        pref.putBoolean(USEEDITORASREFERENCE, useEditorAsReference);
    }

    public void reset() {
        showProjectGroup = default_showProjectGroup;
        showProjectName = default_showProjectName;
        showFileName = default_showFileName;
        showIDEVersion = default_showIDEVersion;
        showRelativeFilename = default_showRelativeFilename;
        useNodeAsReference = default_useNodeAsReference;
        useEditorAsReference = default_useEditorAsReference;
    }
}
