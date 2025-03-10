package dnd.logic.random_generator;

import java.io.*;

/**
 * Reads "random" numbers from a file. Used in deterministic mode.
 */
public class FileRandomGenerator implements RandomGenerator, Closeable {
    private final BufferedReader reader;

    public FileRandomGenerator(String path) throws FileNotFoundException {
        reader = new BufferedReader(new FileReader(path));
    }

    @Override
    public int nextInt(int n) {
        try {
            return Integer.parseInt(reader.readLine());
        }
        catch (IOException e) {
            throw new RuntimeException("io exception", e);
        }
    }

    @Override
    public void close() throws IOException {
        reader.close();
    }
}
