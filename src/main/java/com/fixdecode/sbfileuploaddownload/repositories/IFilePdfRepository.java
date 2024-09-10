package com.fixdecode.sbfileuploaddownload.repositories;

import com.fixdecode.sbfileuploaddownload.entities.FilePdf;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IFilePdfRepository extends JpaRepository<FilePdf, Long> {
    Optional<FilePdf> findByIdentifiantUnique(Long identifiantUnique);
}
