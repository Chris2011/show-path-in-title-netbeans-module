/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.markiewb.netbeans.plugin.showpathintitle.strategies.impl;

import de.markiewb.netbeans.plugin.showpathintitle.ContextObject;
import de.markiewb.netbeans.plugin.showpathintitle.FileUtils;
import de.markiewb.netbeans.plugin.showpathintitle.StringUtils;
import de.markiewb.netbeans.plugin.showpathintitle.strategies.Strategy;
import org.netbeans.api.project.FileOwnerQuery;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectUtils;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataShadow;

/**
 *
 * @author Bender
 */
public class ProjectDirStrategy implements Strategy {

    @Override
    public String createText(ContextObject c) {
        return createProjectDir(c);
    }

    @Override
    public String getSupportedKey() {
        return "${project_dir}";
    }

    public String createProjectDir(ContextObject c) {
        String projectDir = null;
        if (null != c.getProject()) {
            projectDir = getProjectDirectory(c.getProject());
        }

        if (null != c.getDataObject()) {
            final FileObject primaryFile = FileUtils.getFileObjectWithShadowSupport(c.getDataObject());
            projectDir = getProjectDirectory(primaryFile);
        }
        return projectDir;
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
}
