package com.flowjo.plugins;

import java.io.File;
import java.text.SimpleDateFormat;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import org.json.XML;
import org.json.JSONObject;
import javax.swing.JOptionPane;
import com.treestar.lib.PluginHelper;
import com.treestar.lib.core.WorkspacePluginInterface;
import com.treestar.lib.xml.SElement;
import com.treestar.lib.prefs.HomeEnv;
//import com.treestar.lib.fjml.types.FileTypes;
//import com.treestar.lib.xml.XMLUtil;


//This plugin is based on code stubs found in the AWSPlugin
//(located at https://github.com/FlowJo-LLC/FlowJo-AWS-S3-Plugin)
//and the example workspace found in the Plugin developer documents
//git book (https://flowjollc.gitbooks.io/flowjo-plugin-developers-guide/content/workspace_plugin_example.html)
//and the email notification plugin (located at https://github.com/FlowJo-LLC/EmailNotification-Plugin)
public class WSPtoJSON implements WorkspacePluginInterface {
    private String opened;

    /**
     * this function adds the ServerURL to the workspace XML
     *
     * @return String ServerUrl
     */
    @Override
    public String getServerUrl() {
        return "http://localhost:8080/WSPtoJSON ";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }


    /**
     * this function is used to add items to an SElement within the Workspace
     * XML. In this plugin this is where the plugin preferences are saved.
     */
    @Override
    public SElement getElement() {

        SElement pluginPrefs = new SElement("Prefs");
        if (opened != null && !opened.isEmpty())
            pluginPrefs.setString("opened", opened);

        System.out.println(pluginPrefs.toString());

        return pluginPrefs;
    }

    /**
     * Method called when workspace is opened.
     *
     * @param	workspaceElement	Workspace SElement object
     * @return						Boolean value indicating if plugin should be add
     * 								to the workspace for subsequent operations
     */
    @Override
    public boolean openWorkspace(SElement workspaceElement) {
        opened = getDateStamp();
        return true;
    }

    /**
     * Method called when FlowJo workspace is saved.
     * @param	workspaceElement	SElement workspace object
     */
    @Override
    public void save(final SElement workspaceElement) {
        String name = PluginHelper.getWorkspaceName(workspaceElement);
        name = name.substring(0, name.lastIndexOf('.'));
        File folder = getOutputFolder(workspaceElement);
        File jsonFile = new File(folder, name + ".json");

        JSONObject workspaceObj = XML.toJSONObject(workspaceElement.getChild("experiment").toString());
        JSONObject sampleStats = new JSONObject();
        workspaceObj.put("Workspace_name", name);
        // Save the timestamp when the json file was generated/saved
        workspaceObj.put("Timestamp", getDateStamp());

        Map<String, Map<String, Map <String, Double>>> statsMap = PluginHelper.collectStats(workspaceElement);

        for(String sampleName : statsMap.keySet()) {
            Map<String, Map <String, Double>> sampleMap = statsMap.get(sampleName);
            JSONObject sampleObj = new JSONObject();

            for(String populationName : sampleMap.keySet()) {

                Map<String, Double> populationMap= sampleMap.get(populationName);
                JSONObject populationObj = new JSONObject();

                for (String statName : populationMap.keySet()){
                    /** for non-gated parent population counts of a sample
                     * the population name/key in the map is "" i.e. an empty
                     * string, so if we encounter that put all stats under that
                     * in the sample JSON object else land it in the population
                     * JSON object
                     **/
                    if(populationName != null && !populationName.isEmpty()) {
                        populationObj.put(statName, populationMap.get(statName));
                    }
                    else {
                        sampleObj.put(statName, populationMap.get(statName));
                    }
                }

                if(populationName != null && !populationName.isEmpty()) {
                    sampleObj.put(populationName, populationObj);
                }
            }

            sampleStats.put(sampleName, sampleObj);
        }

        workspaceObj.put("Sample_stats", sampleStats);

        try (FileWriter file = new FileWriter(jsonFile)) {
            //File Writer creates a file in write mode at the given location
            file.write(workspaceObj.toString(4));

            //write function is use to write in file,
            //here we write the Json object in the file
            file.flush();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        /** This part is to save the workspaceElement's xml contents to an xml file
         * I needed this to debug and explore the XML element to see what information
         * I can gather from this to export to the JSON file. If you do end up using this
         * then uncomment the FileTypes and XMLUtil imports at the very top of the file
         * **/
//        File saveFile = new File(folder, name + FileTypes.XML_SUFFIX);
//        XMLUtil tempSave = new XMLUtil();
//        tempSave.write(workspaceElement, saveFile);


        // show that the workspace annotations were successfully saved to the json file
        // also display the path to said json file
        String message = "Workspace stats and annotations successfully saved to JSON file --> " + jsonFile.getAbsolutePath();
        JOptionPane.showMessageDialog(null, message, "JSON saved", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Method called when FlowJo session ends
     */
    @Override
    public void endSession() { }

    private String getDateStamp() {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE, MMM d, yyyy HH:mm:ss z");
        return dateFormatter.format(System.currentTimeMillis());
    }

    private File getOutputFolder(SElement workspaceElement) {
        File file = new File(PluginHelper.getWorkspaceAnalysisFolder(workspaceElement).getAbsolutePath());
        if (file != null && file.exists())
            return file;
        String home = HomeEnv.getInstance().getUserHomeFolder();
        File homeFolder = new File(home);
        if (homeFolder.exists())
            return homeFolder;
        return null;
    }

}
