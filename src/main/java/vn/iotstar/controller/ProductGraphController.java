package vn.iotstar.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import vn.iotstar.entity.Product;
import vn.iotstar.repository.ProductRepository;

@Controller
public class ProductGraphController {

    @Autowired
    private ProductRepository productRepository;

    @QueryMapping
    public List<Product> allProductsSortedByPrice() {
        return productRepository.findAllByOrderByUnitPriceAsc();
    }

    @QueryMapping
    public List<Product> productsByCategoryId(@Argument Long categoryId) {
        return productRepository.findByCategoryCategoryId(categoryId);
    }
    
    @QueryMapping
    public List<Product> products() {
        return productRepository.findAll();
    }

    @MutationMapping
    public Product createProduct(@Argument ProductInput product) {
        Product newProduct = new Product();
        newProduct.setProductName(product.getProductName());
        newProduct.setQuantity(product.getQuantity());
        newProduct.setUnitPrice(product.getUnitPrice());
        newProduct.setDescription(product.getDescription());
        newProduct.setImages(product.getImages());
        newProduct.setDiscount(0.0);
        newProduct.setStatus((short) 1);
        return productRepository.save(newProduct);
    }
}

class ProductInput {
    private String productName;
    private int quantity;
    private double unitPrice;
    private String images;
    private String description;
    
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public double getUnitPrice() { return unitPrice; }
    public void setUnitPrice(double unitPrice) { this.unitPrice = unitPrice; }
    public String getImages() { return images; }
    public void setImages(String images) { this.images = images; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}