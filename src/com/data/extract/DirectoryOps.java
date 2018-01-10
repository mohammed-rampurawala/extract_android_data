package com.data.extract;

import java.io.File;
import java.nio.file.Paths;

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
        String filePath = getFilePath();

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

    private String getFilePath() {
        if (mOutputPath == null || mOutputPath.trim().isEmpty()) {
            return System.getProperty("user.dir");
        } else {
            try {
                Paths.get(mOutputPath);
                return mOutputPath;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return System.getProperty("user.dir");
    }

    public String getOutputPath() {
        return this.mOutputPath;
    }

    public void setExtractionPath(String path) {
        if (path != null && !path.trim().isEmpty()) {
            mOutputPath = path;
        }
    }
}
