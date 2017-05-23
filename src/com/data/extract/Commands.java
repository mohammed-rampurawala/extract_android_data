package com.data.extract;

import com.sun.javafx.util.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mohammedrampurawala on 5/20/17.
 */
public class Commands {
    private ArrayList<String> macCommands = new ArrayList<>();
    private ArrayList<String> adbShell = new ArrayList<>();

    {
        //Add Mac Commands
        macCommands.add("/bin/bash");
        macCommands.add("-l");
        macCommands.add("-c");

        //Add adb shell
        adbShell.add("adb");
        adbShell.add("shell");

    }


    private String startAdbServer = "adb start-server";

    private String basePath = "/sdcard/";


    public void setPackageName(String packageName) {
        basePath += packageName;
    }

    public String getBasePath() {
        return basePath;
    }

    public String getStartAdbServerCommand() {
        return startAdbServer;
    }


    public ArrayList<String> getDeleteDirectoryCommand() {
        ArrayList<String> listOfCommands = new ArrayList<>(adbShell);
        listOfCommands.add("rm -r " + basePath);
        return listOfCommands;
    }

    public ArrayList<String> getPullFileCommand(String fileMapKey, String fileName, String outputPath) {
        boolean isMac = Utils.isMac();
        ArrayList<String> listOfCommands = new ArrayList<>();
        if (isMac) listOfCommands.addAll(macCommands);
        listOfCommands.add("adb");
        listOfCommands.add("pull");
        String inputFile = getBasePath() + "/" + fileMapKey + fileName;
        //Input file name path
        listOfCommands.add(inputFile);
        String outputFile = "\"" + outputPath + File.separator + fileMapKey + "\"";
        //Output file name path
        listOfCommands.add(outputFile);
        System.out.println("Input:->" + inputFile);
        System.out.println("Output:->" + outputFile);
        System.out
                .println("-----------------------------------------------------");
        return listOfCommands;
    }

    public ArrayList<String> createDirInSdcard(String folderName) {
        System.out.println(getBasePath() + "/" + folderName);

        ArrayList<String> listOfCommands = new ArrayList<String>(adbShell);
        listOfCommands.add("mkdir " + getBasePath() + "/" + folderName);
        return listOfCommands;
    }

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


    String[][] getExtractionCommands(String packageName, String fileMapKey, String fileName, boolean isRooted) {
        return isRooted ? getRootCommands(packageName, fileMapKey, fileName) : getNonRootCommands(packageName, fileMapKey, fileName);
    }

    private String[][] getNonRootCommands(String packageName, String fileMapKey, String fileName) {
        return new String[][]{
                getTheChmod666Command(packageName, fileMapKey, fileName),
                {
                        "adb",
                        "shell",
                        "cp /data/data/" + packageName + "/" + fileMapKey +
                                fileName + " " + getBasePath() +
                                "/" + fileMapKey},
                getTheChmod600Command(packageName, fileMapKey, fileName)};
    }

    private String[][] getRootCommands(String packageName, String fileMapKey, String fileName) {
        return new String[][]{
                {
                        "adb",
                        "shell",
                        "cp /data/data/" + packageName + "/" + fileMapKey +
                                fileName + " " + getBasePath() +
                                "/" + fileMapKey},
        };
    }

    public List<String> getCreatePackDirInSdcard() {
        ArrayList<String> listOfCommands = new ArrayList<String>(adbShell);
        listOfCommands.add("mkdir " + getBasePath());
        return listOfCommands;
    }

    public List<String> getListOfFilesForPackage(String packageName, String key, boolean isRooted) {
        ArrayList<String> listOfCommands = new ArrayList<String>(adbShell);
        listOfCommands.add("run-as " + packageName);
        listOfCommands.add("ls " + key);
        return listOfCommands;
    }

    public List<String> getMainDirPackage(String packageName, boolean isRooted) {
        ArrayList<String> listOfCommands = new ArrayList<String>(adbShell);
        listOfCommands.add("run-as " + packageName);
        listOfCommands.add("ls");
        return listOfCommands;
    }

    public List<String> getCheckingRootCommand() {
        List<String> listOfCOmmands = new ArrayList<>(adbShell);
        listOfCOmmands.add("cd root");
        return listOfCOmmands;
    }


}
