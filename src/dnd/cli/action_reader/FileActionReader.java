package dnd.cli.action_reader;

import dnd.cli.printer.Printer;

import java.io.*;

public class FileActionReader implements ActionReader, Closeable {
    private BufferedReader reader;
    private final Printer printer;

    public FileActionReader(String path, Printer printer) throws FileNotFoundException {
        reader = new BufferedReader(new FileReader(path));
        this.printer = printer;
    }

    @Override
    public String nextAction() {
        try {
            String action = reader.readLine();
            printer.printLine(action);
            return action;
        }
        catch (IOException e) {
            throw new RuntimeException("io exception", e);
        }
    }

    @Override
    public void close() throws IOException {
        if (reader != null) {
            reader.close();
            reader = null;
        }
    }
}
