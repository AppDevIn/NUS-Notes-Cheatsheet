import java.util.List;

class Test1 {
  private static void print(String s) {
    System.out.println("\n---- " + s + " ----");
  }

  /**
   * Main method for Test.
   *
   * @param args Ignored and unused command line arguments.
   */
  public static void main(String[] args) {

    CS2030STest i = new CS2030STest();

    print("Running tests...");
    {
    List<Product> products = List.of(
      new Product(2030, 450, "Eggs"),
      new Product(2040, 456, "Eggs"),
      new Product(2100, 320, "Milk"),
      new Product(2101, 333, "Milk"),
      new Product(3230, 545, "Bread"),
      new Product(3231, 555, "Bread")
    );
    Cart myShoppingCart = new Cart(products);
    i.expectReturn(
        "myShoppingCart.numOfExpensiveItems(450)",
        () -> myShoppingCart.numOfExpensiveItems(450), 4l);
    i.expectReturn(
        "myShoppingCart.totalCost()",
        () -> myShoppingCart.totalCost(), 2659);
    i.expectReturn(
        "myShoppingCart.findByName(\"Milk\")",
        () -> myShoppingCart.findByName("Milk"), products.get(2));
    i.expectReturn(
        "myShoppingCart.findByName(\"Bread\")",
        () -> myShoppingCart.findByName("Bread"), products.get(4));
    i.expectReturn(
        "myShoppingCart.findByName(\"Tofu\")",
        () -> myShoppingCart.findByName("Tofu"), null);
    }
  }
}