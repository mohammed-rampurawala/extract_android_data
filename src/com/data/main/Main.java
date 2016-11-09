package com.data.main;

import com.mohom.extract.ExtractFiles;
import com.mohom.misc.Utils;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Utils.checkDeviceConnected();

        Scanner scanner = new Scanner(System.in);
        System.out.println("Thanks for using... \n By Mohammed Rampurawala");
        System.out.println("Please enter package name");
        String packageName = scanner.nextLine();
        if (!Utils.isPackageValid(packageName)) {
            System.out.println("Package name you entered is invalid");
            System.out.println("Do you want to continue? Y:N");
            String yesOrNo = scanner.nextLine().toLowerCase();
            if (yesOrNo.startsWith("y")) {
                main(null);
            } else {
                scanner.close();
                System.exit(0);
            }
        } else {
            System.out.println("you entered:" + packageName);
            ExtractFiles name = new ExtractFiles();
            name.startExtraction(packageName);
            System.out.println("Done");
        }
    }
}
