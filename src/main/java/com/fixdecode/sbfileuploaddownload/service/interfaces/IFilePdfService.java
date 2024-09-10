package com.fixdecode.sbfileuploaddownload.service.interfaces;

import org.springframework.http.ResponseEntity;

import java.io.IOException;

public interface IFilePdfService {

    ResponseEntity<Object> saveFilePdf(Long identifiantUniqueFile) throws Exception;
    ResponseEntity<String> extraireInformation(Long identifiantFilePdf) throws Exception;

}
