package com.fixdecode.sbfileuploaddownload.repositories;

import com.fixdecode.sbfileuploaddownload.entities.FileSimple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IFileRepository extends JpaRepository<FileSimple,Long> {

    Optional<FileSimple> findByIdentifiantUnique(Long identifiantUnique);
}
