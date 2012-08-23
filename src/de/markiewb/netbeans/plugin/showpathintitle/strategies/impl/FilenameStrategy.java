/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.markiewb.netbeans.plugin.showpathintitle.strategies.impl;

import de.markiewb.netbeans.plugin.showpathintitle.ContextObject;
import de.markiewb.netbeans.plugin.showpathintitle.FileUtils;
import de.markiewb.netbeans.plugin.showpathintitle.StringUtils;
import de.markiewb.netbeans.plugin.showpathintitle.strategies.Strategy;
import org.openide.filesystems.FileObject;

/**
 *
 * @author Bender
 */
public class FilenameStrategy implements Strategy {

    @Override
    public String createText(ContextObject c) {
        return createFilename(c);
    }

    @Override
    public String getSupportedKey() {
        return "${filename}";
    }

    private String createFilename(ContextObject c) {
        String result = null;
        if (null != c.getDataObject()) {
            final FileObject primaryFile = FileUtils.getFileObjectWithShadowSupport(c.getDataObject());
            if (null != primaryFile.getNameExt()) {

                result = primaryFile.getNameExt();
            }
        }
        String projectDir = new ProjectDirStrategy().createProjectDir(c);
        if (StringUtils.isEmpty(result)) {
            result = FileUtils.getFallbackForFileName(result, projectDir, c.getNode());
        }
        return result;
    }
}
