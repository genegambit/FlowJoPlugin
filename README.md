# WSPtoJSON

This is a GitHub repository for a FlowJo plugin (Version 10.5.0) that exports the experiment annotations (along with metadata and some crucial stats) to JSON file.

### Prerequisites
```
FlowJo (Version 10.5.0):
https://www.flowjo.com/

Java SE Development Kit 8:
https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html

IntelliJ IDE for Java:
https://www.jetbrains.com/idea/
```

## Useful Resources

* docs.flowjo.com/d2/
* www.flowjo.com/tutorials/
* exchange.flowjo.com
* FlowJo Plugin Developers Guide: flowjollc.gitbooks.io/flowjo-plugin-developers-guide/content/plugin_development_and_deployment.html

* JSON-java library: https://github.com/stleary/JSON-java

* FlowJo AWS Plugin:
https://github.com/FlowJo-LLC/FlowJo-AWS-S3-Plugin

* FlowJo Email Notification Plugin: https://github.com/FlowJo-LLC/EmailNotification-Plugin

* Start FlowJo from terminal to expose error logs. Very useful for debugging purposes:
https://www.ramaciotti-hsb.org.au/cytometry-software-blog/2017/11/10/accessing-the-flowjo-error-log-on-mac-os-x-and-potentially-for-any-other-application

* FlowJo Plugin Testbed:
https://www.ramaciotti-hsb.org.au/cytometry-software-blog/2017/12/11/flowjo-plugin-testbed-run-and-debug-your-flowjo-plugins-without-using-flowjo

* SydneyCytometry:
https://github.com/sydneycytometry

## Plugin Installation

* Create your own plugin package in an IDE of your choice (Here, IntelliJ is used on MAC OSX).
* Add the fjlib.jar dependency for the package from IntelliJ found in: /Applications/FlowJo/Resources/Java/flib-2.3.0.jar
* Link the third party JSON-java library jar file to the package.
* Build the project ensuring that there are no errors.
* Build Artifact in IntelliJ, ensuring that only the third party libraries are present in the jar file.
* After build completion the jar file can be found in the project folder: /out/artifacts/module-name/jar-file.
* Copy the jar file to the /Applications/plugins folder.
* Start FlowJo and load a workspace of your choice that contains Annotations data.
* Loading the Plugin:
Workspace > Plugins > Add Workspace Plugin > Select WSPtoJSON
* Repeat the same step to ensure that the plugin is loaded.
* With the Plugin loaded, save the workspace. The plugin works specifically with the .wsp files.
* Upon saving a pop-up dialog will indicate the path where the JSON file is saved (which is in the same folder as the workspace).

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details.
