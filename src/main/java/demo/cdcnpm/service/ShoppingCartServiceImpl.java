package demo.cdcnpm.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import demo.cdcnpm.model.CartItem;
import demo.cdcnpm.repository.ShoppingCartService;
// Đây là service của giỏ hàng
@Service
@SessionScope
public class ShoppingCartServiceImpl implements ShoppingCartService {
	private Map<Integer, CartItem> map = new HashMap<Integer,CartItem>(); // Sử dụng map để lưu thông tin giỏ hàng

	//  Hàm thêm sản phẩm vào giỏ hàng
    @Override
    public void add(CartItem item){
        CartItem existed = map.get(item.getId());
        if(existed!=null){
            existed.setQuantity(item.getQuantity()+1    );
        }else
            map.put((int) item.getId(),item);
    }

	// Hàm xóa sản phẩm khỏi giỏ hàng
	@Override
	public void remove(int id) {
		  map.remove(id);
		
	}

	//values	
	@Override
	public Collection<CartItem> getCartItems() {
		return map.values();
	}

	//clear
	@Override
	public void clear() {
		 map.clear();
		
	}

	// Thay đổi số lượng sản phẩm
	@Override
	public void update(int id, int quantity) {
		 CartItem item = map.get(id);
	        item.setQuantity(quantity);
	        if(item.getQuantity()<=0){
	            map.remove(id);
	        }
		
	}
//Tính tổng giá tiền của các sản phẩm
	@Override
	public Long getAmount() {
		return map.values().stream().mapToLong(item->item.getPrice()*item.getQuantity()).sum();
	}
//Tính số lượng sản phẩm
	@Override
	public int getCount() {
		return map.values().size();
	}
	

}
