package vn.iotstar.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.iotstar.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findAllByOrderByUnitPriceAsc();
    
    List<Product> findByCategoryCategoryId(Long categoryId);
    
    List<Product> findByProductNameContaining(String name);
    
    Optional<Product> findByProductName(String name);
    Optional<Product> findByCreateDate(Date createDate);
    
    Page<Product> findByProductNameContaining(String name, Pageable pageable);
    
    Page<Product> findByCategoryCategoryId(Long categoryId, Pageable pageable);
}
