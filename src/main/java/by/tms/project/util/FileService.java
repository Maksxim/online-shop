package by.tms.project.util;

import by.tms.project.exception.FileSizeException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileService {

    public String saveImage(MultipartFile file, String uploadDirectory) throws IOException {
        if(file.getSize() > (1024 * 1024 * 1)){
            throw new FileSizeException("file is too big");
        }
        // логика сохранения файла на диск
        StringBuilder fileNames = new StringBuilder();
        Path fileNameAndPath = Paths.get(uploadDirectory, file.getOriginalFilename());
        fileNames.append(file.getOriginalFilename());
        Files.write(fileNameAndPath, file.getBytes());
        // метод возвращает return  путь к файлу
        return fileNameAndPath.toString();
    }
}
