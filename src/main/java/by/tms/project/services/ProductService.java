package by.tms.project.services;

import by.tms.project.Logger;
import by.tms.project.entities.Product;
import by.tms.project.exception.FileSizeException;
import by.tms.project.exception.NotFoundException;
import by.tms.project.repositories.ProductRepository;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private static org.apache.logging.log4j.Logger log = LogManager.getLogger(ProductService.class);

    public static String UPLOAD_DIRECTORY = "D:/uploads";

    private ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    @Logger
    public Product createProduct(Product product) {
        Product createdProduct = productRepository.save(product);
        return createdProduct;
    }

    public String saveImage(MultipartFile file) throws IOException {
        if(file.getSize() > (1024 * 1024 * 1)){
            throw new FileSizeException("file is too big");
        }
        // логика сохранения файла на диск
        StringBuilder fileNames = new StringBuilder();
        Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, file.getOriginalFilename());
        fileNames.append(file.getOriginalFilename());
        Files.write(fileNameAndPath, file.getBytes());
        // метод возвращает return  путь к файлу
        return fileNameAndPath.toString();
    }

    @Logger
    public void updateProduct(Product product){
        productRepository.save(product);
    }

    @Logger
    public Product getProduct(int productID){
        Optional <Product> product = productRepository.findById(productID);
        if(product.isEmpty()){
         throw new NotFoundException("product not found");
        }
        return product.get();
    }

    @Logger
    public void delete(int productId){

        productRepository.deleteById(productId);
    }
    @Logger
    public List<Product> getAll(){
        return productRepository.findAll();
    }
}
