package br.com.infnet.webprojectpb.repository;

import br.com.infnet.webprojectpb.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
