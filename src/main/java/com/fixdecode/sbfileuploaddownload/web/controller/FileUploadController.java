package com.fixdecode.sbfileuploaddownload.web.controller;

import com.fixdecode.sbfileuploaddownload.web.dto.FileUploadResponse;
import com.fixdecode.sbfileuploaddownload.service.implement.FileUploadService;
import com.fixdecode.sbfileuploaddownload.entities.FileUpload;
import lombok.AllArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@AllArgsConstructor
public class FileUploadController {

    private FileUploadService fileUploadService;

    @PostMapping(path = "/upload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public FileUploadResponse uploadFile(@RequestPart("file") MultipartFile file) throws Exception {
        FileUpload attachment = null;
        String download = "";
        attachment = fileUploadService.saveFile(file);
        download = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/download")
                .path(attachment.getId())
                .toUriString();

        return new FileUploadResponse(
                attachment.getFileName(),
                attachment.getFileType(),
                file.getSize(),
                download
        );

    }

    @GetMapping(path = "/download/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable("fileId") String fileId) throws Exception{
        FileUpload fileUpload = fileUploadService.downloadFile(fileId);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(fileUpload.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "fileUpload; fileName : \"" + fileUpload.getFileName() + "\"")
                .body(new ByteArrayResource(fileUpload.getData()));
    }

}
