package ca.bgiroux.gravitrips.controller;

import ca.bgiroux.gravitrips.model.MoveStrategy;
import ca.bgiroux.gravitrips.model.RandomMoveStrategy;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StrategyLoader {
    private static final Logger LOGGER = Logger.getLogger(StrategyLoader.class.getName());
    private static final String PLUGIN_DIR = "ai_players";

    public List<MoveStrategy> loadStrategies() {
        List<MoveStrategy> strategies = new ArrayList<>();
        strategies.add(new RandomMoveStrategy());
        strategies.addAll(loadPluginStrategies());
        return strategies;
    }

    private List<MoveStrategy> loadPluginStrategies() {
        Path pluginDir = Paths.get(PLUGIN_DIR);
        if (!Files.exists(pluginDir) || !Files.isDirectory(pluginDir)) {
            LOGGER.info(() -> "Plugin directory not found: " + pluginDir.toAbsolutePath());
            return List.of();
        }

        List<MoveStrategy> strategies = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(pluginDir, path ->
                Files.isRegularFile(path) && (path.toString().endsWith(".jar") || path.toString().endsWith(".class")))) {
            for (Path path : stream) {
                strategies.addAll(loadFromPath(path));
            }
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Failed to read plugin directory", e);
        }
        return strategies;
    }

    private List<MoveStrategy> loadFromPath(Path path) {
        if (path.toString().endsWith(".jar")) {
            return loadFromJar(path);
        }
        if (path.toString().endsWith(".class")) {
            return loadSingleClass(path);
        }
        return List.of();
    }

    private List<MoveStrategy> loadFromJar(Path jarPath) {
        List<MoveStrategy> strategies = new ArrayList<>();
        try (JarFile jarFile = new JarFile(jarPath.toFile());
             URLClassLoader loader = new URLClassLoader(new URL[]{jarPath.toUri().toURL()}, MoveStrategy.class.getClassLoader())) {
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                if (!entry.getName().endsWith(".class") || entry.getName().contains("$")) {
                    continue;
                }
                String className = entry.getName()
                        .replace('/', '.')
                        .replace(".class", "");
                loadStrategyClass(loader, className).ifPresent(strategies::add);
            }
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Failed to load jar plugin: " + jarPath, e);
        }
        return strategies;
    }

    private List<MoveStrategy> loadSingleClass(Path classFile) {
        Path root = Paths.get(PLUGIN_DIR);
        if (!classFile.startsWith(root)) {
            root = classFile.getParent();
        }
        String className = stripClassName(root, classFile);
        if (className == null) {
            return List.of();
        }
        try (URLClassLoader loader = new URLClassLoader(new URL[]{root.toUri().toURL()}, MoveStrategy.class.getClassLoader())) {
            return loadStrategyClass(loader, className).map(List::of).orElse(List.of());
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Failed to load class plugin: " + classFile, e);
            return List.of();
        }
    }

    private String stripClassName(Path root, Path classFile) {
        Path relative = root.relativize(classFile);
        String name = relative.toString();
        if (!name.endsWith(".class")) {
            return null;
        }
        return name.replace('/', '.').replace('\\', '.').replace(".class", "");
    }

    private java.util.Optional<MoveStrategy> loadStrategyClass(ClassLoader loader, String className) {
        try {
            Class<?> clazz = Class.forName(className, true, loader);
            if (!MoveStrategy.class.isAssignableFrom(clazz)) {
                return java.util.Optional.empty();
            }
            Object instance = clazz.getDeclaredConstructor().newInstance();
            return java.util.Optional.of((MoveStrategy) instance);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Failed to load strategy class: " + className, e);
            return java.util.Optional.empty();
        }
    }
}
