package com.data.commands;

import com.data.misc.ExecuteCommands;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by mohammedrampurawala on 5/20/17.
 */
public class NonRootCommands extends BaseCommands {

    private String[] getTheChmod666Command(String packageName, String fileMapKey, String fileName) {
        return new String[]{"adb", "shell",
                "run-as " + packageName,
                "chmod 666 " + fileMapKey + fileName};
    }

    private String[] getTheChmod600Command(String packageName, String fileMapKey, String fileName) {
        return new String[]{"adb", "shell",
                "run-as " + packageName,
                "chmod 600 " + fileMapKey + fileName};
    }


    @Override
    public String[][] getCommandToFetchDataToSdCard(String packageName, String fileMapKey, String fileName) {
        String destPath = getBasePath()+ File.separator+fileMapKey;
        //System.out.println(destPath);
        return new String[][]{
                getTheChmod666Command(packageName, fileMapKey, fileName),
                {
                        "adb",
                        "shell",
                        "run-as " + packageName,
                        "cp /data/data/" + packageName + "/" + fileMapKey +
                                fileName + " " + destPath},
                getTheChmod600Command(packageName, fileMapKey, fileName)};
    }

    @Override
    public List<String> getListOfFilesForPackage(String packageName, String key) {
        ArrayList<String> listOfCommands = new ArrayList<>(adbShell);
        listOfCommands.add("run-as " + packageName);
        listOfCommands.add("ls " + key);
        return listOfCommands;
    }

    @Override
    public List<String> getMainDirPackage(String packageName) {
        ArrayList<String> listOfCommands = new ArrayList<>(adbShell);
        listOfCommands.add("run-as " + packageName);
        listOfCommands.add("ls");
        return listOfCommands;
    }

    @Override
    public List<String> getListOfPackagesInDevice() {
        return Collections.emptyList();
    }
}
