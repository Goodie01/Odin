package org.goodiemania.odin.internal.database;

public class ParameterPair {
    private final String name;
    private final String value;
    private final int count;

    public ParameterPair(final String name, final String value, final int count) {
        this.name = name;
        this.value = value;
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public int getCount() {
        return count;
    }
}