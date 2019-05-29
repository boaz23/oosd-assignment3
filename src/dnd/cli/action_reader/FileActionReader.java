package dnd.cli.action_reader;
import java.io.*;

public class FileActionReader implements ActionReader, Closeable {
    private BufferedReader reader;

    public FileActionReader(String path) throws FileNotFoundException {
        reader = new BufferedReader(new FileReader(path));
    }

    @Override
    public String nextAction() {
        try {
            return reader.readLine();
        }
        catch (IOException e) {
            throw new RuntimeException("io exception", e);
        }
    }

    @Override
    public void close() throws IOException {
        if (reader != null) {
            reader.close();
        }
    }
}
