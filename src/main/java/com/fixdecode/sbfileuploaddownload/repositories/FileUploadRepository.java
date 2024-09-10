package com.fixdecode.sbfileuploaddownload.repositories;

import com.fixdecode.sbfileuploaddownload.entities.FileUpload;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileUploadRepository extends JpaRepository<FileUpload, String> {
}
