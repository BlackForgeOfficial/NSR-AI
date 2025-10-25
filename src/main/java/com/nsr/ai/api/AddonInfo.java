package com.nsr.ai.api;

public class AddonInfo {
    private final String name;
    private final String version;
    private final String author;
    private final boolean loaded;

    public AddonInfo(String name, String version, String author, boolean loaded) {
        this.name = name;
        this.version = version;
        this.author = author;
        this.loaded = loaded;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public String getAuthor() {
        return author;
    }

    public boolean isLoaded() {
        return loaded;
    }
}
