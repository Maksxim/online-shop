package by.tms.project.services;

import by.tms.project.entities.Product;
import by.tms.project.exception.NotFoundException;
import by.tms.project.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;

public class ProductServiceTest {

    private ProductService productService;

    private ProductRepository productRepository;

    @BeforeEach
    public void before() {
        productRepository = Mockito.mock(ProductRepository.class);
        productService = new ProductService(productRepository);
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
    public void createProduct_Success(){
        Product product = new Product(10,"name","description",300,"d");
        Mockito.when(productRepository.save(product)).thenReturn(product);
        Product actualProduct = productService.createProduct(product);

        assertEquals(10, actualProduct.getId());
        assertEquals("name", actualProduct.getProductName());
        assertEquals("description", actualProduct.getDescription());
        assertEquals(300, actualProduct.getPrice());
        assertEquals("d", actualProduct.getImagePath());
    }

    @Test
    public void updateProduct_Success(){
        Product product = new Product(10,"name","description",300,"d");
        Mockito.when(productRepository.save(product)).thenReturn(product);
        productService.updateProduct(product);

        verify(productRepository, only()).save(product);
    }

    @Test
    public void delete_Success(){
        Product product = new Product(10,"name","description",300,"d");
        productService.delete(product.getId());

        Mockito.verify(productRepository, only()).deleteById(product.getId());
    }
}
