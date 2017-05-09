package com.data.extract;

import com.data.misc.ExecuteCommands;
import com.sun.javafx.util.Utils;

import java.io.*;
import java.util.*;

/**
 * Created by mohammed.rampurawala on 5/20/2016.
 */
public class ExtractFiles {

    private HashMap<String, List<String>> mFileMap = new HashMap<String, List<String>>();
    private String mBasePath;
    private DirectoryOps mDirectoryOps = DirectoryOps.getInstance();
    private ArrayList<String> macCommands = new ArrayList<>();

    {
        macCommands.add("/bin/bash");
        macCommands.add("-l");
        macCommands.add("-c");
    }

    public void startExtraction(String packageName) {


        try {
            Runtime.getRuntime().exec("adb start-server");
            this.mDirectoryOps.createExtractionDestDir(packageName);
            this.mBasePath = ("/sdcard/extract_files/" + packageName);

            deleteProjectDirFromSD();

            createMainDirList(packageName);
            createSubDirectory(packageName);
            createProjectDirInSd();
            createFolderPaths();
            extractFiles(packageName);
            pullFiles(packageName);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void deleteProjectDirFromSD() throws IOException {
        ArrayList<String> listOfCommands = new ArrayList<String>();
        listOfCommands.add("adb");
        listOfCommands.add("shell");
        listOfCommands.add("rm -r" + this.mBasePath);
        ExecuteCommands.getInstance().executeCommands(listOfCommands);
    }

    private void pullFiles(String packageName)
            throws IOException, InterruptedException {
        String mOutputPath = this.mDirectoryOps.getOutputPath();
        Set<String> fileMapKeySet = this.mFileMap.keySet();
        boolean isMac = Utils.isMac();
        ArrayList<String> listOfCommands = new ArrayList<>();

        for (String fileMapKey : fileMapKeySet) {
            File stringFilePath = new File(mOutputPath + File.separator + fileMapKey);
            if (!stringFilePath.exists()) {
                stringFilePath.mkdir();
            }
            Thread.sleep(100L);

            //get the list of the file object
            List<String> list = mFileMap.get(fileMapKey);
            int fileMapListSize = list.size();
            for (int fileMapListIndex = 0; fileMapListIndex < fileMapListSize; fileMapListIndex++) {
                String fileName = list.get(fileMapListIndex);
                String inputFile = this.mBasePath + "/" + fileMapKey + fileName;
                String outputFile = "\"" + mOutputPath + File.separator + fileMapKey +
                        "\"";
                System.out.println("Input:->" + inputFile);
                System.out.println("Output:->" + outputFile);
                System.out
                        .println("-----------------------------------------------------");
                //Delay because sometimes it takes time to create directory.
                Thread.sleep(1000L);
                if (isMac) {
                    listOfCommands.addAll(macCommands);
                }
                listOfCommands.add("adb pull " + inputFile + " " + outputFile);
                ProcessBuilder builder = new ProcessBuilder(listOfCommands);
                builder.start();
                listOfCommands.clear();
            }

        }
    }

    private void createFolderPaths()
            throws IOException, InterruptedException {
        for (String folderName : this.mFileMap.keySet()) {
            System.out.println(this.mBasePath + "/" + folderName);
            ArrayList<String> listOfCommands = new ArrayList<String>();
            listOfCommands.add("adb");
            listOfCommands.add("shell");
            listOfCommands.add("mkdir " + this.mBasePath + "/" + folderName);
            ExecuteCommands.getInstance().executeCommands(listOfCommands);
        }
    }

    private void extractFiles(String packageName)
            throws IOException, InterruptedException {
        Set<String> fileMapKeySet = this.mFileMap.keySet();
        for (String fileMapKey : fileMapKeySet) {
            List<String> fileList = (List<String>) this.mFileMap.get(fileMapKey);
            for (String fileName : fileList) {
                if (!fileName.contains("fabric")) {
                    String[][] commandArray = {
                            {"adb", "shell",
                                    "run-as " + packageName,
                                    "chmod 666 " + fileMapKey + fileName},
                            {
                                    "adb",
                                    "shell",
                                    "cp /data/data/" + packageName + "/" + fileMapKey +
                                            fileName + " " + mBasePath +
                                            "/" + fileMapKey},
                            {"adb", "shell",
                                    "run-as " + packageName,
                                    "chmod 600 " + fileMapKey + fileName}};
                    int j = (commandArray).length;
                    for (int i = 0; i < j; i++) {
                        String[] extractionCommands = commandArray[i];
                        ExecuteCommands.getInstance().executeCommands(
                                extractionCommands);
                    }
                }
            }
        }
    }

    private void createProjectDirInSd()
            throws IOException {
        ArrayList<String> listOfCommands = new ArrayList<String>();
        listOfCommands.add("adb");
        listOfCommands.add("shell");
        listOfCommands.add("mkdir " + this.mBasePath);
        ExecuteCommands.getInstance().executeCommands(listOfCommands);
    }

    private void createSubDirectory(String packageName)
            throws IOException {
        Set<String> keySet = this.mFileMap.keySet();
        String line;
        for (String key : keySet) {

            ArrayList<String> listOfCommands = new ArrayList<String>();
            listOfCommands.add("adb");
            listOfCommands.add("shell");
            listOfCommands.add("run-as " + packageName);
            listOfCommands.add("ls " + key);
            InputStream inputStream = ExecuteCommands.getInstance()
                    .executeCommands(listOfCommands);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            List<String> list = (List<String>) this.mFileMap.get(key);

            while ((line = reader.readLine()) != null) {
                if (line.isEmpty()) {
                    continue;
                }
                list.add("/" + line);
            }
            try {
                reader.close();
                inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void createMainDirList(String packageName)
            throws IOException {
        ArrayList<String> listOfCommands = new ArrayList<String>();
        listOfCommands.add("adb");
        listOfCommands.add("shell");
        listOfCommands.add("run-as " + packageName);
        listOfCommands.add("ls");

        InputStream executeCommands = ExecuteCommands.getInstance()
                .executeCommands(listOfCommands);
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                executeCommands));
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.length() > 1) {
                this.mFileMap.put(line, new ArrayList<String>());
            }
        }
        try {
            reader.close();
            executeCommands.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
