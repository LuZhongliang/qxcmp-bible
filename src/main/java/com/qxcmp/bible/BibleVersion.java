package com.qxcmp.bible;

/**
 * 支持的圣经版本
 *
 * @author Aaric
 */
public enum BibleVersion {

    JTHHB("简体和合本"),

    JTXYB("简体新译本"),

    FTHHB("繁体和合本"),

    FTXYB("繁体新译本"),

    NIV("NIV");

    private String name;

    BibleVersion(String name) {
        this.name = name;
    }

    static BibleVersion fromName(String name) {

        for (BibleVersion version : values()) {
            if (version.getName().equals(name)) {
                return version;
            }
        }


        return null;
    }

    public String getName() {
        return name;
    }
}
