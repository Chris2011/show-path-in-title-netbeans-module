/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.markiewb.netbeans.plugin.showpathintitle;

import java.util.prefs.Preferences;
import org.openide.util.NbPreferences;

/**
 * Simple POJO for loading/saving options. Note: No setter/getter were introduced for the ease of usage. It is a plain
 * data holder.
 *
 * @author markiewb
 */
public class ShowPathInTitleOptions {

    public static final String SHOWFILENAME = "showFileName";
    public static final String SHOWIDEVERSION = "showIDEVersion";
    public static final String SHOWPROJECTNAME = "showProjectName";

    private ShowPathInTitleOptions() {
        //NOP
    }
    public boolean showProjectName;
    public boolean showFileName;
    public boolean showIDEVersion;

    public static ShowPathInTitleOptions load() {
        Preferences pref = NbPreferences.forModule(ShowPathInTitleOptions.class);

        ShowPathInTitleOptions options = new ShowPathInTitleOptions();
        options.showProjectName = pref.getBoolean(SHOWPROJECTNAME, true);
        options.showFileName = pref.getBoolean(SHOWFILENAME, true);
        options.showIDEVersion = pref.getBoolean(SHOWIDEVERSION, true);
        return options;
    }

    public void save() {
        Preferences pref = NbPreferences.forModule(ShowPathInTitleOptions.class);
        pref.putBoolean(SHOWPROJECTNAME, showProjectName);
        pref.putBoolean(SHOWFILENAME, showFileName);
        pref.putBoolean(SHOWIDEVERSION, showIDEVersion);
    }

    public void reset() {
        showProjectName = true;
        showFileName = true;
        showIDEVersion = true;
    }
}
