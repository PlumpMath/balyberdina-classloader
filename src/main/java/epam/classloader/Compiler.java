package epam.classloader;

import java.io.IOException;

public class Compiler {
    public static void compile(String sourcePath, String destPath)
            throws IOException, InterruptedException {
        Process p = Runtime.getRuntime().exec(
                new String[] { "javac", "-d", destPath, sourcePath });
        int result = p.waitFor();
        if (result != 0) {
            throw new IllegalStateException();
        }
    }
}
