/**
 * Copyright 2013 markiewb
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package de.markiewb.netbeans.plugin.showpathintitle;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import org.openide.util.Exceptions;
import org.openide.util.NbPreferences;

/**
 *
 * @author markiewb
 */
public class ProjectGroupUtil {

    /**
     * Get the preference for the given node path.
     *
     * @param path configuration path like "org/netbeans/modules/projectui"
     * @return {@link Preferences} or null
     */
    private Preferences getPreferences(String path) {
	try {
	    if (NbPreferences.root().nodeExists(path)) {
		return NbPreferences.root().node(path);
	    }
	} catch (BackingStoreException ex) {
	    Exceptions.printStackTrace(ex);
	}
	return null;
    }

    /**
     *
     * @return name of the current project group or null
     */
    public String getActiveProjectGroup() {
	Preferences groupNode = getPreferences("org/netbeans/modules/projectui/groups");
	if (null != groupNode) {
	    final String groupId = groupNode.get("active", null);
	    if (null != groupId) {
		final Preferences groupPref = getPreferences("org/netbeans/modules/projectui/groups" + "/" + groupId);
		if (null != groupPref) {
		    final String activeProjectGroup = groupPref.get(PathUtil.KEY_NAME, null);
		    return activeProjectGroup;
		}
	    }
	}
	return null;
    }
}
