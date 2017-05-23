package com.data.misc;

import com.data.commands.BaseCommands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static boolean isPackageValid(String packageName) {
        if (packageName.isEmpty()) {
            return false;
        }
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
        List<String> listOfCOmmands = new ArrayList<>();
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
            reader.close();
            if (attachedDevices.size() < 2) {
                System.out.println("No devices attached");
                System.exit(1);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getDataFromStream(InputStream stream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        String line = "";
        String data = "";
        while ((line = reader.readLine()) != null) {
            if (!line.isEmpty()) {
                data += line;
            }
        }
        try {
            reader.close();
            stream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }


    private static boolean checkIsDeviceRooted(String isDeviceRooted) {
        return !(isDeviceRooted != null && isDeviceRooted.toLowerCase().contains("denied"));
    }

    public static boolean checkIfDeviceIsRooted() {
        try {
            InputStream inputStream = ExecuteCommands.getInstance().executeCommands(getCheckingRootCommand());
            String input = Utils.getDataFromStream(inputStream);
            boolean isRooted = checkIsDeviceRooted(input);
            if (isRooted) {
                System.out.println("Congratulations!!! your device is rooted...\nYou can extract any package data:) :)");
            }
            return isRooted;
        } catch (Exception e) {
            return false;
        }
    }

    public static List<String> getCheckingRootCommand() {
        List<String> listOfCOmmands = new ArrayList<>();
        listOfCOmmands.add("adb");
        listOfCOmmands.add("shell");
        listOfCOmmands.add("cd root");
        return listOfCOmmands;
    }

    public static void listPackages(BaseCommands commands) {
        try {
            List<String> listOfPackagesInDevice = commands.getListOfPackagesInDevice();
            if (listOfPackagesInDevice.size() > 0) {
                InputStream inputStream = ExecuteCommands.getInstance().executeCommands(listOfPackagesInDevice);
                String input = Utils.getDataFromStream(inputStream);
                input = input.replaceAll("package:", " | ").replaceAll("\n", "");
                System.out.println(input);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
