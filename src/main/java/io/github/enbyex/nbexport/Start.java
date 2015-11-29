package io.github.enbyex.nbexport;

import io.github.enbyex.core.nbs.NBSSong;
import io.github.enbyex.core.util.NBSInputStream;
import io.github.enbyex.core.util.NBSOutputStream;
import io.github.enbyex.nbexport.api.TypeNotSupportedException;
import io.github.enbyex.nbexport.api.exporter.Exporter;
import io.github.enbyex.nbexport.api.exporter.ExporterFactory;
import io.github.enbyex.nbexport.exporter.ExporterService;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;

import java.io.*;

/**
 * Standalone NBExport main class.
 *
 * @author soniex2
 */
public class Start {
    public static void main(String[] args) {
        OptionParser parser = new OptionParser();
        OptionSpec<String> optexporter = parser.accepts("exporter").withRequiredArg().required();
        OptionSpec<File> optin = parser.accepts("input").withRequiredArg().ofType(File.class).required();
        OptionSpec<File> optout = parser.accepts("output").withRequiredArg().ofType(File.class);
        OptionSpec<Void> opthelp = parser.accepts("help").forHelp();
        OptionSet opt = parser.parse(args);
        if (opt.has(opthelp)) {
            try {
                parser.printHelpOn(System.out);
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
            return;
        }
        ExporterService exporters = ExporterService.getInstance();
        ExporterFactory exporterFactory = exporters.forName(opt.valueOf(optexporter));
        if (exporterFactory == null) {
            System.err.println("Unknown exporter: " + opt.valueOf(optexporter));
            System.exit(1);
            return;
        }
        NBSInputStream is = null;
        NBSOutputStream os = null;
        int ecode = 0;
        try {
            NBSSong song = new NBSSong();
            is = new NBSInputStream(new FileInputStream(opt.valueOf(optin)));
            song.readFrom(is);
            Exporter<NBSSong> exporter = exporterFactory.getExporter(NBSSong.class);
            File out;
            if (opt.has(optout)) {
                out = opt.valueOf(optout);
            } else {
                File in = opt.valueOf(optin);
                File p = in.getParentFile();
                String n = in.getName();
                out = new File(p, n + ".txt"); // TODO ask exporter for ext
            }
            os = new NBSOutputStream(new FileOutputStream(out));
            exporter.write(song, os, null);
        } catch (FileNotFoundException e) {
            System.err.println("File not found");
            ecode = 1;
        } catch (IOException e) {
            e.printStackTrace();
            ecode = 1;
        } catch (TypeNotSupportedException e) {
            System.err.println("File type not supported by exporter");
            ecode = 1;
        } finally {
            try {
                if (is != null) is.close();
            } catch (IOException ignored) {

            }
            try {
                if (os != null) os.close();
            } catch (IOException ignored) {

            }
        }
    }
}
