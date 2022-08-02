package demo.cdcnpm.model;

public class CartItem {
	  	private long Id;
	    private String name;
	    private String image;
	    private int quantity;
	    private int price;
		public long getId() {
			return Id;
		}
		public void setId(long id) {
			this.Id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getImage() {
			return image;
		}
		public void setImage(String image) {
			this.image = image;
		}
		public int getQuantity() {
			return quantity;
		}
		public void setQuantity(int quantity) {
			this.quantity = quantity;
		}
		public int getPrice() {
			return price;
		}
		public void setPrice(int price) {
			this.price = price;
		}
		public CartItem() {
		
		}
		public CartItem(long id, String name, String image, int quantity, int price) {
			Id = id;
			this.name = name;
			this.image = image;
			this.quantity = quantity;
			this.price = price;
		}
	    
}
