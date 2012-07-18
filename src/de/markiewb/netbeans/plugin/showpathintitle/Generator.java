/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.markiewb.netbeans.plugin.showpathintitle;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import org.netbeans.api.project.FileOwnerQuery;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectUtils;
import org.openide.awt.StatusDisplayer;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataShadow;
import org.openide.nodes.Node;
import org.openide.windows.Mode;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

/**
 *
 * @author markiewb
 */
public class Generator {

    public static final String SEPARATOR = " - ";

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



    private String createProjectName(ContextObject c) {
        String result = null;
        if (null != c.getProject()) {
            result = getProjectName(c.getProject());
        }

        if (null != c.getDataObject()) {
            final FileObject primaryFile = getFileObjectWithShadowSupport(c.getDataObject());
            result = getProjectName(primaryFile);
        }
        
        //try to lookup the project by traversal of the parents 
        for (Node node = c.getNode(); null!=node; node=node.getParentNode()) {
            Project project = node.getLookup().lookup(Project.class);
            if (null!=project){
                return getProjectName(project);
            }
        }
        return result;
    }

    private String createVersion(ContextObject c) {
        // version only available for netbeans >=7.1
        return System.getProperty("netbeans.productversion");
    }

    private String createProjectDir(ContextObject c) {
        String projectDir = null;
        if (null != c.getProject()) {
            projectDir = getProjectDirectory(c.getProject());
        }

        if (null != c.getDataObject()) {
            final FileObject primaryFile = getFileObjectWithShadowSupport(c.getDataObject());
            projectDir = getProjectDirectory(primaryFile);
        }
        return projectDir;
    }

    private String getFallbackForFileName(String fileName, String projectDir, Node node) {
        String result = "";
        if (StringUtils.isEmpty(fileName) && !StringUtils.isEmpty(projectDir)) {
            //show projectDir as fallback
            result = projectDir;
        }
        if (StringUtils.isEmpty(fileName) && null != node) {
            //show node label as further fallback
            result = (node.getDisplayName());
        }
        return result;
    }

    private String createAbsoluteFilename(ContextObject c) {
        String absoluteFileName = null;
        if (null != c.getDataObject()) {
            final FileObject primaryFile = getFileObjectWithShadowSupport(c.getDataObject());
            if (null != primaryFile.getPath()) {

                absoluteFileName = primaryFile.getPath();
            }

            //support selected items in jars
            if (null != FileUtil.getArchiveFile(primaryFile)) {
                String fullJARPath = FileUtil.getArchiveFile(primaryFile).getPath();
                String archiveFileName = primaryFile.getPath();
                boolean hasFileName = null != archiveFileName && !"".equals(archiveFileName);
                if (hasFileName) {
                    absoluteFileName = fullJARPath + "/" + archiveFileName;
                } else {
                    absoluteFileName = fullJARPath;
                }
            }

        }
        if (StringUtils.isEmpty(absoluteFileName)) {
            absoluteFileName = getFallbackForFileName(absoluteFileName, createProjectDir(c), c.getNode());
        }
        return absoluteFileName;
    }

    private String createFilename(ContextObject c) {
        String result = null;
        if (null != c.getDataObject()) {
            final FileObject primaryFile = getFileObjectWithShadowSupport(c.getDataObject());
            if (null != primaryFile.getNameExt()) {

                result = primaryFile.getNameExt();
            }
        }
        if (StringUtils.isEmpty(result)) {
            result = getFallbackForFileName(result, createProjectDir(c), c.getNode());
        }
        return result;
    }

    private String createRelativeFilename(ContextObject c) {

        String absoluteFileName = createAbsoluteFilename(c);
        String relativeFileName = null;

        String projectDir = createProjectDir(c);
        //show relative path, when project dir is in selected path
        //show no relative path, when project dir equals selected path
        boolean isRelativePath = null != absoluteFileName && null != projectDir && absoluteFileName.startsWith(projectDir) && !absoluteFileName.equals(projectDir);
        if (isRelativePath) {
            //create and use relative file name
            relativeFileName = absoluteFileName.substring(projectDir.length());
            //fileName = relativeFileName;
        } else {
            //fallback, if not relative
            relativeFileName = absoluteFileName;
        }

        //fallback
        if (StringUtils.isEmpty(relativeFileName)) {
            relativeFileName = getFallbackForFileName(relativeFileName, createProjectDir(c), c.getNode());
        }
        return relativeFileName;
    }

    public String doit(ShowPathInTitleOptions options) {
        TopComponent activeTC = null;
        if (options.useNodeAsReference) {
            activeTC = TopComponent.getRegistry().getActivated();
        }
        if (options.useEditorAsReference) {
            activeTC = getCurrentEditor();
        }
        if (null == activeTC) {
            return "";
        }
        
        DataObject dataObject = activeTC.getLookup().lookup(DataObject.class);
        Project project = activeTC.getLookup().lookup(Project.class);
        Node node = activeTC.getLookup().lookup(Node.class);
        ContextObject c = new ContextObject(project, dataObject, node);

        String[] split = options.format.split(" ");

        Map<String, String> map = new HashMap<String, String>();
        map.put("${project}", StringUtils.defaultIfEmpty(createProjectName(c)));
        map.put("${project_dir}", StringUtils.defaultIfEmpty(createProjectDir(c)));
        map.put("${filename}", StringUtils.defaultIfEmpty(createFilename(c)));
        map.put("${filename_abs}", StringUtils.defaultIfEmpty(createAbsoluteFilename(c)));
        map.put("${filename_rel}", StringUtils.defaultIfEmpty(createRelativeFilename(c)));
        map.put("${version}", StringUtils.defaultIfEmpty(createVersion(c)));

        //dynamic replace of user-defined patterns 
        Set<String> list = new LinkedHashSet<String>();
        for (String pattern : split) {
            String value = map.get(pattern);
            list.add(value);
        }
        return StringUtils.defaultIfEmpty(StringUtils.join(list, SEPARATOR), "NetBeans");
    }
}
