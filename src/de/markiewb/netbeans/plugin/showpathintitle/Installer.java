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
import java.util.*;
import org.openide.awt.StatusDisplayer;
import org.openide.modules.ModuleInstall;
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
                Options options = Options.load();
                String title = new TitleGenerator().createTitle(options);
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
