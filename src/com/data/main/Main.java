package com.data.main;

import com.data.commands.BaseCommands;
import com.data.commands.NonRootCommands;
import com.data.commands.RootCommands;
import com.data.extract.ExtractFiles;
import com.data.misc.Constants;
import com.data.misc.Utils;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        BaseCommands commands = null;
        String path = null;
        String packageName = null;

        Utils.checkDeviceConnected();

        Scanner scanner = new Scanner(System.in);

        for (String arg : args) {
            if (arg.startsWith(Constants.PACKAGE_NAME)) {
                packageName = arg.substring(arg.indexOf(":") + 1);
                boolean isValid = Utils.isPackageValid(packageName);
                if (!isValid) {
                    invalidMessage("Package Name is not valid. Do you want to continue?", scanner);
                }
            } else if (arg.startsWith(Constants.PATH)) {
                path = arg.substring(arg.indexOf(":") + 1);
                boolean validPath = Utils.isValidPath(path);
                if (!validPath) {
                    invalidMessage("Path inserted is not valid. Do you want to continue?", scanner);
                }
            }
        }


        System.out.println("Thanks for using...");

        boolean isDeviceRooted = Utils.checkIfDeviceIsRooted();
        commands = isDeviceRooted ? new RootCommands() : new NonRootCommands();

        if (packageName == null) {
            System.out.println("Please enter package name");
            packageName = scanner.nextLine();
        }


        if (!Utils.isPackageValid(packageName)) {
            invalidMessage("Package Name is not valid. Do you want to continue?", scanner);
        } else {
            System.out.println("You have entered:" + packageName);
            ExtractFiles name = new ExtractFiles(commands);
            name.setExtractionPath(path);
            name.startExtraction(packageName);
            System.out.println("Done");
        }
    }

    private static void invalidMessage(String message, Scanner scanner) {
        System.out.println(message);
        System.out.println("Do you want to continue? Y:N");
        String yesOrNo = scanner.nextLine().toLowerCase();
        if (yesOrNo.startsWith("y")) {
            main(null);
        } else {
            scanner.close();
            System.exit(0);
        }
    }
}
