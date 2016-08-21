/**
 * Copyright 2013-2016 markiewb
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

import org.netbeans.api.project.FileOwnerQuery;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectInformation;
import org.netbeans.api.project.ProjectUtils;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataShadow;
import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.openide.windows.Mode;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

/**
 *
 * @author markiewb
 */
class PathUtil {

    public TitleBuilder getPath(ShowPathInTitleOptions options, TopComponent editor, Node[] selectedNodes) {
        Node node = null;
        Lookup.Provider provider = null;
        if (options.useNodeAsReference) {
            if (null != selectedNodes && selectedNodes.length >= 2) {
                // cannot decide which node is more important, so leave it
                TitleBuilder b = new TitleBuilder();
                b.setFileName("Multiple files selected");
                return b;
            }
            provider = firstOrNull(selectedNodes);
            node = firstOrNull(selectedNodes);
            if (provider != null && null != provider.getLookup().lookup(DataObject.class)) {
                // pick a better lookup target, since Node doesn't necessarily
                // have a Project etc., at least we hope so
                provider = provider.getLookup().lookup(DataObject.class);
            }
        } else if (options.useEditorAsReference) {
            provider = getEditor(editor);
            node = provider.getLookup().lookup(Node.class);
        }

        if (null == provider) {
            return new TitleBuilder();
        }

        DataObject dataObject = provider.getLookup().lookup(DataObject.class);
        Project project = provider.getLookup().lookup(Project.class);
        FileObject fileObject = provider.getLookup().lookup(FileObject.class);

        String projectName = null;
        String projectDir = null;
        String fileName = null;
        if (null != project) {
            projectName = getProjectName(project);
            projectDir = getProjectDirectory(project);
        }

        if (null != dataObject || null != fileObject) {

            final FileObject primaryFile;
            if (null != dataObject) {
                primaryFile = getFileObjectWithShadowSupport(dataObject);
            } else {
                primaryFile = fileObject;
            }
            Project owner = FileOwnerQuery.getOwner(primaryFile);
            if (owner != null) {
                ProjectInformation information = ProjectUtils.getInformation(owner);
                projectDir = getProjectDirectory(information);
                projectName = getProjectName(information);
            }

            if (null != primaryFile.getPath()) {
                fileName = primaryFile.getPath();
            }

            // support selected items in jars
            if (null != FileUtil.getArchiveFile(primaryFile)) {
                String fullJARPath = FileUtil.getArchiveFile(primaryFile).getPath();
                String archiveFileName = primaryFile.getPath();
                boolean hasFileName = !StringUtils.isEmpty(archiveFileName);
                if (hasFileName) {
                    fileName = fullJARPath + "/" + archiveFileName;
                } else {
                    fileName = fullJARPath;
                }
            }
        }

        TitleBuilder builder = new TitleBuilder();
        builder.setActiveProjectGroup(new ProjectGroupUtil().getActiveProjectGroup());
        builder.setProjectName(projectName);
        builder.setProjectDir(projectDir);
        builder.setFileName(fileName);
        builder.setNodeDisplayName(null != node ? node.getDisplayName() : null);

        return builder;
    }

    private Lookup.Provider getEditor(TopComponent editor) {
        Lookup.Provider activeTC;
        WindowManager wm = WindowManager.getDefault();
        boolean isEditor = editor != null && wm.isEditorTopComponent(editor);
        activeTC = isEditor ? editor : getCurrentEditor();
        return activeTC;
    }

    private String getProjectDirectory(final ProjectInformation projectInformation) {
        return getProjectDirectory(projectInformation.getProject());
    }

    private String getProjectDirectory(final Project project) {
        FileObject projectDirectory = project.getProjectDirectory();
        return projectDirectory.getPath();
    }

    private String getProjectName(final Project project) {
        return getProjectName(ProjectUtils.getInformation(project));
    }

    private String getProjectName(final ProjectInformation projectInformation) {
        return projectInformation.getDisplayName();
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
        WindowManager wm = WindowManager.getDefault();
        Mode editor = wm.findMode("editor");
        return editor.getSelectedTopComponent();
    }

    private <T> T firstOrNull(T[] array) {
        return array != null && array.length > 0 ? array[0] : null;
    }
}
