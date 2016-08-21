/**
 * Copyright 2016 markiewb
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

import java.util.LinkedHashSet;
import java.util.Set;

/**
 *
 * @author markiewb
 */
class TitleBuilder {

    String activeProjectGroup;
    String fileName;
    String ideVersion;
    String nodeDisplayName;
    String projectDir;
    String projectName;

    public String build(ShowPathInTitleOptions options) {
        // create title
        Set<String> list = new LinkedHashSet<String>();
        if (options.showProjectGroup) {
            list.add(activeProjectGroup);
        }
        if (options.showProjectName) {
            list.add(projectName);
        }

        if (options.showFileName) {
            // show relative path, when project dir is in selected path
            // show no relative path, when project dir equals selected path
            boolean isRelativePath = null != fileName && null != projectDir && fileName.startsWith(projectDir)
                    && !fileName.equals(projectDir);
            if (options.showRelativeFilename && isRelativePath) {
                // create and use relative file name
                String reducedFileName = fileName.substring(projectDir.length());
                fileName = reducedFileName;
            }

            if (null == fileName && null != projectDir) {
                // show projectDir as fallback
                fileName = projectDir;
            }
            if (null == fileName && null != nodeDisplayName) {
                // show node label as further fallback
                fileName = nodeDisplayName;
            }
            list.add(fileName);
        }
        if (options.showIDEVersion) {
            // version only available for netbeans >=7.1
            list.add(System.getProperty("netbeans.productversion"));
        }
        return StringUtils.join_nullignore(list, " - ");
    }

    public void setActiveProjectGroup(String activeProjectGroup) {
        this.activeProjectGroup = activeProjectGroup;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setIdeVersion(String ideVersion) {
        this.ideVersion = ideVersion;
    }

    public void setNodeDisplayName(String nodeDisplayName) {
        this.nodeDisplayName = nodeDisplayName;
    }

    public void setProjectDir(String projectDir) {
        this.projectDir = projectDir;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

}
