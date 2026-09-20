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
    // Sắp xếp giá từ thấp đến cao
    List<Product> findAllByOrderByUnitPriceAsc();
    
    // Lấy sản phẩm theo mã Category
    List<Product> findByCategoryCategoryId(Long categoryId);
    
    // Tìm kiếm theo tên
    List<Product> findByProductNameContaining(String name);
    
    
    // 2 hàm còn thiếu gây ra lỗi:
    Optional<Product> findByProductName(String name);
    Optional<Product> findByCreateDate(Date createDate);
    
 // Thêm hàm tìm kiếm theo tên có hỗ trợ phân trang
    Page<Product> findByProductNameContaining(String name, Pageable pageable);
    
    Page<Product> findByCategoryCategoryId(Long categoryId, Pageable pageable);
}
