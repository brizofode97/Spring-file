package com.fixdecode.sbfileuploaddownload.service.implement;

import com.fixdecode.sbfileuploaddownload.common.utils.CompressAndDecompressFile;
import com.fixdecode.sbfileuploaddownload.entities.FileUpload;
import com.fixdecode.sbfileuploaddownload.repositories.FileUploadRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

@Slf4j
@Service
@AllArgsConstructor
public class FileUploadService {

    private FileUploadRepository fileUploadRepository;

    public FileUpload saveFile(MultipartFile file) throws Exception {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
//        String extension = fileName.substring(fileName.lastIndexOf("."));
        if(fileName.contains("..")){
            throw new Exception("The file name is invalid" + fileName);
        }

        try{
            log.info("La taille du fichier sans compression : " + file.getSize());
            byte[] fileCompress = CompressAndDecompressFile.compressFile(file.getBytes());
            log.info("La taille du fichier avec compression : " + fileCompress.length);
            FileUpload fileUpload = new FileUpload(fileName, file.getContentType(), fileCompress);
            return fileUploadRepository.save(fileUpload);
        }catch (IOException ioException){
            throw new Exception("The file is not uploded" + ioException.getMessage());
        }
    }

    public FileUpload downloadFile(String fileId) throws Exception {
        return fileUploadRepository.findById(fileId)
                .orElseThrow(() -> new Exception("A file with id : " + fileId + "could not be found"));
    }

}
