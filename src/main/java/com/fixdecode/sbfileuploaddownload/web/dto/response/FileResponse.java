package com.fixdecode.sbfileuploaddownload.web.dto.response;

public record FileResponse(
        String fileName,
        java.net.URL urlDownlaod,
        String extension,
        Long fileSize
) {
}
