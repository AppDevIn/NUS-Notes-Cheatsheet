/**
 * Represents a bank account.
 */
public class Account {

  private int accountNumber;
  private String owner;
  private double balance;
  private boolean isClosed;

  /**
   * Constructs a new Account with the given account number, and balance.
   *
   * @param accountNumber  the unique account number
   * @param owner  the name of the account owner
   * @param balance the initial balance of the account
   */
  public Account(int accountNumber, String owner, double balance) {
    this.accountNumber = accountNumber;
    this.owner = owner;
    this.balance = balance;
    this.isClosed = false;
  }

  /**
   * Constructs a new Account with the given account number, balance, and its current status.
   *
   * @param accountNumber  the unique account number
   * @param owner the name of the account owner
   * @param balance the initial balance of the account
   * @param isClosed the status of the account
   */
  public Account(int accountNumber, String owner, double balance, boolean isClosed) {
    this.accountNumber = accountNumber;
    this.owner = owner;
    this.balance = balance;
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
   * Returns the owner's name.
   *
   * @return the owner's name.
   */
  public String getOwner() {
    return this.owner;
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
   * Mark this account as close.
   */
  public Account close() {
    this.isClosed = true;
    return this;
  }

  /**
   * Check if this account is closed.
   */
  public boolean isClosed() {
    return this.isClosed;
  }
  
  public String toString() {
    return String.format("Acc ID: %d, balance: %.2f", this.accountNumber, this.balance) +
      (this.isClosed ? " [Closed]" : "");
  }
}