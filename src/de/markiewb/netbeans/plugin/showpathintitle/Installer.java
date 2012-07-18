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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.*;
import org.netbeans.api.project.FileOwnerQuery;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectManager;
import org.netbeans.api.project.ProjectUtils;
import org.openide.awt.StatusDisplayer;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataShadow;
import org.openide.modules.ModuleInstall;
import org.openide.nodes.Node;
import org.openide.windows.Mode;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

/**
 * Shows the path of the current editor or node in the title of the main window.
 * Additionally it shows <ul> <li>the project name </li> <li>the NetBeans
 * version (only supported for NB &gt;=7.1)</li></ul>. It can be configured
 * using {@link ShowPathInTitleOptions).
 *
 * http://code.google.com/p/show-path-in-title-netbeans-module/
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
                ShowPathInTitleOptions options = ShowPathInTitleOptions.load();
                String title = new Generator().doit(options);
                WindowManager.getDefault().getMainWindow().setTitle(title);
            }

            private void showInStatusBar(Object data) {
                if (null != data) {
                    StatusDisplayer.getDefault().setStatusText(data.toString());
                } else {
                    StatusDisplayer.getDefault().setStatusText("");

                }
            }

            private void showSystemProperties() {
                Iterable<String> keys = new TreeSet<String>(System.getProperties().stringPropertyNames());
                for (String key : keys) {
                    System.out.println(key + "=" + System.getProperty(key));
                }
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
