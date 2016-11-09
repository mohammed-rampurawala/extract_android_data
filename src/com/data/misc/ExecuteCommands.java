package com.data.misc;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

/**
 * Created by mohammed.rampurawala on 5/20/2016.
 */
public class ExecuteCommands {

    private static final ExecuteCommands sInstance = new ExecuteCommands();

    public static ExecuteCommands getInstance() {
        return sInstance;
    }

    public InputStream executeCommands(List<String> commands)
            throws IOException {
        if ((commands == null) || (commands.size() == 0)) {
            return null;
        }
        try {
            Thread.sleep(100L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ProcessBuilder builder = new ProcessBuilder(commands);
        return builder.start().getInputStream();
    }

    public InputStream executeCommands(String[] commands)
            throws IOException {
        List<String> asList = Arrays.asList(commands);
        return executeCommands(asList);
    }
}
