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
import org.netbeans.api.project.ProjectUtils;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataShadow;
import org.openide.modules.ModuleInstall;
import org.openide.nodes.Node;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

/**
 * Shows the path of the current {@link DataObject} in the title of the main
 * window. Additionally it shows <ul> <li>the project name </li> <li>the
 * NetBeans version (only supported for NB &gt;=7.1)</li> </ul>
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

                DataObject dataObject = activeTC.getLookup().lookup(DataObject.class);
                Project project = activeTC.getLookup().lookup(Project.class);
                Node node = activeTC.getLookup().lookup(Node.class);

                String projectName = null;
                String fileName = null;
                if (null != dataObject) {
                    final FileObject primaryFile = getFileObjectWithShadowSupport(dataObject);

                    projectName = getProjectName(primaryFile);
                    final File toFile = FileUtil.toFile(primaryFile);
                    //TODO optional support for relative path within project
                    fileName = toFile.getAbsolutePath();
                } else {
                    //else get the name
                    String result = activeTC.getDisplayName();
                    final String displayName = activeTC.getDisplayName();
                    if (result != null) {
                        fileName = result;
                    }
                    if (displayName != null) {
                        fileName = displayName;
                    }
                    if (null != node) {
                        fileName = node.getDisplayName();
                    }
                }

                //version only available for netbeans >=7.1
                final String version = System.getProperty("netbeans.productversion");
                final String projectNameFromProject = getProjectName(project);
                if (null != projectNameFromProject) {
                    projectName = projectNameFromProject;
                }
                Set<String> list = new LinkedHashSet<String>();
                ShowPathInTitleOptions options = ShowPathInTitleOptions.load();
                if (options.showProjectName) {
                    list.add(projectName);
                }
                if (options.showFileName) {
                    list.add(fileName);
                }
                if (options.showIDEVersion) {
                    list.add(version);
                }

                WindowManager.getDefault().getMainWindow().setTitle(StringUtils_join_nullignore(list, " - "));

            }

            private void showSystemProperties() {
                Iterable<String> keys = new TreeSet<String>(System.getProperties().stringPropertyNames());
                for (String key : keys) {
                    System.out.println(key + "=" + System.getProperty(key));
                }
            }

            private String getProjectName(final Project project) {
                try {
                    return ProjectUtils.getInformation(project).getDisplayName();
                } catch (Exception e) {
                    //ignore the exception
                    return null;
                }
            }

            private String getProjectName(final FileObject primaryFile) {
                try {
                    return ProjectUtils.getInformation(FileOwnerQuery.getOwner(primaryFile)).getDisplayName();
                } catch (Exception e) {
                    //ignore the exception
                    return null;
                }
            }

            private String StringUtils_join_nullignore(Iterable<String> list, String separator) {
                boolean first = true;
                String a = "";
                for (String string : list) {
                    if (null == string) {
                        continue;
                    }
                    if (!first) {
                        a += separator;
                    }

                    a += string;
                    first = false;
                }
                return a;
            }

            private FileObject getFileObjectWithShadowSupport(DataObject dataObject) {
                if (dataObject instanceof DataShadow) {
                    DataShadow dataShadow = (DataShadow) dataObject;
                    return dataShadow.getOriginal().getPrimaryFile();
                }
                return dataObject.getPrimaryFile();
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
