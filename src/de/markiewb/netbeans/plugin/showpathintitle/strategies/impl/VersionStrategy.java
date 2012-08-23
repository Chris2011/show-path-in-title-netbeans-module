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
public class VersionStrategy implements Strategy {

    @Override
    public String createText(ContextObject c) {
        return createVersion(c);
    }

    @Override
    public String getSupportedKey() {
        return "${version}";
    }

    private String createVersion(ContextObject c) {
        // version only available for netbeans >=7.1
        return System.getProperty("netbeans.productversion");
    }
}
