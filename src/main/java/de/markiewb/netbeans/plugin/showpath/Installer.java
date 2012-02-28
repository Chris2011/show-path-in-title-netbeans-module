package de.markiewb.netbeans.plugin.showpath;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.modules.ModuleInstall;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

/**
 * Shows the path of the current {@link DataObject} in the title of the main window.
 *
 * @author markiewb
 */
public class Installer extends ModuleInstall {

    private static final long serialVersionUID = -2422104143131463778L;
    private PropertyChangeListener propertyChangeListener;

    public Installer() {
        propertyChangeListener = new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                TopComponent activeTC = TopComponent.getRegistry().getActivated();
                if (null == activeTC) {
                    return;
                }
                DataObject lookup = activeTC.getLookup().lookup(DataObject.class);
                String title;
                if (null != lookup) {
                    // if it is a file then get the absolute path
                    title = FileUtil.toFile(lookup.getPrimaryFile()).getAbsolutePath();
                } else {
                    //else get the name
                    String result = activeTC.getDisplayName();
                    title = (result != null ? result : activeTC.getDisplayName());
                }
                WindowManager.getDefault().getMainWindow().setTitle(title);

            }
        };
    }

    @Override
    public void uninstalled() {
        TopComponent.getRegistry().removePropertyChangeListener(propertyChangeListener);
    }

    @Override
    public void restored() {
        TopComponent.getRegistry().addPropertyChangeListener(propertyChangeListener);
    }
}
