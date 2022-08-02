package demo.cdcnpm.repository;

import java.util.Collection;

import demo.cdcnpm.model.CartItem;

public interface ShoppingCartService {
	
	 	void add(CartItem item);

	    void remove(int id);

	    Collection<CartItem> getCartItems();

	    void clear();

	    void update(int id, int quantity);

	    Long getAmount();

	    int getCount();
}
