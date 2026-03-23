package br.com.infnet.webprojectpb.service;

import br.com.infnet.webprojectpb.entity.Product;
import br.com.infnet.webprojectpb.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProductServiceTest {

    private ProductRepository productRepository;
    private ProductService productService;

    Product product1;
    Product product2;

    @BeforeEach
    void setUp() {
        productRepository = Mockito.mock(ProductRepository.class);
        productService = new ProductService(productRepository);

        product1 = new Product(1L, "Meias", 20.0, 100);
        product2 = new Product(2L, "Casaco", 120.0, 90);
    }


    //consultProductsDb
    @Test
    void testConsultProductsDb_ReturnsList() {
        when(productRepository.findAll()).thenReturn(List.of(product1, product2));
        List<Product> result = productService.listProductsDb();
        assertEquals(2, result.size());
        verify(productRepository).findAll();
    }

    @Test
    void testConsultProductsDb_ReturnEmptyList() {
        when(productRepository.findAll()).thenReturn(List.of());
        List<Product> result = productService.listProductsDb();
        assertEquals(0, result.size());
        verify(productRepository).findAll();
    }

    //consultProductDb
    @Test
    void testConsultProductDb_ReturnsProduct_WhenExists() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product1));

        Product result = productService.consultProductDb(1L);

        assertNotNull(result);
        assertEquals("Meias", result.getName());
        assertEquals(20.0, result.getPrice());
        verify(productRepository).findById(1L);
    }

    @Test
    void testConsultProductDb_ThrowsException_WhenNotFound() {
        when(productRepository.findById(12L)).thenReturn(Optional.empty());

        NoSuchElementException e = assertThrows(NoSuchElementException.class,
                () -> productService.consultProductDb(12L));

        assertEquals("Product not found: 12", e.getMessage());
        verify(productRepository).findById(12L);
    }

    //InsertDb
    @Test
    void testInsertProductDb_Success() {
        productService.insertProductDb(product1);

        verify(productRepository, times(1)).save(product1);
    }

    @Test
    void testInsertProductDb_ThrowsException_WhenNameTooShort() {
        Product invalidProduct = new Product(3L, "Oe", 20.0, 2);

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> productService.insertProductDb(invalidProduct)
        );

        assertEquals("The product name needs to be between 3 and 50 characters", e.getMessage());
        verify(productRepository, never()).save(any());
    }

    @Test
    void testInsertProductDb_ThrowsException_WhenNameTooLong() {
        Product invalidProduct = new Product(4L, "A".repeat(60), 300.0, 3);

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> productService.insertProductDb(invalidProduct)
        );

        assertEquals("The product name needs to be between 3 and 50 characters", e.getMessage());
        verify(productRepository, never()).save(any());
    }

    @Test
    void testInsertProductDb_ThrowsException_WhenPriceIsZeroOrNegative() {
        Product invalidProduct = new Product(5L, "Olheira", 0.0, 10);

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> productService.insertProductDb(invalidProduct)
        );

        assertEquals("The price of the product must be greater than zero", e.getMessage());
        verify(productRepository, never()).save(any());
    }

    @Test
    void testInsertProductDb_ThrowsException_WhenQuantityIsNegative() {
        Product invalidProduct = new Product(6L, "Pochete", 100.0, -1);

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> productService.insertProductDb(invalidProduct)
        );

        assertEquals("The quantity of the product cannot be negative", e.getMessage());
        verify(productRepository, never()).save(any());
    }

    //validate product
    @Test
    void testInsertProductDb_ThrowsException_WhenNameIsNull() {
        Product invalidProduct = new Product(7L, null, 10.0, 1);

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> productService.insertProductDb(invalidProduct)
        );

        assertEquals("The product name needs to be between 3 and 50 characters", e.getMessage());
    }

    @Test
    void testInsertProductDb_ThrowsException_WhenPriceIsNull() {
        Product invalidProduct = new Product(8L, "Produto", null, 1);

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> productService.insertProductDb(invalidProduct)
        );

        assertEquals("The price of the product must be greater than zero", e.getMessage());
    }

    @Test
    void testInsertProductDb_ThrowsException_WhenQuantityIsNull() {
        Product invalidProduct = new Product(9L, "Produto", 10.0, null);

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> productService.insertProductDb(invalidProduct)
        );

        assertEquals("The quantity of the product cannot be negative", e.getMessage());
    }

    //deleteDb
    @Test
    void testDeleteProductDb_Success() {
        when(productRepository.existsById(1L)).thenReturn(true);

        productService.deleteProductDb(product1.getId());

        verify(productRepository, times(1)).existsById(1L);
        verify(productRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteProductDb_ThrowsException_WhenNotFound() {
        when(productRepository.existsById(1L)).thenReturn(false);

        NoSuchElementException e = assertThrows(NoSuchElementException.class,
                () -> productService.deleteProductDb(product1.getId())
        );
        assertEquals("Product not found to delete: 1", e.getMessage());
        verify(productRepository, never()).deleteById(anyLong());
    }

    //updateDb
    @Test
    void testUpdateProductDb_Success() {

        Product originalProduct = new Product(3L, "Meia calça", 60.0, 7);
        Product updatedProduct = new Product(3L, "Tapa olho", 25.0, 30);

        when(productRepository.findById(3L)).thenReturn(Optional.of(originalProduct));

        productService.updateProductDb(updatedProduct.getId(), updatedProduct);

        assertEquals("Tapa olho", originalProduct.getName());
        assertEquals(25.0, originalProduct.getPrice());
        assertEquals(30, originalProduct.getQuantity());

        verify(productRepository).save(originalProduct);
    }

    @Test
    void testUpdateProductDb_ProductNotFound() {
        when(productRepository.findById(13L)).thenReturn(Optional.empty());

        Product notExistingProduct = new Product(13L, "Inexistente", 10.0, 5);

        NoSuchElementException e = assertThrows(NoSuchElementException.class,
                () -> productService.updateProductDb(notExistingProduct.getId(), notExistingProduct)
        );

        assertEquals("Product not found: 13", e.getMessage());

        verify(productRepository, never()).save(any());
    }

}

