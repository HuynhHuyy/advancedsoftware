package demo.cdcnpm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import demo.cdcnpm.model.AdminAccount;
import demo.cdcnpm.repository.AccountRepository;

// Viết các service cần thiết
@Service
public class AccountService {

	
	@Autowired
	private AccountRepository repo; // Khai báo repo để sử dụng các hàm có sẵn của JpaRepository

	// Hàm hiển thị tất cả tài khoản
	public List<AdminAccount> listAll(){
		return repo.findAll();
	}
	

	// Hàm lưu thông tin tài khoản 
	public void save(AdminAccount account) {
		repo.save(account);
	}
	
	//Hàm tìm kiếm tài khoản dựa theo id
	public AdminAccount get(Long id) {
		return repo.findById(id).get();
	}
	
	// Hàm xóa tài khoản dựa trên id
	public void delete(Long id) {
		 repo.deleteById(id);
	}
}
