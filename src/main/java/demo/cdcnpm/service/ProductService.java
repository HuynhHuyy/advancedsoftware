package demo.cdcnpm.service;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import demo.cdcnpm.model.AdminProduct;
import demo.cdcnpm.repository.ProductRepository;


@Service
public class ProductService {
	@Autowired
    HttpServletRequest request;

	@Autowired
	private ProductRepository repo; // Khai báo để sử dụng các hàm có sẵn từ JpaRepository
	

    //Hiển thị tất cả sản phẩm
	public List<AdminProduct> listAll(){
		return repo.findAll();
	}
	
	//Lưu thông tin sản phẩm sau khi chỉnh sửa hoặc thêm vào
	public void save(AdminProduct product) {
		repo.save(product);
	}
	
    //Hàm lấy id của sản phẩm
	public AdminProduct get(Long id) {
		return repo.findById(id).get();
	}
	
	//Hàm xóa sản phẩm dựa trên id
	public void delete(Long id) {
		 repo.deleteById(id);
	}

	public File saveImage(MultipartFile file, String path) {
        if (!file.isEmpty()) {
            File dir = new File(request.getServletContext().getRealPath(path));
            if (!dir.exists()) {
                dir.mkdirs();
            }
            try {
                File savedFile = new File(dir, file.getOriginalFilename());
                file.transferTo(savedFile);
                return savedFile;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }
}
