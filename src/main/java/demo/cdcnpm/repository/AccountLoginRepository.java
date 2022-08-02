package demo.cdcnpm.repository;



import org.springframework.data.jpa.repository.JpaRepository;

import demo.cdcnpm.model.AdminAccount;

public interface AccountLoginRepository  extends JpaRepository<AdminAccount, String > {
	AdminAccount findAccountByUsername(String username); // tìm kiếm tài khoản theo tên đăng nhập

}
