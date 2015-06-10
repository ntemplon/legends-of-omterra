package com.jupiter.europa.tools;

import com.jupiter.europa.io.FileLocations;
import jbt.tools.btlibrarygenerator.ActionsAndConditionsGenerator;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nathan on 5/27/15.
 */
public class ActionsAndConditionsGeneration {

    // Constants
    public static final boolean OVERWRITE = false;
    public static final Path FILE = FileLocations.AI_DIRECTORY.resolve("domain-configuration.xml");


    public static void main(String... args) {
        List<String> options = new ArrayList<>();

        options.add("-c");
        options.add(FILE.toString());
        if (OVERWRITE) {
            options.add("-o");
        }
        options.add("-r");
        options.add(FileLocations.AI_DIRECTORY.toString() + "/");

        ActionsAndConditionsGenerator.main(options.toArray(new String[options.size()]));
    }
}
