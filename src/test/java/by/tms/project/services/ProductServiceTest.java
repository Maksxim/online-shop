package by.tms.project.services;

import by.tms.project.dto.ProductDto;
import by.tms.project.entities.Product;
import by.tms.project.exception.NotFoundException;
import by.tms.project.repositories.ProductRepository;
import by.tms.project.util.FileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ProductServiceTest {

    private ProductService productService;

    private ProductRepository productRepository;

    private FileService fileService;

    @BeforeEach
    public void before() {
        productRepository = Mockito.mock(ProductRepository.class);
        fileService = Mockito.mock(FileService.class);
        productService = new ProductService(productRepository, fileService);
        productService.setUploadDirectory("/some/path");
    }

    @Test
    public void getProduct_Success(){
        Product product = new Product(10, "iphone", "description", 100.0, "imagePath");
        Mockito.when(productRepository.findById(10)).thenReturn(Optional.of(product));

        Product actualProduct = productService.getProduct(10);
        assertEquals(10, actualProduct.getId());
        assertEquals("iphone", actualProduct.getProductName());
        assertEquals("description", actualProduct.getDescription());
        assertEquals(100.0, actualProduct.getPrice());
        assertEquals("imagePath", actualProduct.getImagePath());
    }

    @Test
    public void getProduct_NotFound() {
        Mockito.when(productRepository.findById(9999)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> productService.getProduct(9999));
    }

    @Test
    public void createProduct_Success() throws IOException {
        Product product = new Product(10,"name","description",300,"/some/path/image.jpg");
        Mockito.when(productRepository.save(product)).thenReturn(product);
        MultipartFile file = Mockito.mock(MultipartFile.class);
        Mockito.when(fileService.saveImage(file, "/some/path")).thenReturn("/some/path/image.jpg");

        Product actualProduct = productService.createProduct(product, file);

        assertEquals(10, actualProduct.getId());
        assertEquals("name", actualProduct.getProductName());
        assertEquals("description", actualProduct.getDescription());
        assertEquals(300, actualProduct.getPrice());
        assertEquals("/some/path/image.jpg", actualProduct.getImagePath());
    }

    @Test
    public void updateProduct_Success() throws IOException {
        ProductDto dto = new ProductDto(10,"name","description",300);
        MultipartFile file = Mockito.mock(MultipartFile.class);
        Product existingProduct = new Product(10, "name", "description", 300, "/someother/path/image.jpg");
        Mockito.when(productRepository.findById(10)).thenReturn(Optional.of(existingProduct));
        Mockito.when(fileService.saveImage(file, "/some/path")).thenReturn("/some/path/filename.jpg");
        Mockito.when(productRepository.save(any())).thenReturn(existingProduct);

        productService.updateProduct(dto, file);

        verify(productRepository, times(1)).findById(10);
        verify(fileService, only()).saveImage(file, "/some/path");
        ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
        verify(productRepository, times(1)).save(captor.capture());

        Product product = captor.getValue();
        assertEquals("/some/path/filename.jpg", product.getImagePath());
    }

    @Test
    public void delete_Success(){
        Product product = new Product(10,"name","description",300,"d");
        productService.delete(product.getId());

        Mockito.verify(productRepository, only()).deleteById(product.getId());
    }
}
