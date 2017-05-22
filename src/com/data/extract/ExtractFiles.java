package com.data.extract;

import com.data.misc.ExecuteCommands;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by mohammed.rampurawala on 5/20/2016.
 */
public class ExtractFiles {

    private Commands commands;
    private HashMap<String, List<String>> mFileMap = new HashMap<String, List<String>>();
    private DirectoryOps mDirectoryOps = DirectoryOps.getInstance();


    public ExtractFiles() {
        commands = new Commands();
    }


    public void startExtraction(String packageName) {


        try {
            commands.setPackageName(packageName);
            Runtime.getRuntime().exec(commands.getStartAdbServerCommand());
            this.mDirectoryOps.createExtractionDestDir(packageName);


            deleteProjectDirFromSD();

            createMainDirList(packageName);
            createSubDirectory(packageName);
            createProjectDirInSd();
            createFolderPaths();
            extractFiles(packageName);
            pullFiles();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void deleteProjectDirFromSD() throws IOException {
        ExecuteCommands.getInstance().executeCommands(commands.getDeleteDirectoryCommand());
    }

    private void pullFiles()
            throws IOException, InterruptedException {
        String mOutputPath = this.mDirectoryOps.getOutputPath();
        Set<String> fileMapKeySet = this.mFileMap.keySet();

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

                //Delay because sometimes it takes time to create directory.
                Thread.sleep(1000L);

                ExecuteCommands.getInstance().executeCommands(commands.getPullFileCommand(fileMapKey, fileName, mOutputPath));

            }

        }
    }

    private void createFolderPaths()
            throws IOException, InterruptedException {
        for (String folderName : this.mFileMap.keySet()) {
            ExecuteCommands.getInstance().executeCommands(commands.createDirInSdcard(folderName));
        }
    }

    private void extractFiles(String packageName)
            throws IOException, InterruptedException {
        Set<String> fileMapKeySet = this.mFileMap.keySet();
        for (String fileMapKey : fileMapKeySet) {
            List<String> fileList = (List<String>) this.mFileMap.get(fileMapKey);
            for (String fileName : fileList) {
                if (!fileName.contains("fabric")) {
                    String[][] commandArray = commands.getExtractionCommands(packageName, fileMapKey, fileName);
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
        ExecuteCommands.getInstance().executeCommands(commands.getCreatePackDirInSdcard());
    }

    private void createSubDirectory(String packageName)
            throws IOException {
        Set<String> keySet = this.mFileMap.keySet();
        String line;
        for (String key : keySet) {
            InputStream inputStream = ExecuteCommands.getInstance()
                    .executeCommands(commands.getListOfFilesForPackage(packageName, key));
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
        InputStream executeCommands = ExecuteCommands.getInstance()
                .executeCommands(commands.getMainDirPackage(packageName));
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
