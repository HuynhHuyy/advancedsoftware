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

import demo.cdcnpm.model.Order;
import demo.cdcnpm.service.OrderService;
// Đây là controller của quản lý đơn hàng
//Khai báo controller
@Controller
public class OrderController {
    @Autowired
	private OrderService service; // khai báo service của đơn hàng
	

	//Tạo đường dẫn đến trang quản lý đơn hàng và hiển thị các đơn hàng từ cơ sở dữ liệu
	@RequestMapping(value="/Admin/Order", method = RequestMethod.GET)
	public String AdminOrderview(Model model) {
		List<Order> listOrder = service.listAll(); // Sử dụng service.listAll() để hiển thị các đơn hàng từ cơ sở dữ liệu
		model.addAttribute("listOrder",listOrder);
		return "Admin/AdminOrder";
	}

	// Tạo đường dẫn đến trang sửa đơn hàng
    @RequestMapping(value="/Admin/Editorder/{Id}")
	public ModelAndView AdminEditOrderview(@PathVariable(name = "Id") Long Id) {
	    ModelAndView mav = new ModelAndView("Admin/Editorder"); // trả về file giao diện
	    Order order = service.get(Id); // Dựa vào id để hiển thị thông tin đơn hàng cần chỉnh sửa
	    mav.addObject("order", order);
	    return mav;
	}

	// Lưu lại thông tin đơn hàng sau khi chỉnh sửa 
    @RequestMapping(value="/Admin/Saveorder", method = RequestMethod.POST)
	public String AdminOrdersave(Model model, @ModelAttribute("order") Order order)throws IOException {
	             service.save(order); // Sử dụng service để lưu lại thông tin
	             model.addAttribute("account", new Order());
		return "redirect:/Admin/Order";
	}
	
	// Xóa đơn hàng dựa trên id
    @RequestMapping(value="/Admin/Deleteorder/{Id}")
	public String AdminDeleteorder(@PathVariable(name = "Id") Long Id) {
	    service.delete(Id); // Sử dụng service.delete để xóa đơn hàng
	    return "redirect:/Admin/Order";       
	}
}
