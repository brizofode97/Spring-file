package com.fixdecode.sbfileuploaddownload.service.implement;

import com.fixdecode.sbfileuploaddownload.common.utils.IdentifiantUnique;
import com.fixdecode.sbfileuploaddownload.entities.FileSimple;
import com.fixdecode.sbfileuploaddownload.repositories.IFileRepository;
import com.fixdecode.sbfileuploaddownload.service.interfaces.IFileService;
import com.fixdecode.sbfileuploaddownload.web.dto.response.FileResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.nio.file.Files.copy;
import static java.nio.file.Paths.get;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;


@Slf4j
@Service
@RequiredArgsConstructor
public class FileServiceImpl implements IFileService {

    public static final String DIRECTORY = System.getProperty("user.home") + "/storage/Downloads/uploads";

    private final IFileRepository iFileRepository;


    @Override
    public ResponseEntity<List<FileSimple>> uploadFile(List<MultipartFile >files) throws Exception {
        List<FileSimple> fileList = new ArrayList<>();
        List<FileSimple> fileListResponse = new ArrayList<>();

        Long identifiantUnique = IdentifiantUnique.setIdByTime();

        for(MultipartFile file : files){
            String fileName = StringUtils.cleanPath(file.getOriginalFilename() != null ? file.getOriginalFilename() : "");

            if (fileName.isEmpty() || fileName.contains("..")) {
                return ResponseEntity
                        .badRequest()
                        .body(null);
            }

            String extension = fileName.substring(fileName.lastIndexOf("."));
            Path fileStorage = get(DIRECTORY, fileName).toAbsolutePath().normalize();
            if(Files.notExists(fileStorage)){
//                java.io.File file2 = new java.io.File(DIRECTORY);
//                file2.mkdirs();
                Files.createDirectories(fileStorage);
            }
            copy(file.getInputStream(), fileStorage, REPLACE_EXISTING);
            FileSimple fileAdd = new FileSimple(identifiantUnique, fileName, fileStorage.toString(), file.getContentType(), file.getSize(), extension);
            fileList.add(fileAdd);
        }
        try{
            fileListResponse = iFileRepository.saveAll(fileList);
        }catch(DataIntegrityViolationException dataIntegrityViolationException){
            for(int i = 1; i < fileList.size(); i++ ){
                fileList.get(i).setIdentifiantUnique(fileList.get(i - 1).getIdentifiantUnique() + 1L);
            }
            fileListResponse = iFileRepository.saveAll(fileList);
        }
        return ResponseEntity
                .ok()
                .body(fileListResponse);
    }

    @Override
    public ResponseEntity<Resource> downloadFile(Long identifiantUnique) throws IOException {

        Optional<FileSimple> fileOptional = iFileRepository.findByIdentifiantUnique(identifiantUnique);
        if (fileOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

//        File fileFind = fileOptional.get();
        Path path = Paths.get(DIRECTORY).toAbsolutePath().normalize().resolve(fileOptional.get().getFilename());
        if (Files.notExists(path)) {
            return ResponseEntity.notFound().build();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("File-Name", fileOptional.get().getFilename());
        headers.add(CONTENT_DISPOSITION, "attachment; File-Name = " + fileOptional.get().getFilename());

        Resource resource;
        try {
//            InputStreamResource inputStreamResource = new InputStreamResource(Files.newInputStream(path));
            resource = new UrlResource(path.toUri());
        } catch (MalformedURLException malformedURLException) {
            return ResponseEntity.noContent().build();
        }



        FileResponse fileResponse = new FileResponse(
                fileOptional.get().getFilename(),
                resource.getURL(),
                fileOptional.get().getExtension(),
                fileOptional.get().getSize());

        return ResponseEntity.ok()
                .headers(headers)
                .contentType((MediaType.parseMediaType(Files.probeContentType(path))))
                .body(resource);
    }

    @Override
    public ResponseEntity<Object> formatPictureToPdf(Long identifiantUnique) throws Exception {
        Optional<FileSimple> fileOptional = iFileRepository.findByIdentifiantUnique(identifiantUnique);
        if (fileOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        String pdfFileStorage = "C:/Users/User/storage/pdfFile";
        Path path = Paths.get(pdfFileStorage).toAbsolutePath().normalize();
        if(Files.notExists(path)){
            Files.createDirectories(path);
        }
        int dotIndex = fileOptional.get().getFilename().indexOf(".");
        String nameFile = fileOptional.get().getFilename().substring(0, dotIndex) + ".pdf";
        pdfFileStorage += "/" + nameFile;

        try{
            //Creation d'un objet document
            PDDocument document = new PDDocument();

            //Creation d'un objet page et l'ajouter au niveau du document
            PDPage page = new PDPage();
            document.addPage(page);

            //Charger l'image au niveau du document
            PDImageXObject pdImage = PDImageXObject.createFromFile(fileOptional.get().getPath(), document);

            //Obtenir un objet pour écrire du contenu sur la page
            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            contentStream.drawImage(pdImage, 0, 0, page.getMediaBox().getWidth(), page.getMediaBox().getHeight());
            contentStream.close();

            log.info("Voici le dossier de stockage" + pdfFileStorage);
            document.save(pdfFileStorage);

        }catch (Exception e){
            throw new Exception("Un probleme est rencontré : "+ e.getMessage());
        }

        return ResponseEntity
                .ok()
                .body("Le fichier est bien transformé en PDF");
    }




}
