package demo.cdcnpm.repository;



import org.springframework.data.jpa.repository.JpaRepository;

import demo.cdcnpm.model.OrderDetail;

public interface OrderDetailRepository extends  JpaRepository<OrderDetail,Long> {
  
}
