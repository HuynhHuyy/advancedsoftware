package demo.cdcnpm.repository;





import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import demo.cdcnpm.model.AdminProduct;
@Transactional
public interface ProductCategoryRepository extends JpaRepository<AdminProduct, String> {
	  Page<AdminProduct>findByNameLike(String keywords, Pageable pageable); // Tìm kiếm các sản phẩm có tên giống nhau
	  AdminProduct findById(long id); // Tìm kiếm sản phẩm theo id

	 
}
