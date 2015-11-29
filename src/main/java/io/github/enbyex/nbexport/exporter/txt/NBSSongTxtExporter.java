package io.github.enbyex.nbexport.exporter.txt;

import io.github.enbyex.core.nbs.NBSHeader;
import io.github.enbyex.core.nbs.NBSSong;
import io.github.enbyex.nbexport.api.Arguments;
import io.github.enbyex.nbexport.api.exporter.Exporter;

import javax.annotation.Nonnull;
import java.io.*;

/**
 * @author soniex2
 */
public class NBSSongTxtExporter implements Exporter<NBSSong> {
    private int max(int... args) {
        int i = Integer.MIN_VALUE;
        for (int arg : args) {
            i = Math.max(i, arg);
        }
        return i;
    }

    @Override
    public void write(@Nonnull NBSSong song, @Nonnull OutputStream outputStream, @Nonnull Arguments arguments) throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
        PrintWriter pw = new PrintWriter(bufferedWriter);
        NBSHeader header = song.getHeader();
        pw.println("--------------------------");
        pw.println("| NBS TXT Exporter V 1.0 |");
        pw.println("--------------------------");
        pw.printf("| TPS: %02d.%02d | tsig: %1d/4 |", header.getTempo() / 100, header.getTempo() % 100, header.getTimeSignature());
        pw.println();
        String[] lines = header.getDescription().split("\n");
        int descmaxlen = 0;
        for (String line : lines) {
            descmaxlen = Math.max(descmaxlen, line.length());
        }
        int len = 4 // for "| " and " |"
                + max(4 + header.getAuthor().length(), // "By: " + author
                      6 + header.getName().length(), // "Song: " + name
                      header.getOriginalAuthor().isEmpty() ? 0 : 15 + header.getOriginalAuthor().length(), // "Originally by: " + original author
                      descmaxlen, // see above loop
                      22); // "--------------------------" (as in above separators) - 4
        char[] chars = new char[len];
        for (int i = 0; i < len; i++) {
            chars[i] = '-';
        }
        pw.println(chars);
        pw.printf("| Song: %-" + (len - 10) + "s |", header.getName());
        pw.println();
        pw.printf("| By: %-" + (len - 8) + "s |", header.getAuthor());
        pw.println();
        if (!header.getOriginalAuthor().isEmpty()) {
            pw.printf("| Originally by: %-" + (len - 19) + "s |", header.getOriginalAuthor());
            pw.println();
        }
        String fmtdescline = "| %-" + (len - 19) + "s |";
        for (String line : lines) {
            pw.printf(fmtdescline, line);
            pw.println();
        }
        pw.println(chars);
        pw.flush();
    }
}
