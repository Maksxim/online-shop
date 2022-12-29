package by.tms.project.controller;

import by.tms.project.entities.Product;
import by.tms.project.services.ProductService;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;

@Controller
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service){
       this.service = service;
    }

    @GetMapping("/")
    public String showProductList(Model model) {
        model.addAttribute("products", service.getAll());
        return "index";
    }

    @GetMapping("/signup")
    public String showSignUpForm(Product product) {
        return "add-product";
    }

    @PostMapping("/addproduct")
    public String addProduct(@Valid Product product, BindingResult result, @RequestParam("image") MultipartFile file, Model model) throws IOException {
        if (result.hasErrors()) {
            return "add-product";
        }
        if (file != null && !file.isEmpty()) {
            // вызвать метод сохранения файла
            String imagePath = service.saveImage(file);
            // добавить в обьект продукт значение пути файла
            product.setImagePath(imagePath);
        }
        service.createProduct(product);
        return "redirect:/";
    }

    @GetMapping(value = "/product/{id}/image")
    public @ResponseBody byte[] getImage(@PathVariable("id") int productId) throws IOException {
        Product product = service.getProduct(productId);

        String imagePath = product.getImagePath();
        if (imagePath == null) {
            return null;
        }
        return FileUtils.readFileToByteArray(new File(imagePath));
    }

    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") int id, Model model) {
        Product product = service.getProduct(id);
        model.addAttribute("product", product);
        model.addAttribute("image", product);

        return "update-product";
    }

    @PostMapping("/update/{id}")
    public String updateProduct(@PathVariable("id") int id, @Valid Product product, BindingResult result, Model model,@RequestParam("image") MultipartFile file) throws IOException {
        if (result.hasErrors()) {
            product.setId(id);
            return "update-product";
        }
        if(file != null && !file.isEmpty()) {
            String imagePath = service.saveImage(file);
            product.setImagePath(imagePath);
        }
        service.updateProduct(product);
        return "redirect:/";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") int id, Model model) {
        service.delete(id);

        return "redirect:/";
    }
}
