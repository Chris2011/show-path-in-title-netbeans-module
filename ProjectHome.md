It shows the current project group, project name, path of the current file/node/editor and IDE version in the titlebar of `NetBeans`.

Example: <img src='https://show-path-in-title-netbeans-module.googlecode.com/hg/src/test/resources/example.png' />

This implements the feature request from http://netbeans.org/bugzilla/show_bug.cgi?id=159722. The newest version can be downloaded from here. The most "stable" version can also be found at the <a href='http://plugins.netbeans.org/plugin/42000/'><code>Netbeans</code> Plugin Portal</a>.

<p>
Compatibility:<br>
<ul><li>
This plugin is compatible to Netbeans IDE >=6.9 (known to work with 6.9.1, 7.1, 7.1.1, 7.1.2, 7.2, 7.3)<br>
</li>
</ul>
</p>


&lt;hr/&gt;


<b>Update:</b>


Updates in 2.0.1
<ul>
<li><code>[feature]</code> <a href='http://code.google.com/p/show-path-in-title-netbeans-module/issues/detail?id=14'><a href='https://code.google.com/p/show-path-in-title-netbeans-module/issues/detail?id=14'>Issue 14</a>:</a> Show project group, if available</li>
</ul>

Updates in 1.3.1
<ul>
<li><code>[fix]</code> <a href='https://code.google.com/p/show-path-in-title-netbeans-module/issues/detail?id=11'>Issue 11</a>: Preferences are loaded on every change</li>
<li><code>[fix]</code> <a href='https://code.google.com/p/show-path-in-title-netbeans-module/issues/detail?id=12'>Issue 12</a>: Use original window title instead of hardcoded string</li>
</ul>

Updates in 1.3.0
<ul>
<li><code>[feature]</code> <a href='https://code.google.com/p/show-path-in-title-netbeans-module/issues/detail?id=7'>Issue 7</a>: Show path, when in external sources</li>
<li><code>[fix]</code> show projectname + projectdir, when selecting a project node</li>
</ul>

Updates in 1.2.1
<ul>
<li><code>[feature]</code> shows full path when project is selected - useful in conjunction with option "use selected node as source for path"</li>
</ul>

Updates in 1.2
<ul>
<li><code>[feature]</code> shows path relative to project directory (eclipse-like, it is activated by default, old mode can be activated manually)</li>
<li><code>[feature]</code> additonal mode: use open editor as source for path (eclipse-like, has to be enabled manually)</li>
<li><code>[fix]</code> prevents empty title - default is "<code>NetBeans</code>"</li>
</ul>

Updates in 1.1
<p>
Additionally it shows <ul> <li>the project name </li> <li>the <code>NetBeans</code> version (only supported for NB >=7.1)</li> </ul>. It can be configured using an option panel.<br>
</p>