package vn.iotstar.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "home"; // Trỏ tới file home.html trong thư mục templates
    }
    
    @GetMapping("/category")
    public String categoryPage() {
        return "category"; // Trỏ tới file category.html trong thư mục templates
    }
}