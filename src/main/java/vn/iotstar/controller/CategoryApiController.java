package vn.iotstar.controller;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vn.iotstar.entity.Category;
import vn.iotstar.model.Response;
import vn.iotstar.service.ICategoryService;
import vn.iotstar.service.IStorageService;

@RestController
@RequestMapping(path = "/api/category")
public class CategoryApiController {

    @Autowired
    private ICategoryService categoryService;

    @Autowired
    IStorageService storageService;

    @GetMapping
    public ResponseEntity<?> getAllCategory() {
        return new ResponseEntity<Response>(new Response(true, "Thành công", categoryService.findAll()), HttpStatus.OK);
    }

    @GetMapping(path = "/paginated")
    public ResponseEntity<?> getCategoriesPaginated(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size,
            @RequestParam(value = "keyword", defaultValue = "") String keyword) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Category> categoryPage;
        
        if (keyword != null && !keyword.trim().isEmpty()) {
            categoryPage = categoryService.findByCategoryNameContaining(keyword, pageable);
        } else {
            categoryPage = categoryService.findAll(pageable);
        }
        
        return new ResponseEntity<Response>(new Response(true, "Thành công", categoryPage), HttpStatus.OK);
    }

    @PostMapping(path = "/addCategoryJson")
    public ResponseEntity<?> addCategoryJson(@RequestBody Category categoryInput) {
        Optional<Category> optCategory = categoryService.findAll().stream()
                .filter(c -> c.getCategoryName().equalsIgnoreCase(categoryInput.getCategoryName())).findFirst();
        
        if (optCategory.isPresent()) {
            return new ResponseEntity<Response>(new Response(false, "Thể loại đã tồn tại", null), HttpStatus.BAD_REQUEST);
        }
        
        Category category = new Category();
        category.setCategoryName(categoryInput.getCategoryName());
        category.setIcon(null);
        categoryService.save(category);
        
        return new ResponseEntity<Response>(new Response(true, "Thêm thành công", category), HttpStatus.OK);
    }
    
    @PutMapping(path = "/updateCategoryJson")
    public ResponseEntity<?> updateCategoryJson(@RequestBody Category categoryInput) {
        Optional<Category> optCategory = categoryService.findById(categoryInput.getCategoryId());
        if (optCategory.isEmpty()) {
            return new ResponseEntity<Response>(new Response(false, "Không tìm thấy thể loại", null), HttpStatus.NOT_FOUND);
        }
        
        Category category = optCategory.get();
        category.setCategoryName(categoryInput.getCategoryName());
        categoryService.save(category);
        
        return new ResponseEntity<Response>(new Response(true, "Cập nhật thành công", category), HttpStatus.OK);
    }

    @DeleteMapping(path = "/deleteCategory")
    public ResponseEntity<?> deleteCategory(@Validated @RequestParam("categoryId") Long categoryId) {
        Optional<Category> optCategory = categoryService.findById(categoryId);
        if (optCategory.isEmpty()) {
            return new ResponseEntity<Response>(new Response(false, "Không tìm thấy Category", null), HttpStatus.BAD_REQUEST);
        }
        categoryService.delete(optCategory.get());
        return new ResponseEntity<Response>(new Response(true, "Xóa thành công", optCategory.get()), HttpStatus.OK);
    }
}