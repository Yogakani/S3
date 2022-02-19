package com.yoga.s3.service;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
public interface FileService {

    boolean uploadFile(MultipartFile file) throws IOException;

    boolean downloadFile(String fileName) throws IOException;
}
