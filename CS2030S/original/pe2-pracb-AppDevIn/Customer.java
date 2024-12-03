/**
 * Represents a customer with a name and address.
 */
public class Customer {

  private String name;
  private String address;

  /**
   * Constructs a new Customer with the given name and address.
   *
   * @param name    the name of the customer
   * @param address the address of the customer
   */
  public Customer(String name, String address) {
    this.name = name;
    this.address = address;
  }

  /**
   * Changes the address of the customer to the given address.
   *
   * @param address the new address of the customer
   */
  public Customer changeAddress(String address) {
    this.address = address;
    return this;
  }

  /**
   * Changes the name of the customer to the given name.
   *
   * @param name the new name of the customer
   */
  public Customer changeName(String name) {
    this.name = name;
    return this;
  }

  /**
   * Returns the name of the customer.
   *
   * @return the name of the customer
   */
  public String getName() {
    return this.name;
  }

  /**
   * Returns the address of the customer.
   *
   * @return the address of the customer
   */
  public String getAddress() {
    return this.address;
  }

  /**
   * Returns a string representation of the customer.
   *
   * @return a string containing the name of the customer
   */
  @Override
  public String toString() {
    return this.name;
  }

  /**
   * Check if this customer equals to a given one.
   *
   * @return true if this customer equals to the given one; false otherwise.
   */
  @Override
  public boolean equals(Object o) {
    if (o instanceof Customer) {
      Customer c = (Customer) o;
      return (this.name.equals(c.name)) && (this.address.equals(c.address));
    }
    return false;
  }
}
