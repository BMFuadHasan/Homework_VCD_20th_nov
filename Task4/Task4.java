import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

public class Task4 {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Enter a directory name: ");
            String directoryName = scanner.nextLine().trim();

            try {
                Path directory = Paths.get(directoryName).toAbsolutePath().normalize();
                if (!Files.isDirectory(directory)) {
                    System.err.println("Error: the specified directory does not exist.");
                    return;
                }

                Map<Path, Long> directorySizes = calculateDirectorySizes(directory);
                System.out.println("Directory sizes (including subdirectories):");
                for (Map.Entry<Path, Long> entry : directorySizes.entrySet()) {
                    printSize(entry.getKey().toString(), entry.getValue());
                }
                printSize("Total", directorySizes.get(directory));
            } catch (InvalidPathException e) {
                System.err.println("Error: the directory name is not a valid path.");
            } catch (IOException e) {
                System.err.println("Error: could not read the directory: " + e.getMessage());
            }
        }
    }

    private static Map<Path, Long> calculateDirectorySizes(Path root) throws IOException {
        Map<Path, Long> sizes = new LinkedHashMap<>();
        Files.walkFileTree(root, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path directory, BasicFileAttributes attributes) {
                sizes.put(directory, 0L);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attributes) {
                if (attributes.isRegularFile()) {
                    for (Path parent = file.getParent(); parent != null && parent.startsWith(root);
                         parent = parent.getParent()) {
                        sizes.merge(parent, attributes.size(), Long::sum);
                    }
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException error) throws IOException {
                throw error;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path directory, IOException error) throws IOException {
                if (error != null) {
                    throw error;
                }
                return FileVisitResult.CONTINUE;
            }
        });
        return sizes;
    }

    private static void printSize(String label, long bytes) {
        double kilobytes = bytes / 1024.0;
        double megabytes = bytes / (1024.0 * 1024.0);
        System.out.printf(Locale.ROOT, "%s: %.2f MB, %.2f KB, %d bytes%n",
                label, megabytes, kilobytes, bytes);
    }
}
