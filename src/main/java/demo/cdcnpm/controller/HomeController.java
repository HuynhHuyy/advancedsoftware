package demo.cdcnpm.controller;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import demo.cdcnpm.model.AdminAccount;
import demo.cdcnpm.model.AdminProduct;
import demo.cdcnpm.model.CartItem;
import demo.cdcnpm.model.Order;
import demo.cdcnpm.model.OrderDetail;
import demo.cdcnpm.repository.AccountLoginRepository;
import demo.cdcnpm.repository.AccountRepository;
import demo.cdcnpm.repository.OrderDetailRepository;
import demo.cdcnpm.repository.OrderRepository;
import demo.cdcnpm.repository.ProductCategoryRepository;
import demo.cdcnpm.repository.ProductRepository;
import demo.cdcnpm.repository.ShoppingCartService;
import demo.cdcnpm.service.AccountService;
import demo.cdcnpm.service.OrderService;
import demo.cdcnpm.service.SessionService;


// Đây là trang giao diện của phía người dùng
//Khai báo controller
@Controller
public class HomeController {
	//Khai báo các repository và service cần thiết
	@Autowired
	AccountLoginRepository dao;
	@Autowired
	AccountRepository accountdao;
	@Autowired
	OrderRepository orderdao;
	@Autowired
	OrderDetailRepository orderDetails;
	@Autowired
	OrderService orderservice;
	@Autowired
	SessionService session;
	@Autowired
	ProductRepository productdao;
	@Autowired
	ProductCategoryRepository productcategory;
	@Autowired
	private AccountService service;
	@Autowired
    ShoppingCartService shoppingCartService;
		
	
		//Tạo đường dẫn Trang chủ và hiển thị sản phẩm nổi bật
	   @RequestMapping(value="/Home/views")
		public String homeViews(Model model,@RequestParam("p") Optional<Integer> p) {
			AdminProduct item = new AdminProduct(); // Khai báo một sản phẩm mới
			model.addAttribute("item",item); // truyền dữ liệu qua giao diện hiển thị
		   	PageRequest pageable = PageRequest.of(p.orElse(0), 6); //Sử dụng pageable để hiển thị theo số trang ( ví dụ một trang ở đây là 6 sản phẩm)
	        Page<AdminProduct> products = productdao.findAll(pageable); // Tiến hành hiển thị
			model.addAttribute("products",products);
		    return "Home/index";       
		}

		// Tạo đường dẫn trang giới thiệu
	    @RequestMapping(value="/Home/about")
		public String homeAbout() {
		    return "Home/about";       
		}
		// Tạo đường dẫn trang liên hệ
		@RequestMapping(value="/Home/contact")
	  		public String homecontact() {
	  		    return "Home/contact";       
	  		}
		
		// Tạo đường dẫn trang sản phẩm và hiển thị sản phẩm
	    @RequestMapping(value="/Home/product")
		public String AdminProductview(Model model,@RequestParam("p") Optional<Integer> p) {
	        PageRequest pageable = PageRequest.of(p.orElse(0), 12); // số sản phẩm hiển thị trên 1 trang
	        Page<AdminProduct> product = productdao.findAll(pageable); // Hiển thị sản phẩm
			model.addAttribute("product",product);
			return "Home/product";
		}
	    
		// Tạo đường dẫn đến trang đăng nhập
	    @GetMapping(value="/account/login")
  		public String homelogin() {
  		    return "Home/loginform";       
  		}

		// Kiểm tra tài khoản đăng nhập
	    @PostMapping("/account/login")
	    public String login(Model model,HttpServletRequest request,
	                        @RequestParam("username") String username,
	                        @RequestParam("password") String password , HttpServletRequest req) {

	           AdminAccount user = dao.findAccountByUsername(username); // Tìm tài khoản theo tên đăng nhập
	            if (!user.getPassword().equals(password)) // so sánh tài mật khẩu của cở sở dữ liệu với mật khẩu người dùng nhập
				 {
	                model.addAttribute("message", "Invalid password");
	            } else {
	            	String url = request.getRequestURI();
	                String uri = session.get("security-uri");
	                if (uri != null) {
	                    return "redirect:" + uri;
	                } else {

	                    if(user.getActivated()) // kiểm tra tài khoản đã được kích hoạt hay chưa
						{
	                    	if(!user.getRole() && url.startsWith("/Admin/**")) // Không sử dụng được link bên trong Admin nếu chưa đăng nhập
							 {
	                    		model.addAttribute("message", "Please Login with your permission");
								return "redirect:/account/login";
	                    	}

	                    	else if(user.getRole()) // nếu vai trò là admin sẽ được vào trang quản lý tài khoản
							{
								model.addAttribute("message", "Login succeed");
								session.set("user", user);
								return "redirect:/Admin/Account";
							}else // nếu vai trò là admin sẽ được vào trang chủ
							{
								model.addAttribute("message", "Login succeed");
								session.set("user", user);
								return "redirect:/Home/views";
							}	                    
	                }else  // đăng nhập thất bại
					{
						model.addAttribute("message", "Login fail");
					}
	            }
			}

	        return "Home/loginform"; 
	    }

