import java.util.ArrayList;
import java.util.List;

/**
 * Represents a shopping Cart that contains a collection of Product objects.
 */
class Cart {
  private List<Product> items;

  /**
   * Constructs an empty cart.
   */
  public Cart() {
    this.items = new ArrayList<>();
  }

  /**
   * Constructs a new Cart with the specified items.
   *
   * @param items the items inside the shopping cart.
   */
  public Cart(List<Product> items) {
    this.items = items;
  }

  /**
   * Adds a product to the cart.
   *
   * @param p the product to be added.
   */
  void add(Product p) {
    this.items.add(p);
  }

  /**
   * Calculates the total cost of the items in the cart.
   *
   * @return the sum of the prices of all items in the cart
   */
  int totalCost() {
    int sum = 0;
    for (Product p : items) {
      sum = sum + p.getPrice();
    }
    return sum;
  }

  /**
   * Counts the number of items in the cart with a price equal to or greater than the given 
   * threshold.
   *
   * @param threshold the price threshold (in cents)
   * @return the number of items with a price equal to or greater than the threshold
   */
  long numOfExpensiveItems(int threshold) {
    long count = 0;
    for (Product p : items) {
      if (p.getPrice() >= threshold) {
        count += 1;
      }
    }
    return count;
  }

  /**
   * Finds a product in the cart by its name.
   *
   * @param name the name of the product
   * @return the product instance if found, or null if not found
   */
  Product findByName(String name) {
    for (Product p : this.items) {
      if (p.getName().equals(name)) {
        return p;
      }
    }
    return null;
  }
}