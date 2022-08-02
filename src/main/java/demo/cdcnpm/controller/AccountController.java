package demo.cdcnpm.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import org.springframework.web.servlet.ModelAndView;

import demo.cdcnpm.model.AdminAccount;
import demo.cdcnpm.service.AccountService;
//Đây là controller của quản lý tài khoản
//Khai báo controller
@Controller
public class AccountController {
	@Autowired
	private AccountService service; // Khai báo service của tài khoản
	
	//Lấy danh sách tài khoản từ cơ sở dữ liệu
	@RequestMapping(value="/Admin/Account", method = RequestMethod.GET) // set đường dẫn, sử dụng method GET để lấy danh sách
	public String AdminAccountview(Model model) {
		List<AdminAccount> listAccounts = service.listAll(); // sử dụng hàm listAll() của service để lấy ra danh sách
		model.addAttribute("listAccount",listAccounts); // truyền attribute qua giao diện
		return "Admin/AdminAccount"; // trả về tên file
	}

	// Thêm tài khoản vào cơ sở dữ liệu
	@RequestMapping(value="/Admin/Addaccount", method = RequestMethod.GET) // set đường dẫn, sử dụng method GET 
	public String AdminAccountadd(Model model) {
		AdminAccount account = new AdminAccount();  // tạo tài khoản mới
		model.addAttribute("account",account); // truyền attribute qua giao diện
		return "Admin/AddAccount"; // trả về tên file
	}
	
	// Lưu dữ liệu vào cơ sở dữ liệu sau khi form được submit - sử dụng cho cả Sửa và Thêm tài khoản
	@RequestMapping(value="/Admin/Saveaccount", method = RequestMethod.POST) // set đường dẫn, sử dụng method POST
	public String AdminAccountsave(Model model, @ModelAttribute("account") AdminAccount account)throws IOException 
	{
	    service.save(account); // sử dụng service để lưu lại thông tin của tài khoản
	    model.addAttribute("account", new AdminAccount()); 
		return "redirect:/Admin/Account"; // trả về tên file
	}
	
	// Sửa thông tin tài khoản
	@RequestMapping(value="/Admin/Editaccount/{Id}")
	public ModelAndView AdminEditAccountview(@PathVariable(name = "Id") Long Id)
	 {
	    ModelAndView mav = new ModelAndView("Admin/Editaccount"); // trả về tên file
	    AdminAccount adminaccount = service.get(Id); // Sử dụng service.get để lấy Id tài khoản cần sửa thông tin
	    mav.addObject("account", adminaccount); // truyền attribute qua giao diện
	     
	    return mav;
	}
	
	// Xóa tài khoản thông qua ID
	@RequestMapping(value="/Admin/Deleteaccount/{Id}")
	public String AdminDeleteaccount(@PathVariable(name = "Id") Long Id) {
	    service.delete(Id); // Sử dụng service.delete để xóa tài khoản qua id
	    return "redirect:/Admin/Account";       // trả về tên file
	}

	
}
