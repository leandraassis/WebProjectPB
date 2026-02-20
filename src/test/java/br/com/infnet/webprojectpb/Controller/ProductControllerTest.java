package br.com.infnet.webprojectpb.Controller;

import br.com.infnet.webprojectpb.entity.Product;
import br.com.infnet.webprojectpb.service.ProductService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;


@WebMvcTest(ProductController.class)
public class  ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService service;

    @Autowired
    private ObjectMapper objectMapper;

    Product product1;
    Product product2;

    @BeforeEach
    void setUp() {
        product1 = new Product(1L, "Meias", 20.0, 100);
        product2 = new Product(2L, "Casaco", 120.0, 90);
    }

    @Test
    void getProducts_ReturnsJsonList() throws Exception{
        when(service.listProductsDb()).thenReturn(List.of(product1, product2));

        mockMvc.perform(get("/products").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Meias"))
                .andExpect(jsonPath("$[1].name").value("Casaco"));
    }

    @Test
    void getProductById_ReturnsProduct() throws Exception {
        when(service.consultProductDb(1L)).thenReturn(product1);

        mockMvc.perform(get("/products/1")).andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Meias"));
    }

    @Test
    void postProduct_ReturnCreatedProduct() throws Exception {
        Product productUnsaved = new Product(null, "Touca", 15.0, 50);
        Product saved = new Product(1L, "Touca", 15.0, 50);

        when(service.insertProductDb(any(Product.class))).thenReturn(saved);

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productUnsaved)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void updateProduct_ReturnUpdatedProduct() throws Exception {
        Product updated = new Product(1L, "Sapatos", 1898.0, 90);

        when(service.updateProductDb(eq(1L), any(Product.class)))
                .thenReturn(updated);

        mockMvc.perform(put("/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Sapatos"));
    }

    @Test
    void shouldDeleteProduct() throws Exception {
        mockMvc.perform(delete("/products/1")).andExpect(status().isOk());
        verify(service, times(1)).deleteProductDb(1L);
    }
}









