package io.github.enbyex.nbexport.api.exporter;

import io.github.enbyex.nbexport.api.Arguments;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author soniex2
 */
public interface Exporter<T> {
    void write(@Nonnull T song, @Nonnull OutputStream outputStream, @Nonnull Arguments arguments) throws IOException;
}
