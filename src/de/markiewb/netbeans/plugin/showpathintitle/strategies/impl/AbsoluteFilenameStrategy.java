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
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataShadow;

/**
 *
 * @author Bender
 */
public class AbsoluteFilenameStrategy implements Strategy {

    @Override
    public String createText(ContextObject c) {
        return createAbsoluteFilename(c);

    }

    @Override
    public String getSupportedKey() {
        return "${filename_abs}";
    }

    private String createAbsoluteFilename(ContextObject c) {
        String absoluteFileName = null;
        if (null != c.getDataObject()) {
            final FileObject primaryFile = FileUtils.getFileObjectWithShadowSupport(c.getDataObject());
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
            String projectDir=new ProjectDirStrategy().createText(c);
            absoluteFileName = FileUtils.getFallbackForFileName(absoluteFileName, projectDir, c.getNode());
        }
        return absoluteFileName;
    }
}
