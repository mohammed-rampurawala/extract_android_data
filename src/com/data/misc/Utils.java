package com.data.misc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static boolean isPackageValid(String packageName) {
        String[] commands = {"adb",
                "shell", "pm", "list", "packages", packageName};
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    ExecuteCommands.getInstance().executeCommands(commands)));
            if (reader.readLine() == null) {
                return false;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static void checkDeviceConnected() {
        List<String> listOfCOmmands = new ArrayList();
        listOfCOmmands.add("adb");
        listOfCOmmands.add(Constants.DEVICES);

        ProcessBuilder builder = new ProcessBuilder(listOfCOmmands);
        try {
            Process start = builder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    start.getInputStream()));
            List<String> attachedDevices = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.length() > 0) {
                    attachedDevices.add(line);
                }
            }
            if (attachedDevices.size() < 2) {
                System.out.println("No devices attached");
                System.exit(1);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
