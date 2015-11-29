package io.github.enbyex.nbexport.exporter.txt;

import io.github.enbyex.core.nbs.NBSSong;
import io.github.enbyex.nbexport.Constants;
import io.github.enbyex.nbexport.api.TypeNotSupportedException;
import io.github.enbyex.nbexport.api.exporter.Exporter;
import io.github.enbyex.nbexport.api.exporter.ExporterFactory;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;

/**
 * @author soniex2
 */
public class TxtExporterFactory implements ExporterFactory {
    /**
     * Get the minimum NBExport version required by this plugin. NBExport will refuse to use this plugin if
     * the version specified here is higher than the active NBExport version.
     *
     * @return A version string representing the minimum NBExport version required by this plugin.
     */
    @Nonnull
    @Override
    public String getMinVersion() {
        return Constants.VERSION;
    }

    /**
     * Check for version compatibility. Called after getMinVersion().
     *
     * @param version A version string.
     */
    @Override
    public boolean checkVersionCompatibility(@Nonnull String version) {
        return version.equals(Constants.VERSION);
    }

    /**
     * Returns an exporter for the given type.
     *
     * @param type
     */
    @Nonnull
    @Override
    @SuppressWarnings("unchecked") // compiler too stupid
    public <T> Exporter<T> getExporter(@Nonnull Class<T> type) throws TypeNotSupportedException {
        if (type == NBSSong.class)
            return (Exporter<T>) new NBSSongTxtExporter();
        throw new TypeNotSupportedException();
    }

    /**
     * Returns the name of this exporter.
     */
    @Nonnull
    @Override
    public String getName() {
        return "TXT";
    }

    private static final List<String> aliases = Arrays.asList("txt", "plaintext", "text");

    /**
     * Returns a list of aliases for this exporter.
     */
    @Nonnull
    @Override
    public List<String> getAliases() {
        return aliases;
    }
}
