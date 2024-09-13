package RSS;

import RSS.reader.NewsSystem;

import java.io.File;

public class App {

    public static void main(String[] args) {
        String path = System.getProperty("user.dir") + File.separator + "sources.txt";
        NewsSystem newsSystem = new NewsSystem(path);

        Thread t = new Thread(newsSystem);
        t.start();

    }
}
