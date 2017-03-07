package com.visma.approval;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by robert on 10.02.2017.
 */
public class TestUtils {
    private static ClassLoader classLoader = TestUtils.class.getClassLoader();

    public static String resourceFileContent(String file) throws IOException {
        InputStream fileContent = classLoader.getResourceAsStream(file);
        return IOUtils.toString(fileContent);
    }

    public static List<String> resourceFolderFileContents(String folder) throws IOException {
        List<String> result = new ArrayList<>();

        if (!folder.endsWith("/")){
            folder = folder + "/";
        }
        InputStream streamFileNames = classLoader.getResourceAsStream(folder + ".");

        String strFileNames = IOUtils.toString(streamFileNames);
        String[] fileNames = strFileNames.split("\n");

        for (int i=0; i<fileNames.length; i++){
            String fileContent = resourceFileContent(folder + fileNames[i]);
            result.add(fileContent);
        }
        return result;
    }
}
