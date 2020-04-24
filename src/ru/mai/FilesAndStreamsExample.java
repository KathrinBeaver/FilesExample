package ru.mai;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.stream.Stream;

/**
 * @author Beaver
 */
public class FilesAndStreamsExample {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        useFileWriter();
        useBufferedIOStream();
        useDataIOStream();
        readLines();
        writeLines();
        useFile();
        listOfFiles();
    }

    /**
     * Пример записи в файл и чтения массива байт
     */
    private static void useFileWriter() {
        byte[] bytesToWrite = {1, 2, 3};
        byte[] bytesReaded = new byte[10];
        final String FILE_NAME = "test.txt";

        FileOutputStream outFile = null;
        FileInputStream inFile = null;

        try {
            // создать выходной поток
            outFile = new FileOutputStream(FILE_NAME);
            System.out.println("File is open to write");

            // записать массив
            outFile.write(bytesToWrite);
            System.out.println("Written: " + bytesToWrite.length + " byte");
            System.out.println();

            // создать входной поток
            inFile = new FileInputStream(FILE_NAME);
            System.out.println("File is open to read");

            // узнать, сколько байт готово к считыванию
            int bytesAvailable = inFile.available();
            System.out.println("Ready to read: " + bytesAvailable + " byte");
            System.out.println();

            // считать в массив
            int count = inFile.read(bytesReaded, 0, bytesAvailable);
            System.out.println("Read: " + count + " byte");
            for (int i = 0; i < count; i++) {
                System.out.print(bytesReaded[i] + " ");
            }
        } catch (FileNotFoundException e) {
            System.out.println("Its impossible to write to file: " + FILE_NAME);
        } catch (IOException e) {
            System.out.println("Input/output error: " + e.toString());
        } finally {
            try {
                System.out.println();
                outFile.close();
                System.out.println("Output stream is closed");
                inFile.close();
                System.out.println("Input stream is closed");
            } catch (IOException ex) {
                System.out.println("Error...");
            }
        }
    }

    /**
     * Пример работы с буферизированными потоками, сравнение скорости работы
     */
    private static void useBufferedIOStream() {
        final String FILE_NAME = "file1";
        InputStream inStream = null;
        BufferedInputStream bufInStream = null;
        OutputStream outStream = null;
        BufferedOutputStream bufOutStream = null;

        long timeStart = System.currentTimeMillis();

        try {
            outStream = new FileOutputStream(FILE_NAME);
            for (int i = 1000000; --i >= 0; ) {
                outStream.write(i);
            }
            long time = System.currentTimeMillis() - timeStart;
            System.out.println("Writing time: " + time + " millisec");

            timeStart = System.currentTimeMillis();

            bufOutStream = new BufferedOutputStream(outStream);
            for (int i = 1000000; --i >= 0; ) {
                bufOutStream.write(i);
            }

            time = System.currentTimeMillis() - timeStart;
            System.out.println("Writing time: " + time + " millisec");
            bufOutStream.close();
            outStream.close();

            // Определение времени считывания без буферизации
            timeStart = System.currentTimeMillis();
            inStream = new FileInputStream(FILE_NAME);

            while (inStream.read() != -1) {
            }

            time = System.currentTimeMillis() - timeStart;
            inStream.close();
            System.out.println("Direct read time: " + (time) + " millisec");

            // Применение буферизации
            timeStart = System.currentTimeMillis();
            inStream = new FileInputStream(FILE_NAME);
            bufInStream = new BufferedInputStream(inStream);
            while (bufInStream.read() != -1) {
            }

            time = System.currentTimeMillis() - timeStart;
            inStream.close();
            System.out.println("Buffered read time: " + (time) + " millisec");
        } catch (FileNotFoundException ex) {
            System.out.println("Its impossible to write to file: " + FILE_NAME);
        } catch (IOException ex) {
            System.out.println("IOException: " + ex.toString());
        }
    }

    /**
     * Пример чтения и записи данных разных типов
     */
    private static void useDataIOStream() {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            DataOutputStream outData = new DataOutputStream(out);
            outData.writeByte(5);
            outData.writeInt(128);
            outData.writeLong(128);
            outData.writeDouble(128);
            outData.close();

            byte[] bytes = out.toByteArray();
            InputStream in = new ByteArrayInputStream(bytes);
            DataInputStream inData = new DataInputStream(in);
            System.out.println("Correct sequence reading:");
            System.out.println("readByte: " + inData.readByte());
            System.out.println("readInt: " + inData.readInt());
            System.out.println("readLong: " + inData.readLong());
            System.out.println("readDouble: " + inData.readDouble());
            inData.close();

            System.out.println();
            System.out.println("Inverted sequence reading:");
            in = new ByteArrayInputStream(bytes);
            inData = new DataInputStream(in);
            System.out.println("readInt: " + inData.readInt());
            System.out.println("readDouble: " + inData.readDouble());
            System.out.println("readLong: " + inData.readLong());
            inData.close();
        } catch (Exception ex) {
            System.out.println("Impossible IOException occurs: " + ex.toString());
        }
    }

    /**
     * Построчное чтение файлов
     */
    private static void readLines() {
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File("verse.txt"));

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                System.out.println(line);
            }

            System.out.println("-------------------------");

            try (Stream<String> stream = Files.lines(Paths.get("verse.txt"))) {
                stream.forEach(System.out::println);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Convert encoding
     */
    private static void writeLines() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("verse.txt"), StandardCharsets.UTF_8));
             Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("verseNonUTF.txt"), "Cp1251"))) {
            String line;
            while ((line = br.readLine()) != null) {
                out.write(line);
                out.write("\n");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Поиск файлов с расширением .java
     */
    private static void useFile() {
        File pathFile = new File(".");
        String filterString = ".java";
        try {
            FileFilter filter = new NameFilter(filterString);
            findFiles(pathFile, filter, System.out);
        } catch (IOException ex) {
            System.out.println("IOException: " + ex.toString());
        }
        System.out.println("Finished");
    }

    private static void findFiles(File file, FileFilter filter, PrintStream output) throws IOException {
        if (file.isDirectory()) {
            File[] list = file.listFiles();
            for (int i = list.length; --i >= 0; ) {
                findFiles(list[i], filter, output);
            }
        } else {
            if (filter.accept(file)) {
                output.println("\t" + file.getName());
                output.println("\t" + file.getParent());
                output.println("\t" + file.isDirectory());
                output.println("\t" + file.getCanonicalPath());
                output.println("\t" + file.getAbsolutePath());
                output.println("\t" + file.getPath());
                output.println();
            }
        }
    }

    /**
     * Получение списков файлов и директорий
     */
    private static void listOfFiles() {
        try {

            if (!Files.exists(Paths.get("newFile.txt"))) {
                try {
                    Files.createFile(Paths.get("newFile.txt"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            Files.list(Paths.get("."))
                    .forEach(System.out::println);

            System.out.println("-------------------------");

            Files.list(Paths.get("."))
                    .filter(Files::isRegularFile)
//                    .filter(Files::isDirectory)
                    .forEach(System.out::println);

            System.out.println("-------------------------");

            Files.newDirectoryStream(Paths.get("."),
                    path -> path.toString().endsWith(".txt"))
                    .forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
