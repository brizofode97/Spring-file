package com.fixdecode.sbfileuploaddownload.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "file")
public class FileSimple {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="Id")
    private Long id;

    @Column(name = "identifiant_unique", unique = true)
    private Long identifiantUnique;

    @Column(name="nom_fichier")
    private String filename;

    @Column(name="url_fichier")
    private String path;

    @Column(name="type_fichier")
    private String type;

    @Column(name="taille_fichier")
    private Long size;

    @Column(name="extension_fichier")
    private String extension;

    public FileSimple(Long identifiantUnique, String filename, String path, String type, Long size, String extension) {
        this.identifiantUnique = identifiantUnique;
        this.filename = filename;
        this.path = path;
        this.type = type;
        this.size = size;
        this.extension = extension;
    }
}
