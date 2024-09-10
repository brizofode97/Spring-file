package com.fixdecode.sbfileuploaddownload.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "file_pdf")
@Data
@NoArgsConstructor
public class FilePdf {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "identifiant_unique")
    private Long identifiantUnique;

    @Column(name = "url_file")
    private String urlFile;

    @OneToOne
    @JoinColumn(name = "identite_file", referencedColumnName = "identifiant_unique")
    private FileSimple file;


    public FilePdf(String urlFile, FileSimple file, Long identifiantUnique) {
        this.urlFile = urlFile;
        this.file = file;
        this.identifiantUnique = identifiantUnique;
    }
}
