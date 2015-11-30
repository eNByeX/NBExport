package io.github.enbyex.nbexport.exporter.txt;

import io.github.enbyex.core.nbs.NBSHeader;
import io.github.enbyex.core.nbs.NBSNote;
import io.github.enbyex.core.nbs.NBSSong;
import io.github.enbyex.core.util.Note;
import io.github.enbyex.nbexport.api.Arguments;
import io.github.enbyex.nbexport.api.exporter.Exporter;

import javax.annotation.Nonnull;
import java.io.*;
import java.util.Optional;

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
        pw.println(".------------------------.");
        pw.println("| NBS TXT Exporter V 1.1 |");
        pw.println("|------------------------|");
        pw.printf("|  %1d custom instruments  |", song.instruments());
        pw.println();
        pw.println("|------------,-----------|");
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
        chars[0] = '|';
        chars[13] = '\'';
        chars[25] = '\'';
        if (chars.length != 26) chars[chars.length - 1] = '.';
        pw.println(chars);
        chars[13] = '-';
        chars[25] = '-';
        if (chars.length != 26) chars[chars.length - 1] = '\'';
        pw.printf("| Song: %-" + (len - 10) + "s |", header.getName().replace('\n', ' '));
        pw.println();
        pw.printf("| By: %-" + (len - 8) + "s |", header.getAuthor().replace('\n', ' '));
        pw.println();
        if (!header.getOriginalAuthor().isEmpty()) {
            pw.printf("| Originally by: %-" + (len - 19) + "s |", header.getOriginalAuthor().replace('\n', ' '));
            pw.println();
        }
        String fmtdescline = "| %-" + (len - 19) + "s |";
        for (String line : lines) {
            pw.printf(fmtdescline, line);
            pw.println();
        }
        int linelen = song.songHeight() * 7 + 1;
        char[] tsigline = new char[linelen];
        for (int i = 0; i < linelen; i++) {
            tsigline[i] = '-';
            if (i % 7 == 0) {
                if (i < chars.length) {
                    chars[i] = ',';
                }
                if (i == chars.length - 1) {
                    chars[i] = '|';
                }
                tsigline[i] = '|';
            }
        }
        chars[0] = '|';
        pw.println(chars);
        pw.flush();
        int tsig = header.getTimeSignature();
        for (int i = 0; i < song.length(); i++) {
            if (i > 0 && i % tsig == 0) pw.println(tsigline);
            for (int j = 0; j < song.songHeight(); j++) {
                Optional<NBSNote> note = song.getNote(i, j);
                if (note.isPresent()) {
                    NBSNote n = note.get();
                    pw.printf("|%2d:%-3s", n.getInstrument(), Note.noteName(n.getNote()));
                } else {
                    pw.printf("|      ");
                }
            }
            pw.println("|");
        }
        pw.flush();
    }
}
