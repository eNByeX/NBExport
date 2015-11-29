package io.github.enbyex.nbexport.api.exporter;

import io.github.enbyex.nbexport.api.TypeNotSupportedException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * @author soniex2
 */
public interface ExporterFactory {
    /**
     * Get the minimum NBExport version required by this plugin. NBExport will refuse to use this plugin if
     * the version specified here is higher than the active NBExport version.
     *
     * @return A version string representing the minimum NBExport version required by this plugin.
     */
    @Nonnull
    String getMinVersion();

    /**
     * Check for version compatibility. Called after getMinVersion().
     *
     * @param version A version string.
     */
    boolean checkVersionCompatibility(@Nonnull String version);

    /**
     * Returns a new exporter for the given song type.
     *
     * @param type The song type.
     */
    @Nonnull
    <T> Exporter<T> getExporter(@Nonnull Class<T> type) throws TypeNotSupportedException;

    /**
     * Returns the name of this exporter.
     */
    @Nonnull
    String getName();

    /**
     * Returns a list of aliases for this exporter.
     */
    @Nonnull
    List<String> getAliases();
}
