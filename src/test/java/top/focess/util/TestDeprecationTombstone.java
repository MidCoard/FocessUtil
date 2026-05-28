package top.focess.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestDeprecationTombstone {

    @Test
    public void testAllTopLevelTypesAreDeprecated() throws IOException, ClassNotFoundException {
        final Path sourceRoot = Paths.get("src/main/java/top/focess/util");
        try (final Stream<Path> stream = Files.walk(sourceRoot)) {
            final List<String> classNames = stream
                    .filter(path -> path.toString().endsWith(".java"))
                    .map(sourceRoot::relativize)
                    .map(Path::toString)
                    .map(path -> "top.focess.util." + path.replace('/', '.').replace('\\', '.').replaceAll("\\.java$", ""))
                    .collect(Collectors.toList());
            for (final String className : classNames) {
                Assertions.assertTrue(Class.forName(className).isAnnotationPresent(Deprecated.class), className);
            }
        }
    }
}
