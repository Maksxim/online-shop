package by.tms.project.controller;

import by.tms.project.entities.Product;
import by.tms.project.services.ProductService;
import by.tms.project.services.ShoppingCartService;
import org.apache.commons.io.FileUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class ProductController {

    private final ProductService service;
    private final ShoppingCartService shoppingCartService;

    public ProductController(ProductService service, ShoppingCartService shoppingCartService) {
        this.service = service;
        this.shoppingCartService = shoppingCartService;
    }

    @GetMapping("/admin")
    public String defaultAdminPage()
    {
        return "redirect:/admin/product";
    }



    @GetMapping("/admin/product")
    public String showAdminProductList(Model model,
                                  @RequestParam("page") Optional<Integer> page,
                                  @RequestParam("size") Optional<Integer> size) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(10);

        Page<Product> productPage = service.getAll(PageRequest.of(currentPage - 1, pageSize));
        model.addAttribute("products", productPage);

        int totalPages = productPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "admin-product";
    }

    @GetMapping("/product")
    public String showProductList(HttpServletRequest request, Model model,
                                  @RequestParam("page") Optional<Integer> page,
                                  @RequestParam("size") Optional<Integer> size) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(10);

        Page<Product> productPage = service.getAll(PageRequest.of(currentPage - 1, pageSize));
        model.addAttribute("products", productPage);

        int totalPages = productPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        model.addAttribute("cartInfo", shoppingCartService.getCartInfoFromSession(request));
        return "product";
    }

    @GetMapping("/admin/product/add")
    public String showAddProductForm(Product product) {
        return "add-product";
    }

    @PostMapping("/admin/product")
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
        return "redirect:/admin/product";
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

    @GetMapping("/admin/product/{id}")
    public String showEditForm(@PathVariable("id") int id, Model model) {
        Product product = service.getProduct(id);
        model.addAttribute("product", product);

        return "update-product";
    }

    @PostMapping("/admin/product/{id}")
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
        return "redirect:/admin/product";
    }

    @GetMapping("/product/{id}")
    public String viewProduct(@PathVariable("id") int id, Model model, HttpServletRequest request){
        Product product = service.getProduct(id);
        model.addAttribute("product", product);
        model.addAttribute("cartInfo", shoppingCartService.getCartInfoFromSession(request));
        return "view-product";
    }

    @GetMapping("/admin/product/delete/{id}")
    public String deleteProduct(@PathVariable("id") int id, Model model) {
        service.delete(id);

        return "redirect:/admin/product";
    }
}
