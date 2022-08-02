package demo.cdcnpm.controller;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import demo.cdcnpm.model.AdminAccount;
import demo.cdcnpm.model.AdminProduct;
import demo.cdcnpm.model.Order;
import demo.cdcnpm.repository.AccountLoginRepository;
import demo.cdcnpm.repository.OrderRepository;
import demo.cdcnpm.repository.ProductRepository;
import demo.cdcnpm.service.ProductService;

//Đây là controller trang quản lý sản phẩm
//Khai báo controller
@Controller
public class ProductController {
	//Khai báo các service à repo cần thiết
	@Autowired
	private ProductService service;
	@Autowired
	private ProductRepository repo;
	@Autowired
	OrderRepository orderdao;
	@Autowired
	AccountLoginRepository accountdao;
	

	// Tạo đường dẫn đến trang quản lý sản phẩm và hiển thị danh sách sản phẩm từ cơ sở dữ liệu
	@RequestMapping(value="/Admin/Product", method = RequestMethod.GET)
	public String AdminProductview(Model model) {
		List<AdminProduct> listProducts = service.listAll(); // sử dụng service để lấy danh sách sản phẩm
		model.addAttribute("listProducts",listProducts);
		return "Admin/AdminProduct";
	}
	
	// Tạo đường dẫn đến trang thêm sản phẩm
	@RequestMapping(value="/Admin/Addproduct", method = RequestMethod.GET)
	public String AdminProductadd(Model model) {
		AdminProduct product = new AdminProduct(); 
		model.addAttribute("product",product);
		return "Admin/AddProduct";
	}
	
	// Lưu dữ liệu sau khi chỉnh sửa hoặc thêm
	@RequestMapping(value="/Admin/Saveproduct", method = RequestMethod.POST)
	public String AdminProductsave(@ModelAttribute(name = "product") AdminProduct product,RedirectAttributes ra, @RequestParam("fileImage") MultipartFile multipartFile )throws IOException {
		String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        product.setImage(fileName);
        AdminProduct adminproduct = repo.save(product);
        String uploadDir = "src/main/resources/static/upload/"; //lưu hình ảnh đến đường dẫn
        Path uploadPath = Paths.get(uploadDir);
        if(!Files.exists(uploadPath)) {
        	Files.createDirectories(uploadPath);
        }
        try( InputStream inputStream = multipartFile.getInputStream()){
       Path filePath = uploadPath.resolve(fileName);
 	   Files.copy(inputStream,filePath,StandardCopyOption.REPLACE_EXISTING);
      }catch(IOException e){
   	   throw new IOException("upload fail");
      }
		ra.addFlashAttribute("message","upload success");
		return "redirect:/Admin/Product";
	}
	
	//Tạo đường dẫn đến trang sửa sản phẩm
	@RequestMapping(value="/Admin/Editproduct/{Id}")
	public ModelAndView AdminEditview(@PathVariable(name = "Id") Long Id) {
	    ModelAndView mav = new ModelAndView("Admin/Editproduct"); // trả về file giao diện
	    AdminProduct adminproduct = service.get(Id); // Dựa trên id để hiển thị thông tin chỉnh sửa
	    mav.addObject("product", adminproduct);
	     
	    return mav;
	}
	
	// Xóa sản phẩm dựa trên id
	@RequestMapping(value="/Admin/Deleteproduct/{Id}")
	public String AdminDeleteproduct(@PathVariable(name = "Id") Long Id) {
	    service.delete(Id); // sử dụng service để xóa sản phẩm theo id
	    return "redirect:/Admin/Product";       
	}
	
	
	
  @RequestMapping("/Admin/Views")
  public String Homestatis(Model model){
      //Hien thi don hang
   Order item = new Order();
   model.addAttribute("item", item);
   List<Order> items = orderdao.findAll();
   model.addAttribute("items", items);

   //Thong ke User hien tai
   List<AdminAccount> accounts = accountdao.findAll();
   long count = 0;
   for (AdminAccount ac : accounts) {
          count++;
          model.addAttribute("count", count);
   }
   //Thong ke Doanh thu theo nam
      Long orderList = orderdao.selectTotals();
      model.addAttribute("orderList",orderList);


      return "Admin/AdminStatis";
  }
	
	
	
	
	
	
	
	
	
	
}
