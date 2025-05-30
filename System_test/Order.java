package System_test;
public class Order {
    private int menuId;
    private int quantity;
    private int totalPrice;

    Order(int m, int q, int pricePerUnit){
        menuId = m;
        quantity = q;
        totalPrice = quantity * pricePerUnit;  // คำนวณราคารวมทันที
    }

    public void setMenuId(int m){
        menuId = m;
    }

    public void setQuantity(int q){
        quantity = q;
    }

    public void setTotalPrice(int pricePerUnit){
        totalPrice = quantity * pricePerUnit;  // คำนวณใหม่ตามจำนวน
    }

    public int getMenuId(){
        return menuId;
    }

    public int getQuantity(){
        return quantity;
    }

    public int getTotalPrice(){
        return totalPrice;
    }

    @Override
    public String toString(){
        return  "Menu ID: " + menuId +
                " | Quantity: " + quantity +
                " | Total Price: " + totalPrice + " Baht";
    }
}
