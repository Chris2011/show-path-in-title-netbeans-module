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

                TopComponent activeTC = null;
                if (options.useNodeAsReference) {
                    activeTC = TopComponent.getRegistry().getActivated();
                }
                if (options.useEditorAsReference) {
                    activeTC = getCurrentEditor();
                }

                if (null == activeTC) {
                    return;
                }

                DataObject dataObject = activeTC.getLookup().lookup(DataObject.class);
                Project project = activeTC.getLookup().lookup(Project.class);

                String projectName = null;
                String projectDir = null;
                String fileName = null;
                if (null != dataObject) {
                    final FileObject primaryFile = getFileObjectWithShadowSupport(dataObject);
                    projectDir = getProjectDirectory(primaryFile);
                    projectName = getProjectName(primaryFile);


                    if (null != primaryFile.getPath()) {
                        fileName = primaryFile.getPath();
                    }

                    //support selected items in jars
                    if (null != FileUtil.getArchiveFile(primaryFile)) {
                        String fullJARPath = FileUtil.getArchiveFile(primaryFile).getPath();
                        String archiveFileName = primaryFile.getPath();
                        boolean hasFileName = null != archiveFileName && !"".equals(archiveFileName);
                        if (hasFileName) {
                            fileName = fullJARPath + "/" + archiveFileName;
                        } else {
                            fileName = fullJARPath;
                        }
                    }
                    if (null == projectName) {
                        projectName = getProjectName(project);
                    }
                    if (null == projectDir && null != project) {
                        projectDir = getProjectDirectory(project);
                    }

                    // create title
                    Set<String> list = new LinkedHashSet<String>();
                    if (options.showProjectName) {
                        list.add(projectName);
                    }

                    if (options.showFileName) {
                        //show relative path, when project dir is in selected path
                        //show no relative path, when project dir equals selected path
                        boolean isRelativePath = null != fileName && null != projectDir && fileName.startsWith(projectDir) && !fileName.equals(projectDir);
                        if (options.showRelativeFilename && isRelativePath) {
                            //create and use relative file name
                            String reducedFileName = fileName.substring(projectDir.length());
                            fileName = reducedFileName;
                        } else {
                            if (null == fileName && null != projectDir) {
                                //show projectDir as fallback
                                fileName = projectDir;
                            }
                        }
                        list.add(fileName);
                    }

                    if (options.showIDEVersion) {
                        // version only available for netbeans >=7.1
                        list.add(System.getProperty("netbeans.productversion"));
                    }

                    // set title
                    String title = StringUtils_join_nullignore(list, " - ");
                    WindowManager.getDefault().getMainWindow().setTitle(defaultIfEmpty(title, "NetBeans"));

                }
            }

            /**
             * Returns the original string if not empty or not null. Else return
             * the given default.
             */
            private String defaultIfEmpty(String string, String defaultStr) {
                if (null == string || "".equals(string)) {
                    return defaultStr;
                }
                return string;
            }

            private String getProjectDirectory(final FileObject primaryFile) {
                try {
                    Project project = ProjectUtils.getInformation(FileOwnerQuery.getOwner(primaryFile)).getProject();
                    return getProjectDirectory(project);
                } catch (Exception e) {
                    //ignore the exception
                    return null;
                }
            }

            private String getProjectDirectory(final Project project) {
                try {
                    FileObject projectDirectory = project.getProjectDirectory();
                    return projectDirectory.getPath();
                } catch (Exception e) {
                    //ignore the exception
                    return null;
                }
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
                    if (null == string || "".equals(string)) {
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

            /**
             * Gets the currently opened editor.
             */
            private TopComponent getCurrentEditor() {
                Set<? extends Mode> modes = WindowManager.getDefault().getModes();
                for (Mode mode : modes) {
                    if ("editor".equals(mode.getName())) {
                        return mode.getSelectedTopComponent();
                    }
                }
                return null;
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
