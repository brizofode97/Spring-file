package com.fixdecode.sbfileuploaddownload.service.implement;

import com.fixdecode.sbfileuploaddownload.common.utils.IdentifiantUnique;
import com.fixdecode.sbfileuploaddownload.entities.FileSimple;
import com.fixdecode.sbfileuploaddownload.entities.FilePdf;
import com.fixdecode.sbfileuploaddownload.repositories.IFilePdfRepository;
import com.fixdecode.sbfileuploaddownload.repositories.IFileRepository;
import com.fixdecode.sbfileuploaddownload.service.interfaces.IFilePdfService;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FilePdfServiceImpl implements IFilePdfService {


    public static final String DIRECTORY_PDF = System.getProperty("user.home") + "/storage/pdfFile";

    private final IFilePdfRepository filePdfRepository;
    private final IFileRepository fileRepository;

    @Override
    public ResponseEntity<Object> saveFilePdf(Long identifiantUniqueFile) throws Exception {


        Optional<FileSimple> fileOptional = fileRepository.findByIdentifiantUnique(identifiantUniqueFile);
        if (fileOptional.isEmpty()) {
            return ResponseEntity
                    .ok()
                    .body("The file identity is not found : " + identifiantUniqueFile);
        }

        FileSimple file = fileOptional.get();
        Path fileStorage = Paths.get(DIRECTORY_PDF).toAbsolutePath().normalize();
        try{
            if (Files.notExists(fileStorage)) {
                Files.createDirectories(fileStorage);
            }

            int dot = file.getFilename().indexOf(".");
            String name = file.getFilename().substring(0, dot) + ".pdf";
            String stringFile = DIRECTORY_PDF + "/" + name;

            PDDocument document = new PDDocument();
            PDPage page = new PDPage();
            document.addPage(page);

            PDImageXObject pdImage = PDImageXObject.createFromFile(file.getPath(), document);


            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            contentStream.drawImage(pdImage, 0, 0, page.getMediaBox().getWidth(), page.getMediaBox().getHeight());
            contentStream.close();

            document.save(stringFile);
            document.close();


            FilePdf filePdf = new FilePdf(stringFile, file, IdentifiantUnique.setIdByTime());


            filePdfRepository.save(filePdf);


        } catch (IOException e) {
            throw new IOException("Cannot create directory OR  Cannot save file:" + e.getMessage());
        } catch (DataIntegrityViolationException dataIntegrityViolationException) {
            throw new DataIntegrityViolationException("Cette image avec nom fichier : "+ file.getFilename() + " a déjà un pdf enregistré : "
                    + dataIntegrityViolationException.getMessage());
        }

        return ResponseEntity
                .ok()
                .body("Votre fichier est transformé en pdf avec succés");
    }

    @Override
    public ResponseEntity<String> extraireInformation(Long identifiantFilePdf) throws Exception {

        String extrait;

        Optional<FilePdf> filePdfOptional = filePdfRepository.findByIdentifiantUnique(identifiantFilePdf);
        if(filePdfOptional.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        FilePdf filePdf = filePdfOptional.get();

        try{
            File file = new File(filePdf.getUrlFile());
            PDDocument document = Loader.loadPDF(file);

            PDFTextStripper stripper = new PDFTextStripper();
            extrait = stripper.getText(document);
            document.close();

        }catch (NullPointerException nullPointerException){
            throw new IOException("Probleme sur le chemin du fichier spécifié : " + nullPointerException.getMessage());
        }catch (IOException ioException){
            throw new IOException("File is not save" + ioException.getMessage());
        }


        return ResponseEntity
                .ok()
                .body("Le texte extrait est : " + extrait);
    }
}
