package FileReader;

import java.io.*;

public class ReaderTXT {
    public static void read(String text, File file) throws IOException {
        file.createNewFile();
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(text);
        fileWriter.flush();
    }

    public static File createNewFile(File file) throws IOException {
        file.createNewFile();
        return file;
    }
}
