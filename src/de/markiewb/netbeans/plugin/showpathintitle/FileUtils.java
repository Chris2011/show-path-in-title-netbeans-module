/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.markiewb.netbeans.plugin.showpathintitle;

import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataShadow;
import org.openide.nodes.Node;

/**
 *
 * @author Bender
 */
public class FileUtils {

    public static String getFallbackForFileName(String fileName, String projectDir, Node node) {
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

    public static FileObject getFileObjectWithShadowSupport(DataObject dataObject) {
        if (dataObject instanceof DataShadow) {
            DataShadow dataShadow = (DataShadow) dataObject;
            return dataShadow.getOriginal().getPrimaryFile();
        }
        return dataObject.getPrimaryFile();
    }
}
