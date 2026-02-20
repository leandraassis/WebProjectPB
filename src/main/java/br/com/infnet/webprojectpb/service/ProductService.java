package br.com.infnet.webprojectpb.service;

import br.com.infnet.webprojectpb.entity.Product;
import br.com.infnet.webprojectpb.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ProductService {

    private ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> listProductsDb() {
        return productRepository.findAll();
    }

    public Product consultProductDb(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Product not found: " + id));
    }

    public Product insertProductDb(Product newProduct) {
        validateProduct(newProduct);
        return productRepository.save(newProduct);
    }

    public void deleteProductDb(Long id) {
        if(!productRepository.existsById(id)) {
            throw new NoSuchElementException("Product not found to delete: " + id);
        }
        productRepository.deleteById(id);
    }

    public Product updateProductDb(Long id, Product updatedProduct) {
        Product productToUpdate = productRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Product not found: " + id));

        validateProduct(updatedProduct);

        productToUpdate.setName(updatedProduct.getName());
        productToUpdate.setPrice(updatedProduct.getPrice());
        productToUpdate.setQuantity(updatedProduct.getQuantity());
        return productRepository.save(productToUpdate);
    }

    protected void validateProduct(Product product) {
        if (product.getName() == null || product.getName().length() < 3 || product.getName().length() > 50) {
            throw new IllegalArgumentException("The product name needs to be between 3 and 50 characters");
        }

        if (product.getPrice() == null || product.getPrice() <= 0) {
            throw new IllegalArgumentException("The price of the product must be greater than zero");
        }

        if (product.getQuantity() == null || product.getQuantity() < 0) {
            throw new IllegalArgumentException("The quantity of the product cannot be negative");
        }
    }

}

