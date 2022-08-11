package NIO;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class PathAndFiles {
    public static void main(String[] args) throws IOException {
        Path path = Paths.get("./common/src/main/resources/1.txt");
//        path = path.toAbsolutePath();
//        while (path.getParent() != null) {
//            System.out.println(path.getParent());
//            path = path.getParent();
//        }
//        Files.walkFileTree(path, new FileVisitor<Path>() {
//            @Override
//            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
//                return null;
//            }
//
//            @Override
//            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
//                return null;
//            }
//
//            @Override
//            public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
//                return null;
//            }
//
//            @Override
//            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
//                return null;
//            }
//        });
        System.out.println(Files.exists(path));
        if (!Files.exists(path)) {
            Files.createFile(path);
        }
        System.out.println(Files.exists(path));
        Path p2 = Paths.get(path.getParent().toString(), "2.txt");
        Files.copy(path, p2,
                StandardCopyOption.REPLACE_EXISTING);
        Files.write(path, "Hello world".getBytes(), StandardOpenOption.APPEND);
        Files.lines(path).sorted().forEach(System.out::println);
    }
}