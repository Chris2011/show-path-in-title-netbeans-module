/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.markiewb.netbeans.plugin.showpathintitle.strategies;

import de.markiewb.netbeans.plugin.showpathintitle.ContextObject;

/**
 *
 * @author Bender
 */
public interface Strategy {

    String createText(ContextObject c);
    String getSupportedKey() ;
}
