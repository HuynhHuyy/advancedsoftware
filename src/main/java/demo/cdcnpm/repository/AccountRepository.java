package demo.cdcnpm.repository;





import org.springframework.data.jpa.repository.JpaRepository;

import demo.cdcnpm.model.AdminAccount;

public interface AccountRepository extends JpaRepository<AdminAccount,Long>{
 
    AdminAccount findById(long id); // Tìm kiếm tài khoản dựa trên id
}
