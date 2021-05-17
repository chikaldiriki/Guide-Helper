package ru.hse.guidehelper.config;

import java.io.File;

public class ApplicationConfig {
    public static void setCachedUserDTOfile(File cachedUserDTOfile) {
        ApplicationConfig.cachedUserDTOfile = cachedUserDTOfile;
    }

    public static File cachedUserDTOfile;
}
