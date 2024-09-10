package com.fixdecode.sbfileuploaddownload.web.controller;

import com.fixdecode.sbfileuploaddownload.service.interfaces.IFilePdfService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/filePdf")
@RequiredArgsConstructor
public class FilePdfController {

    private final IFilePdfService filePdfService;

    @GetMapping(path = "/conversion-image-to-pdf/{identifiant}")
    public ResponseEntity<Object> conversionImageToPdf(@PathVariable("identifiant") Long identifiant) throws Exception {
        return filePdfService.saveFilePdf(identifiant);
    }

    @GetMapping(path = "/contenu-extrait/{identifiantPdf}")
    public ResponseEntity<String> contenuExtrait(@PathVariable("identifiantPdf") Long identifiant) throws Exception {
        return filePdfService.extraireInformation(identifiant);
    }

}