		//Tạo đường dẫn đăng xuất
	    @RequestMapping("/account/logout")
	    public String logout(){
	      session.remove("user");
	        return "redirect:/Home/views";
	    }
	    
	    
		//Tạo đường dẫn đăng kí
	    @RequestMapping("/account/register")
	    public String register(@ModelAttribute("item") AdminAccount item){

	        return"Home/signupform";
	    }
	    
		// Lưu dữ liêu của người dùng khi đã đăng kí thành công
		@RequestMapping(value="/account/signup", method = RequestMethod.POST)
		public String signup(Model model, @ModelAttribute("signup") AdminAccount account)throws IOException {
		             service.save(account); // Sử dụng service để lưu lại dữ liệu tài khoản
		             model.addAttribute("account", new AdminAccount());
			return "redirect:/account/login";
		}

		// Tạo đường dẫn chi tiết sản phẩm
		@RequestMapping("/Home/product/about/{id}")
	    public String aboutproduct(Model model, @PathVariable("id") Long Id){
	        AdminProduct item = productcategory.findById(Id); // Tìm sản phẩm theo Id 
	        model.addAttribute("item", item);
	        return "Home/aboutproduct";
	    }

		// Tìm kiếm sản phẩm bằng cách nhập vào
		 @RequestMapping("/product/search")
		    public String searchAndPage(Model model, @RequestParam("keywords") Optional<String> kw, @RequestParam("p") Optional<Integer> p) {
		        AdminProduct item = new AdminProduct();
		        model.addAttribute("item",item);
				String kwords = kw.orElse(session.get("keywords"));
		        session.set("keywords", kwords); // Sử dụng sessiond để set keywords cần tìm
		        Pageable pageable = PageRequest.of(p.orElse(0), 5); // Hiển thị 5 sản phẩm mỗi trang khi tìm được
		        Page<AdminProduct> products = productcategory.findByNameLike("%"+kwords+"%", pageable); // Sử dụng findByNameLike để tìm sản phẩm
		        model.addAttribute("product", products);
		        return "Home/product";
		    }
		  
		  // Thêm sản phẩm vào giỏ hàng
		  @RequestMapping(value = "/shoppingcart/add/{id}" )
		    public String addItemCart(Model model ,@PathVariable("id") Long Id){
		        AdminProduct product = productcategory.findById(Math.toIntExact(Id));
		        CartItem  item = new CartItem ();
		            BeanUtils.copyProperties(product,item);
		            item.setQuantity(1);
		        item.setId(product.getId());
		        shoppingCartService.add(item);
		        return "redirect:/shoppingcart/index";
		    }
		  
			// Tạo đường dẫn trang giỏ hàng
		  @RequestMapping("/shoppingcart/index")
		    public String indexShoppingCart(Model model){
		        Collection<CartItem>  cartItems = shoppingCartService.getCartItems();
		        model.addAttribute("items",cartItems); // Hiển thị sản phẩm giỏ hàng
		        model.addAttribute("total",shoppingCartService.getAmount()); // Hiển thị tổng giá các sản phẩm
		        model.addAttribute("NoOfItem",shoppingCartService.getCount()); // Số lượng ben trong giỏ hàng
		        return "Home/itemCart";
		    }
		
			// Xóa sản phẩm bên trong giỏ hàng
		  @RequestMapping("/remove/{id}")
		    public String removeShoppingCart(@PathVariable("id") Integer Id){
		        shoppingCartService.remove(Id); // Sử dụng service để xóa sản phẩm theo id
		        return "redirect:/shoppingcart/index";
		    }

			// Tăng hoặc giảm số lượng sản phẩm bên trong giỏ hàng
		  @RequestMapping("/shoppingcart/update")
		    public String updateShoppingCart(@RequestParam("id") Integer id,@RequestParam("quantity") Integer quantity){
		        shoppingCartService.update(id,quantity); // Sử dụng service để tăng hoặc giảm số lượng theo id
		        return "redirect:/shoppingcart/index";
		    }
		  
		  // Tạo đường dẫn đến trang thanh toán
		  @RequestMapping("/shoppingcart/save")
		    public String checkShoppingCart(Model model){
		        Collection<CartItem>  cart = shoppingCartService.getCartItems();
		        model.addAttribute("items",cart); // Hiển thị giỏ hàng
		        model.addAttribute("total",shoppingCartService.getAmount()); // Giá tổng của các sản phẩm khi tạm tính
		        model.addAttribute("NoOfItem",shoppingCartService.getCount()); // Số lượng sản phẩm 
		        model.addAttribute("order", new Order());
		        return "Home/checkform";
		    }

