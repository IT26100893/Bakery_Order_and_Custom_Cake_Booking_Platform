package com.bakery.bakeryordersystem.repository;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;
/** Generic text-file storage: one record per line, fields separated by '|'. */
public abstract class FileRepository<T> {
    private final Path file;
    protected FileRepository(String name) {
        file = Paths.get("data", name);
        try { Files.createDirectories(file.getParent()); if (Files.notExists(file)) Files.createFile(file); }
        catch (IOException e) { throw new UncheckedIOException(e); }
    }
    protected abstract String toLine(T t);
    protected abstract T fromLine(String line);
    protected abstract String idOf(T t);
    public synchronized List<T> findAll() {
        try (Stream<String> s = Files.lines(file)) { return s.filter(l -> !l.isBlank()).map(this::fromLine).collect(Collectors.toList()); }
        catch (IOException e) { throw new UncheckedIOException(e); }
    }
    public Optional<T> findById(String id) { return findAll().stream().filter(t -> idOf(t).equals(id)).findFirst(); }
    public synchronized void save(T t) {
        List<T> all = findAll(); boolean found = false;
        for (int i = 0; i < all.size(); i++) if (idOf(all.get(i)).equals(idOf(t))) { all.set(i, t); found = true; }
        if (!found) all.add(t);
        write(all);
    }
    public synchronized void delete(String id) { List<T> all = findAll(); all.removeIf(t -> idOf(t).equals(id)); write(all); }
    private void write(List<T> all) {
        try { Files.write(file, all.stream().map(this::toLine).collect(Collectors.toList())); }
        catch (IOException e) { throw new UncheckedIOException(e); }
    }
    public static String newId(String prefix) { return prefix + UUID.randomUUID().toString().substring(0, 6).toUpperCase(); }
}
