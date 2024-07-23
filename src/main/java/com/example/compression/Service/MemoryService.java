package com.example.compression.Service;

import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

@Service
public class MemoryService {

    HashMap<String, byte[]> dataMap = new HashMap<>();

    public String setMemCompress(String key, String value) {
        String response = "";
        try {
            String fileName = key + ".txt";
            File file = new File(fileName);
            if(file.exists()){
                return "key already exists use another key name";
            }
            // compress the value to byte[]
            byte[] compressedData = compressString(value);

            // storing the data into the dataMap
            dataMap.put(key, compressedData);

            // store the compressed data into the file
            try (FileOutputStream fos = new FileOutputStream(fileName)) {
                fos.write(compressedData);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Bytes written to file successfully";
    }

    public String getMemCompress(String key){
        String response="";
        try {
            if (!dataMap.containsKey(key)) {
                String fileName = key + ".txt";
                File file = new File(fileName);
                String filePath = file.getPath();
                if (!file.exists()) {
                    return "Key may be wrong or does not exists";
                }
//                String filePathStr = filePath.toAbsolutePath().toString();
                byte[] decompressData = readBytesFromFile(filePath);
                response = decompressString(decompressData);
            } else {
                response = decompressString(dataMap.get(key));
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return response;
    }

    private byte[] compressString(String inputString) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (GZIPOutputStream gzipOutputStream = new GZIPOutputStream(outputStream)) {
            gzipOutputStream.write(inputString.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputStream.toByteArray();
    }

    private String decompressString(byte[] compressedData) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(compressedData);
             GZIPInputStream gzipInputStream = new GZIPInputStream(inputStream)) {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = gzipInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }
        }
        return outputStream.toString(StandardCharsets.UTF_8);
    }
    private String readFileToString(String filePath) {
        StringBuilder contentBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                contentBuilder.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return contentBuilder.toString();
    }

    private static byte[] readBytesFromFile(String filePath) {
        byte[] bytes = null;
        try {
            Path path = Paths.get(filePath);
            bytes = Files.readAllBytes(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }

}
