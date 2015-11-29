package io.github.enbyex.nbexport.exporter;

import io.github.enbyex.nbexport.Constants;
import io.github.enbyex.nbexport.api.exporter.ExporterFactory;

import java.util.*;

/**
 * @author soniex2
 */
public class ExporterService {
    private static ExporterService instance;
    private ServiceLoader<ExporterFactory> loader;

    private Map<String, ExporterFactory> nameToFactory = new HashMap<>();
    private Map<String, ExporterFactory> aliasToFactory = new HashMap<>();
    private Set<String> conflictingAlias = new HashSet<>();

    private boolean initialized = false;

    private ExporterService() {
        loader = ServiceLoader.load(ExporterFactory.class);
    }

    public static synchronized ExporterService getInstance() {
        if (instance == null) {
            instance = new ExporterService();
        }
        return instance;
    }

    private synchronized void init() {
        for (Iterator<ExporterFactory> iter = loader.iterator(); iter.hasNext(); /*nothing*/) {
            ExporterFactory f = iter.next();

            // min version check
            String minVersion = f.getMinVersion();
            // TODO check version

            // version check
            if (!f.checkVersionCompatibility(Constants.VERSION)) continue;

            String name = f.getName();
            if (nameToFactory.containsKey(name))
                throw new RuntimeException("Found 2 ExporterFactories with the same name!");
            nameToFactory.put(name, f);

            List<String> aliases = f.getAliases();

            for (String alias : aliases) {
                if (aliasToFactory.containsKey(alias)) {
                    aliasToFactory.remove(alias);
                    conflictingAlias.add(alias);
                }
                if (!conflictingAlias.contains(alias))
                    aliasToFactory.put(alias, f);
            }
        }
        initialized = true;
    }

    public synchronized ExporterFactory forName(String name) {
        if (!initialized) init();
        if (nameToFactory.containsKey(name)) {
            return nameToFactory.get(name);
        }
        if (aliasToFactory.containsKey(name)) {
            return aliasToFactory.get(name);
        }
        return null;
    }
}
