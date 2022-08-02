package demo.cdcnpm.controller;


import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import demo.cdcnpm.model.AdminProduct;
import demo.cdcnpm.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

// Đây là controller của trang sản phẩm với các chức năng sắp xếp
// Khai báo controller
@Controller
public class ProductHomeController {
    

    @Autowired
	ProductRepository dao; // Khai báo repository của sản phẩm

   
    // Sắp xếp sản phẩm theo giá tăng dần
    @RequestMapping("/Home/product/sort")
    public String productsort(Model model, @RequestParam("p") Optional<Integer> p , @RequestParam("field") Optional<String> field){
        model.addAttribute("field",field.orElse("price").toUpperCase());
        Pageable pageable = PageRequest.of(p.orElse(0), 12,Sort.Direction.ASC,field.orElse("price")); // Hiển thị 12 sản phẩm trên trang theo giá tăng dần
        Page<AdminProduct> product = dao.findAll(pageable);
        model.addAttribute("product", product);
        return "Home/product";
    }

    // Sắp xếp sản phẩm theo giá giảm dần
    @RequestMapping("/Home/product/sortdesc")
    public String productsortdesc(Model model, @RequestParam("p") Optional<Integer> p , @RequestParam("field") Optional<String> field){
        model.addAttribute("field",field.orElse("price").toUpperCase());
        Pageable pageable = PageRequest.of(p.orElse(0), 12,Sort.Direction.DESC,field.orElse("price"));// Hiển thị 12 sản phẩm trên trang theo giá giảm dần
        Page<AdminProduct> product = dao.findAll(pageable);
        model.addAttribute("product", product);
        return "Home/product";
    }


}
