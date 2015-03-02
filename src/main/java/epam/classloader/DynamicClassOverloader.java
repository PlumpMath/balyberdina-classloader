package epam.classloader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DynamicClassOverloader extends ClassLoader {
    private final String[] classPath;
    private Map<String, Class<?>> classesHash = new HashMap<>();

    public DynamicClassOverloader(String[] classPath) {
        this.classPath = classPath;
    }

    protected synchronized Class<?> loadClass(String name, boolean resolve)
            throws ClassNotFoundException {
        Class<?> result = findClass(name);
        if (resolve) {
            resolveClass(result);
        }
        return result;
    }

    protected Class<?> findClass(String name) throws ClassNotFoundException {
        Class<?> result = (Class<?>) classesHash.get(name);
        if (result != null) {
            return result;
        }
        File f = findFile(name.replace('.', File.separatorChar), ".class");

        if (f == null) {
            return findSystemClass(name);
        }

        try {
            byte[] classBytes = loadFileAsBytes(f);
            result = defineClass(name, classBytes, 0, classBytes.length);
        } catch (IOException e) {
            throw new ClassNotFoundException("Cannot load class " + name + ": "
                    + e);
        } catch (ClassFormatError e) {
            throw new ClassNotFoundException(
                    "Format of class file incorrect for class " + name + ": "
                            + e);
        }

        classesHash.put(name, result);
        return result;
    }

    private File findFile(String name, String extension) {
        for (int k = 0; k < classPath.length; k++) {
            File f = new File(new File(classPath[k]).getPath()
                    + File.separatorChar + name + extension);
            if (f.exists()) {
                return f;
            }
        }
        return null;
    }

    public static byte[] loadFileAsBytes(File file) throws IOException {
        byte[] result = new byte[(int) file.length()];
        try (FileInputStream f = new FileInputStream(file)) {
            f.read(result, 0, result.length);
        }
        return result;
    }
}
