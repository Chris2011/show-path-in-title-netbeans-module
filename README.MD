show-path-in-title-netbeans-module
==================================

It shows the current project group, project name, path of the current file/node/editor and IDE version in the titlebar of NetBeans.
It can be configured using an option panel.

See <a href="http://plugins.netbeans.org/plugin/42000/show-path-in-title">http://plugins.netbeans.org/plugin/42000/show-path-in-title</a>
This implements the feature request from <a href="http://netbeans.org/bugzilla/show_bug.cgi?id=159722">http://netbeans.org/bugzilla/show_bug.cgi?id=159722</a>

<img src="https://raw.githubusercontent.com/markiewb/show-path-in-title-netbeans-module/master/src/test/resources/example.png">

<p>
Updates in 2.2.0
<ul>
<li>[<a href="https://github.com/markiewb/show-path-in-title-netbeans-module/issues/25">Issue 25</a>]: Fixed: Wrong file displayed if using docked windows (initial code by Shredder121)</li>
<li>[<a href="https://github.com/markiewb/show-path-in-title-netbeans-module/issues/3">Issue 3</a>]: Fixed: When multiple files are selected only the last one is shown</li>
</ul>

Updates in 2.1.0
<ul>
<li>[<a href="https://github.com/markiewb/show-path-in-title-netbeans-module/pull/21">Issue  21</a>]: Speed up the query for the current project (PR by Shredder121)</li>
<li>[<a href="https://github.com/markiewb/show-path-in-title-netbeans-module/issues/22">Issue  22</a>]: Update to 8.0.2 platform</li>
<li>[<a href="https://github.com/markiewb/show-path-in-title-netbeans-module/issues/17">Issue  17</a>]: Move options to Options|Appearance</li>

</ul>

Updates in 2.0.1
<ul>
<li>[feature] <a href="http://code.google.com/p/show-path-in-title-netbeans-module/issues/detail?id=14">Issue 14:</a> Show project group, if available</li>
<li>[internal] use another non-API approach get the name of the project group. Fortunately this allows the plugin to be compatible to older NetBeans versions.</li>
</ul>

Updates in 1.3.1
<ul>
<li>[fix] <a href="http://code.google.com/p/show-path-in-title-netbeans-module/issues/detail?id=11">Issue 11:</a> Preferences are loaded on every change</li>
<li>[fix] <a href="http://code.google.com/p/show-path-in-title-netbeans-module/issues/detail?id=12">Issue 12:</a> Use original window title instead of hardcoded string</li>
</ul>

Updates in 1.3.0
<ul>
<li>[feature] <a href="http://code.google.com/p/show-path-in-title-netbeans-module/issues/detail?id=7">Issue 7:</a> Show path, when in external sources</li>
<li>[fix] show projectname + projectdir, when selecting a project node</li>
</ul>

Updates in 1.2.1
<ul>
<li>[feature] shows full path when project is selected - useful in conjunction with option "use selected node as source for path"</li>
</ul>

Updates in 1.2
<ul>
<li>[feature] shows path relative to project directory (eclipse-like, it is activated by default, old mode can be activated manually)</li>
<li>[feature] additonal mode: use open editor as source for path (eclipse-like, has to be enabled manually)</li>
<li>[fix] prevents empty title - default is "NetBeans"</li>
</ul>
</p>
<p>Provide defects, request for enhancements and feedback at <a href="https://github.com/markiewb/show-path-in-title-netbeans-module/issues">https://github.com/markiewb/show-path-in-title-netbeans-module/issues</a></p>
<p>Compatible to >=NB 8.0</p>
<p>Legal disclaimer: Code is licensed under Apache 2.0.</p>
<p>
<a href="https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=K4CMP92RZELE2"><img src="https://www.paypalobjects.com/en_US/i/btn/btn_donate_SM.gif" border="0"></a>
</p>