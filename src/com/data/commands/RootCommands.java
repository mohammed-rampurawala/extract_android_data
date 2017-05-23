package com.data.commands;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mohammed.rampurawala on 5/23/2017.
 */
public class RootCommands extends BaseCommands {

    @Override
    public String[][] getCommandToFetchDataToSdCard(String packageName, String fileMapKey, String fileName) {
        return new String[][]{
                {"adb", "shell",
                        "chmod 666 data/data/" + packageName + fileMapKey + fileName},
                {
                        "adb",
                        "shell",
                        "cp /data/data/" + packageName + "/" + fileMapKey +
                                fileName + " " + getBasePath() +
                                "/" + fileMapKey},
                {"adb", "shell",
                        "chmod 600 data/data/" + packageName + fileMapKey + fileName}};
    }


    @Override
    public List<String> getListOfFilesForPackage(String packageName, String key) {
        ArrayList<String> listOfCommands = new ArrayList<String>(adbShell);
        listOfCommands.add("ls data/data/" + packageName + "/" + key);
        return listOfCommands;
    }

    @Override
    public List<String> getMainDirPackage(String packageName) {
        ArrayList<String> listOfCommands = new ArrayList<String>(adbShell);
        listOfCommands.add("ls data/data/" + packageName);
        return listOfCommands;
    }

    @Override
    public List<String> getListOfPackagesInDevice() {
        ArrayList<String> listOfCommands = new ArrayList<String>(adbShell);
        listOfCommands.add("pm list packages");
        return listOfCommands;
    }
}
