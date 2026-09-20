package vn.iotstar.service;

import java.util.List;
import java.util.Optional;
import vn.iotstar.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ICategoryService {
    List<Category> findAll();
    Optional<Category> findById(Long id);
    Category save(Category category);
    void delete(Long id);
    void delete(Category entity);
    Page<Category> findAll(Pageable pageable);
    Page<Category> findByCategoryNameContaining(String name, Pageable pageable);
}