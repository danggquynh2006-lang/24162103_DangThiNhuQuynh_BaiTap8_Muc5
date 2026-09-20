package vn.iotstar.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable; // Thêm thư viện Pageable
import vn.iotstar.entity.Product;

public interface IProductService {
    List<Product> findAll();
    Page<Product> findAll(Pageable pageable);
    Optional<Product> findById(Long id);
    Product save(Product product);
    void delete(Long id);
    List<Product> findByProductNameContaining(String name);
    Page<Product> findByProductNameContaining(String name, Pageable pageable);
    Optional<Product> findByProductName(String name);
    Optional<Product> findByCreateDate(Date createDate);
    List<Product> findAllByOrderByUnitPriceAsc();
    List<Product> findByCategoryCategoryId(Long categoryId);
    Page<Product> findByCategoryCategoryId(Long categoryId, Pageable pageable);
}