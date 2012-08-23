/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.markiewb.netbeans.plugin.showpathintitle.strategies.impl;

import de.markiewb.netbeans.plugin.showpathintitle.ContextObject;
import de.markiewb.netbeans.plugin.showpathintitle.FileUtils;
import de.markiewb.netbeans.plugin.showpathintitle.TitleGenerator;
import de.markiewb.netbeans.plugin.showpathintitle.strategies.Strategy;
import org.netbeans.api.project.FileOwnerQuery;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectUtils;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataShadow;
import org.openide.nodes.Node;

/**
 *
 * @author Bender
 */
public class ProjectNameStrategy implements Strategy {

    
     
    public String createText(ContextObject c) {
        String result = null;
        if (null != c.getProject()) {
            result = getProjectName(c.getProject());
        }
        if (null != c.getDataObject()) {
            final FileObject primaryFile = FileUtils.getFileObjectWithShadowSupport(c.getDataObject());
            result = this.getProjectName(primaryFile);
        }
        for (Node node = c.getNode(); null != node; node = node.getParentNode()) {
            Project project = node.getLookup().lookup(Project.class);
            if (null != project) {
                return this.getProjectName(project);
            }
        }
        return result;
    }

    private String getProjectName(final FileObject primaryFile) {
        try {
            return ProjectUtils.getInformation(FileOwnerQuery.getOwner(primaryFile)).getDisplayName();
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

    @Override
    public String getSupportedKey() {
        return "${project}";
    }
}
