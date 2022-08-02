package demo.cdcnpm.repository;



import org.springframework.data.jpa.repository.JpaRepository;

import demo.cdcnpm.model.AdminProduct;

public interface ProductRepository extends JpaRepository<AdminProduct, Long> {

	AdminProduct findById(long id); // tìm kiếm sản phẩm theo id
	AdminProduct findAllById(long id); // tìm tất cả id của sản phẩm

}
	
