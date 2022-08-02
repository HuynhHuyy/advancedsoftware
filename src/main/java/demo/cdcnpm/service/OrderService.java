package demo.cdcnpm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import demo.cdcnpm.model.Order;
import demo.cdcnpm.repository.OrderRepository;

@Service
public class OrderService {
	@Autowired
	private OrderRepository repo; // Khai báo repository để sử dụng các hàm từ JpaRepository
	
	// Hiển thị tất cả đơn hàng từ cở sở dữ liệu
	public List<Order> listAll(){
		return repo.findAll();
	}
	//Lưu thông tin đơn hàng 
	public void save(Order order) {
		repo.save(order);
	}
	// Tìm đơn hàng theo id
	public Order get(Long id) {
		return repo.findById(id).get();
	}
	
	// Xóa đơn hàng theo id
	public void delete(Long id) {
		 repo.deleteById(id);
	}


	
}
