package uk.co.droidinactu.ebooklauncher.contenttable;

import java.io.File;

public class Storage {
    private static final String CACHE_DIRECTORY = "cache";
    private static final String DATABASE_DIRECTORY = "database";
    private static final String MEDIA_DIRECTORY = "media";
    private static final String READER_DIRECTORY = "Sony_Reader";
    private static final String SYNC_DIRECTORY = "sync";

    public static String getCacheDirectoryName() {
        return getDatabaseDirectoryName() + File.separatorChar + "cache";
    }

    public static String getDatabaseDirectoryName() {
        return "Sony_Reader" + File.separatorChar + "database";
    }

    public static String getMediaDirectoryName() {
        return "Sony_Reader" + File.separatorChar + "media";
    }

    public static String getReaderDirectoryName() {
        return "Sony_Reader";
    }

    public static String getSyncDirectoryName() {
        return getDatabaseDirectoryName() + File.separatorChar + "sync";
    }
}