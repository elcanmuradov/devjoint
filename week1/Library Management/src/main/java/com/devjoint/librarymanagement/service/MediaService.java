package com.devjoint.librarymanagement.service;

import com.devjoint.librarymanagement.exception.FileException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.text.html.Option;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class MediaService {

    private final Path uploadDir = Paths.get("./uploads");

    @PostConstruct
    public void init() throws IOException {
            Files.createDirectories(uploadDir);
    }

    public String save(MultipartFile file) {
        try {
            String fileName = UUID.randomUUID().toString() + file.getOriginalFilename();
            Path path = uploadDir.resolve(fileName).normalize();

            Files.copy(file.getInputStream(),path, StandardCopyOption.REPLACE_EXISTING);
            return path.toString();
        }catch (IOException e) {
            throw new FileException(e.getMessage());
        }
    }

    public Resource download(String fileName){
        try {


            File file = uploadDir.resolve(fileName).toFile();

            Path path = Path.of(file.getAbsoluteFile().toURI());
            if (!Files.exists(path)) {
                throw new RuntimeException("Fayl diskdə tapılmadı");
            }

            return new UrlResource(path.toUri());
        }catch (MalformedURLException e) {
            throw new FileException(e.getMessage());
        }
    }

}
