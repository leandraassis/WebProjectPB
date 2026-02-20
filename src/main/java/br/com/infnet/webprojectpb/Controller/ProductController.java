package br.com.infnet.webprojectpb.Controller;

import br.com.infnet.webprojectpb.entity.Product;
import br.com.infnet.webprojectpb.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@CrossOrigin
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping
    public List<Product> list() {
        return service.listProductsDb();
    }

    @GetMapping("/{id}")
    public Product search(@PathVariable Long id) {
        return service.consultProductDb(id);
    }

    @PostMapping
    public Product insert(@RequestBody Product product) {
        return service.insertProductDb(product);
    }

    @PutMapping("/{id}")
    public Product updater(@PathVariable Long id, @RequestBody Product product) {
        return service.updateProductDb(id, product);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteProductDb(id);
    }
}

