import cs2030s.fp.Transformer;
import java.util.List;

class Lab07Mini1 {

  public static void main(String[] args) {
    List<Product> products = List.of(
      new Product(2030, 450, "Eggs"),
      new Product(2040, 456, "Eggs"),
      new Product(2100, 320, "Milk"),
      new Product(2101, 333, "Milk"),
      new Product(3230, 545, "Bread"),
      new Product(3231, 555, "Bread")
    );

    Cart myShoppingCart = new Cart(products);
    System.out.println(String.format("Total cost: %d cents", myShoppingCart.totalCost()));
    System.out.println(String.format("Num of items >= $4.50: %d", myShoppingCart.numOfExpensiveItems(450)));
    System.out.println(String.format("Product with name \"Milk\": %s", myShoppingCart.findByName("Milk")));
    System.out.println(String.format("Product with name \"Bread\": %s", myShoppingCart.findByName("Bread")));
    System.out.println(String.format("Product with name \"Tofu\": %s", myShoppingCart.findByName("Tofu")));
  }
}