package by.tms.project.services;

import by.tms.project.Logger;
import by.tms.project.dto.ProductDto;
import by.tms.project.entities.Product;
import by.tms.project.exception.FileSizeException;
import by.tms.project.exception.NotFoundException;
import by.tms.project.repositories.ProductRepository;
import by.tms.project.util.FileService;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Value("${upload.directory}")
    private String uploadDirectory;

    private ProductRepository productRepository;

    private FileService fileService;

    public void setUploadDirectory(String uploadDirectory) {
        this.uploadDirectory = uploadDirectory;
    }

    @Autowired
    public ProductService(ProductRepository productRepository, FileService fileService){
        this.productRepository = productRepository;
        this.fileService = fileService;
    }

    @Logger
    public Product createProduct(Product product, MultipartFile file) throws IOException {
        if (file != null && !file.isEmpty()) {
            // вызвать метод сохранения файла
            String imagePath = fileService.saveImage(file, uploadDirectory);
            // добавить в обьект продукт значение пути файла
            product.setImagePath(imagePath);
        }
        return productRepository.save(product);
    }

    @Logger
    public void updateProduct(ProductDto dto, MultipartFile file) throws IOException {
        Product existingProduct = getProduct(dto.getId());
        existingProduct.setProductName(dto.getProductName());
        existingProduct.setDescription(dto.getDescription());
        existingProduct.setPrice(dto.getPrice());
        if(file != null && !file.isEmpty()) {
            String imagePath = fileService.saveImage(file, uploadDirectory);
            existingProduct.setImagePath(imagePath);
        }
        productRepository.save(existingProduct);
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
    public Page<Product> getAll(Pageable pageable){
        return productRepository.findAll(pageable);
    }
}
