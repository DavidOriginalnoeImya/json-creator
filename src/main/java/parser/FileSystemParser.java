package parser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import entities.FSBase;
import entities.FSDirectory;
import entities.FSFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Blob;
import java.util.Arrays;
import java.util.Base64;
import java.util.Objects;

public class FileSystemParser {
    private final FSDirectory parseDirectory = new FSDirectory();

    private final StringBuilder json = new StringBuilder();

    public void parse(String path) {
        parse(path, 3);

        try (FileWriter fileReader = new FileWriter("output.json")){
            //fileReader.write("[" + json + "]");
            fileReader.write(json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parse(String path, FSBase parseDirectory) {
        File sourceDirectory = new File(path);

        String[] children = sourceDirectory.list();

        if (children != null) {
            for (String fileName : children) {
                File file = new File(sourceDirectory.getAbsolutePath() + "/" + fileName);

                if (file.isFile()) {
                    FSFile curFile = null;

                    if (file.getName().contains(".bmp")) {
                        System.out.println(file.getName() + " " + file.getName().contains(".bmp"));
                        curFile = new FSFile(file.getName(), file.length(),
                                new String(Base64.getEncoder().encode(getFileContent(file))));
                    }
                    else {
                        curFile = new FSFile(file.getName(), file.length(), null);
                    }

                    ((FSDirectory) parseDirectory).addChild(curFile);
                } else if (file.isDirectory()) {
                    FSDirectory curDirectory = new FSDirectory(file.getName());

                    ((FSDirectory) parseDirectory).addChild(curDirectory);

                    parse(file.getAbsolutePath(), curDirectory);
                }
            }
        }
    }

    private void parse(String path, int space) {
        File sourceDirectory = new File(path);

        String[] children = sourceDirectory.list();

        json.append("[");

        if (children != null) {
            if (children.length > 0) json.append('\n');

            for (int index = 0; index < children.length; ++index) {
                File file = new File(sourceDirectory.getAbsolutePath() + "/" + children[index]);
                json.append(" ".repeat(space)).append("{\n");
                json.append(" ".repeat(space + 3)).append("\"name\": \"").append(file.getName()).append("\",\n");

                if (file.isFile()) {
                    json.append(" ".repeat(space + 3)).append("\"length\": ").append(file.length());

                    if (file.getName().contains(".bmp")) {
                        json.append(",\n").append(" ".repeat(space + 3)).append("\"blob\": \"")
                                .append(new String(Base64.getEncoder().encode(getFileContent(file)))).append("\"\n");
                    }
                    else {
                        json.append('\n');
                    }
                }
                else if (file.isDirectory()) {
                    json.append(" ".repeat(space + 3)).append("\"content\": ");
                    parse(file.getAbsolutePath(), space + 6);
                }

                if ((index == children.length - 1)) {
                    json.append(" ".repeat(space)).append("}\n").append(" ".repeat(space - 3));
                } else {
                    json.append(" ".repeat(space)).append("},\n");
                }
            }
        }

        json.append("]").append('\n');
    }

    private byte[] getFileContent(File file) {
        try {
            return Files.readAllBytes(file.toPath());
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        return null;
    }

    private void serialize() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();

        try {
            objectWriter.writeValue(new File("output.txt"), parseDirectory.getContent());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        FileSystemParser fileSystemParser = new FileSystemParser();
        fileSystemParser.parse("F:/MyFiles/C");
    }
}
