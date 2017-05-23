package com.data.extract;

import java.io.File;

/**
 * Created by mohammed.rampurawala on 5/20/2016.
 */
public class DirectoryOps {

    private static final DirectoryOps sDirectory = new DirectoryOps();
    private String mOutputPath;

    public static DirectoryOps getInstance() {
        return sDirectory;
    }

    public void createExtractionDestDir(String packageName)
            throws InterruptedException {
        String filePath = System.getProperty("user.dir");

        int count = 0;
        File file = new File(filePath + File.separator + packageName);
        while (file.exists()) {
            count++;
            file = new File(filePath + File.separator + packageName + "_" + count);
        }
        file.mkdir();
        Thread.sleep(100L);
        this.mOutputPath = file.getAbsolutePath();
    }

    public String getOutputPath() {
        return this.mOutputPath;
    }

}
