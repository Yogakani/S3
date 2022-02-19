package com.yoga.s3.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
@Slf4j
@NoArgsConstructor
public class FileUploadDownloadService implements FileService {

    @Autowired
    private AmazonS3Client amazonS3Client;

    @Value("${s3.bucket}")
    private String bucket;

    @Override
    public boolean uploadFile(MultipartFile file) throws IOException {
        File uploadFile = getFileFromMultipart(file);
        uploadToS3(uploadFile);
        uploadFile.delete();
        return true;
    }

    @Override
    public boolean downloadFile(String fileName) throws IOException {
        S3ObjectInputStream objectInputStream = downloadFromS3(fileName);
        log.info("File Downloaded from S3 bucket : {}", bucket + "/" + fileName);
        getFileFromS3Object(objectInputStream,fileName);
        return true;
    }

    private File getFileFromMultipart(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fileOutputStream = new FileOutputStream(convFile);
        fileOutputStream.write(file.getBytes());
        fileOutputStream.close();
        return convFile;
    }

    private void uploadToS3(File file) {
        amazonS3Client.putObject(new PutObjectRequest(bucket, file.getName(), file)
                .withCannedAcl(CannedAccessControlList.Private));
        log.info("File Uploaded Successfully into S3 bucket : {}", bucket + "/" + file.getName());
    }

    private S3ObjectInputStream downloadFromS3(String fileName) {
         return amazonS3Client.getObject(new GetObjectRequest(bucket, fileName)).getObjectContent();
    }

    private void getFileFromS3Object(S3ObjectInputStream objectInputStream, String fileName) throws IOException {
        byte[] content = IOUtils.toByteArray(objectInputStream);
        objectInputStream.close();
        FileOutputStream fos = new FileOutputStream(fileName);
        fos.write(content);
        fos.close();
    }
}

