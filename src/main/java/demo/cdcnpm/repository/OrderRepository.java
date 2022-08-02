package demo.cdcnpm.repository;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import demo.cdcnpm.model.Order;

public interface OrderRepository extends JpaRepository<Order,Long> {
    List<Order> findAccountByUsername(String username); // Tìm kiếm tài khoản dựa trên tên đăng nhập dành cho list đơn hàng
    List<Order> findAllById(long id); // Tìm kiếm tất cả id của list đơn hàng
    
    
    @Query(value = "Select sum(price)  from orders where  Year(create_date) like '2022' ",nativeQuery = true)
    Long selectTotals(); //tính tổng doanh thu trong năm


    
    
}
  
