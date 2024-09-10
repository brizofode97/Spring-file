package com.fixdecode.sbfileuploaddownload.service.interfaces;

import com.fixdecode.sbfileuploaddownload.entities.FileSimple;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IFileService{
    ResponseEntity<List<FileSimple>> uploadFile(List<MultipartFile> files) throws Exception;
    ResponseEntity<Resource> downloadFile(Long identifiantUnique) throws IOException;
    ResponseEntity<Object> formatPictureToPdf(Long identifiantUnique) throws Exception;
}