			//Tạo đường dẫn đến trang sau khi thanh toán và lưu hóa đơn vào cơ sở dữ liệu
		  @RequestMapping(value="/shoppingcart/thankyou",method = RequestMethod.POST)
			public String saveorder(Model model ,@ModelAttribute("order") Order order )throws IOException{
			orderservice.save(order); // sử dụng service để lưu sản phẩm
			 model.addAttribute("order", new Order());
			 
			 Collection<CartItem>  carts = shoppingCartService.getCartItems();
			 AdminProduct prd = new AdminProduct();
				for(CartItem x : carts){
				OrderDetail items = new OrderDetail(); // Tạo một orderdetail để lưu chi tiết sản phẩm vào
				prd.setId(x.getId()); // lưu id sản phẩm
				items.setQuantity(x.getQuantity()); // lưu số lượng
				items.setOrder(order); // lưu hóa đơn
				items.setPrice(x.getPrice()); // lưu tổng tiền
				items.setProduct(prd); // lưu sản phẩm
		  AdminProduct product = productdao.findAllById(x.getId());
		  if(product.getQuantity() < x.getQuantity()) // so sánh số lượng sản phẩm đang có và khách hàng mua
		  {
			  model.addAttribute("message","This product just have "+product.getQuantity());
			  }else{ 
				  orderDetails.save(items);
				  product.setQuantity(product.getQuantity()-items.getQuantity());
				  productdao.save(product);
				  model.addAttribute("message", "Order Success");
		
			  }
				}
				return "Home/thankyou";
			}
		
		// Tạo đường dẫn xem thông tin chi tiết tài khoản ở trang khách hàng
		@RequestMapping(value="/Home/Account/Views/{Id}")
		public ModelAndView HomeAccountview(Model model, @PathVariable("Id") String username) {
		ModelAndView mav = new ModelAndView("Home/profile"); // trả về file  giao diện 
		AdminAccount adminaccount = dao.findAccountByUsername(username); // Dựa theo tên đăng nhập để hiển thị thông tin
		mav.addObject("account", adminaccount);
		
		return mav;
		}
		
		// Tạo đường dẫn đến trang chỉnh sửa thông tin tài khoản ở trang khách hàng
		@RequestMapping(value = "/Home/Account/Edit/{Id}")
		public ModelAndView HomeEditAccountview(Model model, @PathVariable("Id") String username) {
			ModelAndView mav = new ModelAndView("Home/settingaccount"); // trả về file giao diện
			AdminAccount adminaccount = dao.findAccountByUsername(username);// Dựa theo tên đăng nhập để hiển thị thông tin
			mav.addObject("account", adminaccount); 

			return mav;
		}

		// Lưu lại thông tin tài khoản sau khi được chỉnh sửa
		@RequestMapping(value = "/Home/Account/Update", method = RequestMethod.POST)
		public String HomeAccountsave(Model model, @ModelAttribute("account") AdminAccount account) throws IOException {
			service.save(account); // Lưu vào cở sở dữ liệu
			model.addAttribute("account", new AdminAccount());
			return "redirect:/Home/views";
		}

		// Tạo đường dẫn đến trang đơn hàng
		@RequestMapping(value = "/Home/YourCart/{Id}", method = RequestMethod.GET)
		public String YourCart(Model model, @PathVariable("Id") String username) {
			List<Order> order = orderdao.findAccountByUsername(username); // Hiển thị các đơn hàng theo tên đăng nhập
			model.addAttribute("order", order);
			return "Home/yourorder";
		}

		// Tạo đường dẫn đến trang thay đổi mật khẩu
		@RequestMapping("/Home/account/change/{Id}")
		public String password(Model model, @PathVariable("Id") long id) {
			AdminAccount item = accountdao.findById(id); // Dựa vào id để hiển thị thông tin
			model.addAttribute("item", item);
			List<AdminAccount> items = accountdao.findAll();
			model.addAttribute("items", items);
			return "Home/changepassword";
		}
		

		//Lưu mật khẩu sau khi người dùng đổi
		@RequestMapping(value="/Home/account/change/password",method = RequestMethod.POST)
    	public String passwordchange( @ModelAttribute("item") AdminAccount item,@RequestParam("old") String old,@RequestParam("newp") String newp, Model model){
			AdminAccount account = dao.findAccountByUsername(item.getUsername());

       	 	if(item.getPassword().equals(old)) // So sánh mật khẩu đã nhập vào với mật khẩu trong  cơ sở dữ liệu
				{
       	 	if(newp.equals("")){
       	 		model.addAttribute("message","Please enter your password");
       	 	}else // Lưu lại mật khẩu mới và các thông tin liên quan đến tài khoản
				 {
       	 			account.setId(item.getId());
                    account.setActivated(item.getActivated());
                    account.setAddress(item.getAddress());
                    account.setPassword(newp);
                    account.setRole(item.getRole());
                    account.setFullname(item.getFullname());
                    account.setPhone(item.getPhone());
                    account.setUsername(item.getUsername());
                    dao.save(account);
                    model.addAttribute("message","success full");
       	 	}
            }else{
                model.addAttribute("message","New password aren't match Confirmpassword");
            }

        return"home/changepassword";
		}
	
	
}
