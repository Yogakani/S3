package com.yoga.s3.controller;

import com.yoga.s3.service.FileService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1.0/file")
@AllArgsConstructor
public class FileUploadDownloadController {

    private FileService fileService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        fileService.uploadFile(file);
        return new ResponseEntity<>("File Uploaded", HttpStatus.OK);
    }

    @GetMapping("/download")
    public ResponseEntity<?> downloadFile(@RequestParam("filename") String fileName) throws IOException {
        fileService.downloadFile(fileName);
        return new ResponseEntity<>("File Downloaded", HttpStatus.OK);
    }
}
