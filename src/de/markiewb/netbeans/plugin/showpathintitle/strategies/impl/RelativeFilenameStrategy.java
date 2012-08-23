/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.markiewb.netbeans.plugin.showpathintitle.strategies.impl;

import de.markiewb.netbeans.plugin.showpathintitle.ContextObject;
import de.markiewb.netbeans.plugin.showpathintitle.FileUtils;
import de.markiewb.netbeans.plugin.showpathintitle.StringUtils;
import de.markiewb.netbeans.plugin.showpathintitle.strategies.Strategy;

/**
 *
 * @author Bender
 */
public class RelativeFilenameStrategy implements Strategy {

    @Override
    public String createText(ContextObject c) {
        return createRelativeFilename(c);

    }

    private String createRelativeFilename(ContextObject c) {

        String absoluteFileName = new AbsoluteFilenameStrategy().createText(c);
        String relativeFileName = null;

        String projectDir = new ProjectDirStrategy().createText(c);
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
            relativeFileName = FileUtils.getFallbackForFileName(relativeFileName, projectDir, c.getNode());
        }
        return relativeFileName;
    }

    @Override
    public String getSupportedKey() {
        return "${filename_rel}";
    }
}
