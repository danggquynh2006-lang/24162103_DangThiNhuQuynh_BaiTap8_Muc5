package vn.iotstar.controller;

import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.iotstar.entity.Category;
import vn.iotstar.entity.Product;
import vn.iotstar.model.Response;
import vn.iotstar.service.ICategoryService;
import vn.iotstar.service.IProductService;
import vn.iotstar.service.IStorageService;
import java.util.List;

// API Lấy danh sách sản phẩm theo Category ID
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping(path = "/api/product")
public class ProductApiController {

    @Autowired
    private IProductService productService;

    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private IStorageService storageService;

    // API Thêm sản phẩm mới
    @PostMapping(path = "/addProduct")
    public ResponseEntity<?> addProduct(
            @RequestParam("productName") String productName,
            @RequestParam(value = "imageFile", required = false) MultipartFile productImages,
            @RequestParam("unitPrice") Double productPrice,
            @RequestParam("description") String productDescription,
            @RequestParam("quantity") Integer quantity,
            @RequestParam(value = "categoryId", required = false) Long categoryId) {

        Product product = new Product();
        try {
            if (productImages != null && !productImages.isEmpty()) {
                UUID uuid = UUID.randomUUID();
                String storedFilename = storageService.getSorageFilename(productImages, uuid.toString());
                product.setImages(storedFilename);
                storageService.store(productImages, storedFilename);
            }

            product.setProductName(productName);
            product.setUnitPrice(productPrice);
            product.setQuantity(quantity);
            product.setDescription(productDescription);

            if (categoryId != null) {
                Optional<Category> optCategory = categoryService.findById(categoryId);
                if (optCategory.isPresent()) {
                    product.setCategory(optCategory.get());
                }
            }

            productService.save(product);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Response>(new Response(false, "Lỗi thêm sách: " + e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<Response>(new Response(true, "Thêm sản phẩm thành công", product), HttpStatus.OK);
    }

    // API Cập nhật sản phẩm
    @PutMapping(path = "/updateProduct/{id}")
    public ResponseEntity<?> updateProduct(
            @PathVariable("id") Long id,
            @RequestParam("productName") String productName,
            @RequestParam(value = "imageFile", required = false) MultipartFile productImages,
            @RequestParam("unitPrice") Double productPrice,
            @RequestParam("description") String productDescription,
            @RequestParam("quantity") Integer quantity,
            @RequestParam(value = "categoryId", required = false) Long categoryId) {

        Optional<Product> optProduct = productService.findById(id);
        if (optProduct.isEmpty()) {
            return new ResponseEntity<Response>(new Response(false, "Không tìm thấy sản phẩm", null), HttpStatus.NOT_FOUND);
        }

        Product product = optProduct.get();
        try {
            if (productImages != null && !productImages.isEmpty()) {
                UUID uuid = UUID.randomUUID();
                String storedFilename = storageService.getSorageFilename(productImages, uuid.toString());
                product.setImages(storedFilename);
                storageService.store(productImages, storedFilename);
            }

            product.setProductName(productName);
            product.setUnitPrice(productPrice);
            product.setQuantity(quantity);
            product.setDescription(productDescription);

            if (categoryId != null) {
                Optional<Category> optCategory = categoryService.findById(categoryId);
                if (optCategory.isPresent()) {
                    product.setCategory(optCategory.get());
                }
            }

            productService.save(product);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Response>(new Response(false, "Lỗi cập nhật: " + e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<Response>(new Response(true, "Cập nhật thành công", product), HttpStatus.OK);
    }
    


 // API Phân trang, tìm kiếm và lọc theo Thể loại
 // API Phân trang, tìm kiếm và lọc theo Thể loại
 // API Phân trang, tìm kiếm và lọc theo Thể loại
    @GetMapping(path = "/paginated")
    public ResponseEntity<?> getProductsPaginated(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size,
            @RequestParam(value = "keyword", defaultValue = "") String keyword,
            @RequestParam(value = "categoryId", required = false) Long categoryId) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productPage;
        
        if (categoryId != null) {
            productPage = productService.findByCategoryCategoryId(categoryId, pageable);
        } else if (keyword != null && !keyword.trim().isEmpty()) {
            productPage = productService.findByProductNameContaining(keyword, pageable);
        } else {
            productPage = productService.findAll(pageable);
        }
        
        // 👉 LUÔN LUÔN CHẠY VÒNG LẶP NÀY CHO MỌI TRƯỜNG HỢP (Tất cả, Tìm kiếm, hay Lọc)
        for (Product p : productPage.getContent()) {
            if (p.getCategory() != null && p.getCategory().getCategoryId() != null) {
                // Tự động gọi service tìm category theo ID để lấy đầy đủ tên thể loại gắn vào
                categoryService.findById(p.getCategory().getCategoryId()).ifPresent(cat -> {
                    p.getCategory().setCategoryName(cat.getCategoryName());
                });
            }
        }
        
        return new ResponseEntity<Response>(new Response(true, "Thành công", productPage), HttpStatus.OK);
    }
    
    // API Xóa sản phẩm
    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable("id") Long id) {
        Optional<Product> product = productService.findById(id);
        if (product.isPresent()) {
            productService.delete(id);
            return new ResponseEntity<Response>(new Response(true, "Xóa thành công", null), HttpStatus.OK);
        }
        return new ResponseEntity<Response>(new Response(false, "Không tìm thấy sản phẩm", null), HttpStatus.NOT_FOUND);
    }
}