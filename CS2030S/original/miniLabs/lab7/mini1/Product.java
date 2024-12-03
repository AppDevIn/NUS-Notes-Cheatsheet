/**
 * Represents a Product with a unique id, name, and price.
 */
class Product {
  private int id;
  private int price;
  private String name;

  /**
   * Constructs a new Product with the specified id, price, and name.
   *
   * @param id the unique identifier of the product
   * @param price the price of the product in cents
   * @param name the name of the product
   */
  public Product(int id, int price, String name) {
    this.id = id;
    this.price = price;
    this.name = name;
  }

  /**
   * Gets the product ID.
   *
   * @return the unique identifier of the product
   */
  public int getID() {
    return this.id;
  }

  /**
   * Gets the product name.
   *
   * @return the name of the product
   */
  public String getName() {
    return this.name;
  }

  /**
   * Gets the product price in cents.
   *
   * @return the price of the product
   */
  public int getPrice() {
    return this.price;
  }

  /**
   * Returns a string representation of the product.
   *
   * @return a string containing the product id, name, and price
   */
  @Override
  public String toString() {
    return "[" + this.id + ", " + this.name + ", $" + 
        String.format("%.2f", this.price / 100.0) + "]";
  }

  /**
   * Compares this product to the specified object.
   *
   * @param o the object to compare this product against
   * @return true if the given object represents a Product with the same id,
   *      name, and price; false otherwise
   */
  @Override
  public boolean equals(Object o) {
    if (o instanceof Product && o != null) {
      Product p = (Product) o;
      return this.id == p.id &&
        this.name.equals(p.name) &&
        this.price == p.price;
    }
    return false;
  }
}