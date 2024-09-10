package com.fixdecode.sbfileuploaddownload.web.controller;

import com.fixdecode.sbfileuploaddownload.entities.FileSimple;
import com.fixdecode.sbfileuploaddownload.service.interfaces.IFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(path = "/manage-file-in-folder")
@RequiredArgsConstructor
public class FileController {

    private final IFileService iFileService;

    @PostMapping(path = "/upload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    ResponseEntity<List<FileSimple>> upload(@RequestPart("file") List<MultipartFile> files) throws Exception {
        return iFileService.uploadFile(files);
    }

    @GetMapping(path = "/download/{identifiantFile}")
    ResponseEntity<Resource>  download(@PathVariable Long identifiantFile) throws IOException {
        return iFileService.downloadFile(identifiantFile);
    }

    @GetMapping(path = "/conversion-image-to-pdf/{identity}")
    ResponseEntity<Object> conversionImageToPdf(@PathVariable("identity") Long identifiantUnique) throws Exception {
        return iFileService.formatPictureToPdf(identifiantUnique);
    }

}
