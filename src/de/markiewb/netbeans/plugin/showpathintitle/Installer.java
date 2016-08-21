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

import static de.markiewb.netbeans.plugin.showpathintitle.StringUtils.defaultIfEmpty;
import static org.openide.windows.TopComponent.Registry.*;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;
import java.util.prefs.Preferences;

import org.openide.modules.ModuleInstall;
import org.openide.nodes.Node;
import org.openide.util.NbPreferences;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

/**
 * Shows the path of the current editor or node in the title of the main window.
 * Additionally it shows <ul> <li>the project name </li> <li>the NetBeans
 * version (only supported for NB &gt;=7.1)</li></ul>. It can be configured
 * using {@link ShowPathInTitleOptions).
 *
 * https://github.com/markiewb/show-path-in-title-netbeans-module
 *
 * @author markiewb
 */
public class Installer extends ModuleInstall implements PreferenceChangeListener, PropertyChangeListener {

    private static final long serialVersionUID = -2422104143131463778L;
    private Preferences pref = NbPreferences.forModule(ShowPathInTitleOptions.class);
    private String previousTitle;

    private void updateTitle(final TopComponent editor, final Node[] selectedNodes) {
        // on change set the title
        WindowManager.getDefault().invokeWhenUIReady(new Runnable() {
            @Override
            public void run() {
                ShowPathInTitleOptions options = ShowPathInTitleOptions.loadFrom(pref);
                TitleBuilder path = new PathUtil().getPath(options, editor, selectedNodes);
                WindowManager.getDefault().getMainWindow().setTitle(defaultIfEmpty(path.build(options), previousTitle));
            }
        });
    }

    @Override
    public void uninstalled() {
        //remove the listeners
        TopComponent.getRegistry().removePropertyChangeListener(this);
        pref.removePreferenceChangeListener(this);
        //reset to original title
        WindowManager.getDefault().getMainWindow().setTitle(previousTitle);
    }

    @Override
    public void restored() {
        final PropertyChangeListener listenerA = this;
        final PreferenceChangeListener listenerB = this;
        WindowManager.getDefault().invokeWhenUIReady(new Runnable() {
            @Override
            public void run() {
                // code to be invoked when system UI is ready

                //backup original title
                previousTitle = WindowManager.getDefault().getMainWindow().getTitle();

                TopComponent.getRegistry().addPropertyChangeListener(listenerA);
                pref.addPreferenceChangeListener(listenerB);

                // initial setting of title - emulate configuration change
                preferenceChange(null);
            }
        });
    }

    @Override
    public void preferenceChange(PreferenceChangeEvent evt) {
        TopComponent.Registry registry = TopComponent.getRegistry();
        updateTitle(registry.getActivated(), registry.getActivatedNodes());
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String propertyName = evt.getPropertyName();
        Object newValue = evt.getNewValue();
        if (evt.getSource() instanceof TopComponent.Registry) {
            TopComponent.Registry registry = (TopComponent.Registry) evt.getSource();

            ShowPathInTitleOptions options = ShowPathInTitleOptions.loadFrom(pref);
            if (options.useEditorAsReference && PROP_ACTIVATED.equals(propertyName)) {
                updateTitle((TopComponent) newValue, registry.getActivatedNodes());
            } else if (options.useNodeAsReference && PROP_ACTIVATED_NODES.equals(propertyName)) {
                updateTitle(registry.getActivated(), (org.openide.nodes.Node[]) newValue);
            }
        }
    }
}
