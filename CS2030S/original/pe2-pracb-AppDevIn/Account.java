import java.util.Arrays;

/**
 * Represents a bank account with multiple account owners.
 */
public class Account {

  private Customer[] owners;
  private int accountNumber;
  private double balance;
  private boolean isClosed;

  /**
   * Constructs a new Account with the given account number, balance, and list of account owners.
   *
   * @param accountNumber  the unique account number
   * @param balance the initial balance of the account
   * @param owners  the list of account owners
   */
  public Account(int accountNumber, double balance, Customer... owners) {
    this.accountNumber = accountNumber;
    this.balance = balance;
    this.owners = owners;
    this.isClosed = false;
  }

  /**
   * Constructs a new Account with the given account number, balance, and list of account owners.
   *
   * @param accountNumber  the unique account number
   * @param balance the initial balance of the account
   * @param owners  the list of account owners
   */
  public Account(int accountNumber, double balance, boolean isClosed, Customer... owners) {
    this.accountNumber = accountNumber;
    this.balance = balance;
    this.owners = owners;
    this.isClosed = isClosed;
  }

  /**
   * Returns the account number.
   *
   * @return the account number
   */
  public int getAccountNumber() {
    return this.accountNumber;
  }

  /**
   * Returns the account balance.
   *
   * @return the account balance
   */
  public double getBalance() {
    return this.balance;
  }

  /**
   * Deposits the given amount into the account.
   *
   * @param amount the amount to be deposited
   * @return this account
   */
  public Account deposit(double amount) {
    this.balance += amount;
    return this;
  }

  /**
   * Withdraws the given amount from the account.
   *
   * @param amount the amount to be withdrawn
   * @return this account
   */
  public Account withdraw(double amount) {
    this.balance -= amount;
    return this;
  }

  /**
   * Returns the owners of the account.
   *
   * @return An array containing the owners of the account
   */
  public Customer[] getOwners() {
    return this.owners;
  }

  /**
   * Transfers the given amount from this account to another account.
   *
   * @param toAccount the account to transfer the amount to
   * @param amount    the amount to be transferred
   * @return a pair of accounts: this and the account transferred to
   */
  public Pair<Account, Account> transferTo(Account toAccount, double amount) {
    toAccount.deposit(amount);
    this.withdraw(amount);
    return new Pair<>(this, toAccount);
  }

  /**
   * Combines this account with another account by merging their balances and account owners.
   *
   * @param other the other account to combine with
   * @return a new Account with the combined balance and list of account owners
   */
  public Account combine(Account other) {
    this.balance += other.balance;
    this.owners = concat(this.owners, other.owners);
    return this;
  }

  /**
   * Concatenates two arrays of Customers.
   *
   * @param a the first array of Customers
   * @param b the second array of Customers
   * @return a new array containing all the elements of a followed by all the elements of b
   */
  private Customer[] concat(Customer[] arr1, Customer[] arr2) {
    int len1 = arr1.length;
    int len2 = arr2.length;

    Customer[] c = new Customer[len1 + len2];
    System.arraycopy(arr1, 0, c, 0, len1);
    System.arraycopy(arr2, 0, c, len1, len2);
    return c;
  }

  /**
   * Mark this account as close.
   */
  public Account close() {
    this.isClosed = true;
    return this;
  }

  /**
   * Check if this account is close.
   */
  public boolean isClosed() {
    return this.isClosed;
  }

  /**
   * Returns a string representation of the account.
   *
   * @return a string containing the account owners, account number, and account balance
   */
  @Override
  public String toString() {
    String returnString = "";
    for (int i = 0; i < this.owners.length - 1; i++) {
      returnString += this.owners[i] + ", ";
    }
    returnString += this.owners[this.owners.length - 1];
    returnString += " | " + this.accountNumber
        + " | " + this.balance;
    if (this.isClosed) {
      returnString += "[closed]";
    }
    return returnString;
  }

  /**
   * Checks if this account equals to another
   *
   * @return true if the given object o equals to this.  Otherwise return false
   */
  @Override
  public boolean equals(Object o) {
    if (o instanceof Account) {
      Account a = (Account) o;
      return a.accountNumber == this.accountNumber && 
        a.balance == this.balance && 
        a.isClosed == this.isClosed && 
        // Compare if the elements of the two arrays are equals.
        Arrays.equals(a.owners, this.owners);
    }
    return false;
  }
}
