package io.github.enbyex.nbexport.api.exporter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author soniex2
 */
public abstract class AbstractExporterFactory implements ExporterFactory {

    @Nonnull
    private final String name;

    protected AbstractExporterFactory(@Nonnull String name) {
        this.name = name;
    }

    /**
     * Returns the name of this exporter.
     */
    @Nonnull
    @Override
    public String getName() {
        return name;
    }

    /**
     * Returns a list of aliases for this exporter.
     */
    @Nonnull
    @Override
    public List<String> getAliases() {
        return Collections.emptyList();
    }
}
