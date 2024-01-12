package com.lynsis.velocitymail.utils;

import com.lynsis.velocitymail.VelocityMail;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.yaml.YAMLConfigurationLoader;
import org.apache.commons.io.IOUtils;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileUtils {

    public static @NonNull ConfigurationNode loadOrCreate(String name) throws IOException {
        File dataDirectory = VelocityMail.get().getDataDirectory().toFile();
        dataDirectory.mkdir();

        File copyFile = new File(dataDirectory, name + ".yml");

        if (!copyFile.exists()) {
            copyFile.getParentFile().mkdirs();
            copyFile = copyResource(name + ".yml", copyFile);
        }

        return YAMLConfigurationLoader.builder().setFile(copyFile).build().load();
    }

    public static File copyResource(String resourceFileName, File file) {
        InputStream inputStream = FileUtils.class.getClassLoader().getResourceAsStream(resourceFileName);

        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            IOUtils.copy(inputStream, outputStream);

            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file;
    }

}
