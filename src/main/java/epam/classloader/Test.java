package epam.classloader;

import java.io.BufferedReader;
import java.io.InputStreamReader;


public class Test {

    public static void main(String[] argv) throws Exception {
        String name = "epam.classloader.TestModule";
        TestModuleFactory tmf = new TestModuleFactory(name, "target/classes",
                "src/main/java");
        for (;;) {
            Object object = tmf.newInstance();
            System.out.println(object);
            new BufferedReader(new InputStreamReader(System.in)).readLine();
        }

    }

}
