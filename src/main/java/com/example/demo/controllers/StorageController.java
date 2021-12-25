package com.example.demo.controllers;


import com.google.cloud.storage.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Files;
import java.nio.file.Paths;

import static java.nio.charset.StandardCharsets.UTF_8;

@RestController
@Component
@RequestMapping("/gcp")
public class StorageController {

    private final Storage storage = StorageOptions.getDefaultInstance().getService();
    @Value("my-file.txt")
    private String fileName;
    @Value("${gcs-resource-test-bucket}")
    private String bucketName;

    @PostMapping("/create-file")
    public void createFile() throws IOException {
        BlobId blobId = BlobId.of(bucketName, fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
        storage.create(blobInfo, Files.readAllBytes(Paths.get("C:\\Users\\nidhi\\OneDrive\\Desktop\\my-file.txt")));
    }

    @GetMapping("/read-file")
    public String readFile() throws IOException {
        BlobId blobId = BlobId.of(bucketName, fileName);
        Blob blob = storage.get(blobId);
        return new String(blob.getContent());
    }

    @PutMapping("/update-file")
    public void updateFile(@RequestBody String data) throws IOException {
        BlobId blobId = BlobId.of(bucketName, fileName);
        Blob blob = storage.get(blobId);
        WritableByteChannel channel = blob.writer();
        channel.write(ByteBuffer.wrap(data.getBytes(UTF_8)));
        channel.close();
    }

    @DeleteMapping("/delete-file")
    public void deleteFile() throws IOException {
        Storage storage = StorageOptions.getDefaultInstance().getService();
        BlobId blobId = BlobId.of(bucketName, fileName);
        storage.delete(blobId);
    }
}