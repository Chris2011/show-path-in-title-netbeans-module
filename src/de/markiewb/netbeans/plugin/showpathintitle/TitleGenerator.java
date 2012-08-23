/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.markiewb.netbeans.plugin.showpathintitle;

import de.markiewb.netbeans.plugin.showpathintitle.strategies.Strategy;
import de.markiewb.netbeans.plugin.showpathintitle.strategies.impl.AbsoluteFilenameStrategy;
import de.markiewb.netbeans.plugin.showpathintitle.strategies.impl.FilenameStrategy;
import de.markiewb.netbeans.plugin.showpathintitle.strategies.impl.ProjectDirStrategy;
import de.markiewb.netbeans.plugin.showpathintitle.strategies.impl.ProjectNameStrategy;
import de.markiewb.netbeans.plugin.showpathintitle.strategies.impl.RelativeFilenameStrategy;
import de.markiewb.netbeans.plugin.showpathintitle.strategies.impl.VersionStrategy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.netbeans.api.project.Project;
import org.openide.loaders.DataObject;
import org.openide.nodes.Node;
import org.openide.windows.Mode;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

/**
 *
 * @author markiewb
 */
public class TitleGenerator {

    public static final String SEPARATOR = " - ";

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

    public String createTitle(Options options) {
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
        String definedFormat = options.getDefinedFormat();
//        definedFormat = "${filename_abs} ${filename_rel} ${project_dir} ${project} ${filename} ${version}]";
//        definedFormat = "${project} ${filename_rel} ${version}]";
        List<String> splitted = Arrays.asList(definedFormat.split(" "));
        Map<String, String> map = new HashMap<String, String>();

        for (Strategy strategy : createStrategies()) {
            map.put(strategy.getSupportedKey(), StringUtils.defaultIfEmpty(strategy.createText(c)));
        }

//        System.out.println(map.keySet());

        //dynamic replace of user-defined patterns 
        Set<String> list = new LinkedHashSet<String>();
//        splitted=map.keySet();
        for (String pattern : splitted) {
            String value = map.get(pattern);
            list.add(value);
        }
        return StringUtils.defaultIfEmpty(StringUtils.join(list, SEPARATOR), "NetBeans");
    }

    public static List<Strategy> createStrategies() {
        List<Strategy> strategies = new ArrayList<Strategy>();
        strategies.add(new ProjectNameStrategy());
        strategies.add(new ProjectDirStrategy());
        strategies.add(new RelativeFilenameStrategy());
        strategies.add(new AbsoluteFilenameStrategy());
        strategies.add(new FilenameStrategy());
        strategies.add(new VersionStrategy());
        return strategies;
    }
}
