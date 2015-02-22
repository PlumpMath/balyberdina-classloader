package epam.classloader;

import java.io.File;
import java.io.IOException;

public class TestModuleFactory {
    private final String name;
    private final String destPath;
    private final String srcPath;
    private final String destRoot;
    private DynamicClassOverloader cache;
    private long lastModified = 0;

    public TestModuleFactory(String name, String destDir, String srcDir) {

        String dPath = destDir;
        String[] fileComponents = name.split(".");
        for (int i = 0; i < fileComponents.length - 1; i++) {
            dPath += File.separator + fileComponents[i];
        }
        this.srcPath = srcDir + File.separator
                + name.replace(".", File.separator) + ".java";
        this.destPath = dPath;
        this.destRoot = destDir;
        this.name = name;
    }

    public Object newInstance() throws ClassNotFoundException,
            InstantiationException, IllegalAccessException, IOException,
            InterruptedException {
        File file = new File(srcPath);
        if (file.lastModified() > lastModified) {
            Compiler.compile(srcPath, destPath);
            lastModified = file.lastModified(); 
            cache = new DynamicClassOverloader(new String[] { destRoot });
        }
        return Class.forName(name, true, cache).newInstance();
    }
}
