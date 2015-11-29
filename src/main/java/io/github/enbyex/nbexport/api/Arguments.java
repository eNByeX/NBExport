package io.github.enbyex.nbexport.api;

/**
 * @author soniex2
 */
public interface Arguments {
    void setStringArgument(String name);

    void setStringArgument(String name, String defaultValue);

    void setIntArgument(String name);

    void setIntArgument(String name, int defaultValue);

    <T extends Enum<T>> void setEnumArgument(String name, Class<T> type);

    <T extends Enum<T>> void setEnumArgument(String name, Class<T> type, T defaultValue);

    Arguments setCategoryArgument(String name);

    /**
     * Commit the arguments. Call this when you want to read arguments.
     */
    void commit();
}
