/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.markiewb.netbeans.plugin.showpathintitle;

import org.netbeans.api.project.Project;
import org.openide.loaders.DataObject;
import org.openide.nodes.Node;

/**
 *
 * @author Bender
 */
public class ContextObject {

    private final Project project;
    private final DataObject dataObject;
    private final Node node;

    ContextObject(Project project, DataObject dataObject, Node node) {
        this.project = project;
        this.dataObject = dataObject;
        this.node = node;
    }

    public Project getProject() {
        return project;
    }

    public DataObject getDataObject() {
        return dataObject;
    }

    public Node getNode() {
        return node;
    }
}
