package com.fixdecode.sbfileuploaddownload.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileUploadResponse {

    private String fileName;
    private String fileType;
    private Long fileSize;
    private String downloadUrl;
}
