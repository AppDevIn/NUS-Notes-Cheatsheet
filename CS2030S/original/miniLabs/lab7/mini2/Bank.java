import cs2030s.fp.Maybe;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

class Bank {
  private Map<Integer, Account> accounts;

  /**
   * Constructs a new Bank with the given list of accounts and customers.
   *
   * @param accounts  a map of account numbers and their associated accounts
   * @param customers an list of customers
   */
  public Bank(Map<Integer, Account> accounts) {
    this.accounts = accounts;
  }

  /**
   * Returns a stream of all accounts in the bank.
   *
   * @return a stream containing all accounts in the bank
   */
  private Stream<Account> getAccountStream() {
    return accounts.values().stream();
  }

  /**
   * Returns the account with the specified account number.
   *
   * @param number the account number to search for
   * @return the Account with the specified account number, or null if not found
   */
  private Account findAccount(int number) {
    // Map::get returns null if the account does not exist
    return accounts.get(number);
  }

  /**
   * Transfers the specified amount of money between accounts.
   *
   * @param from   the account number to transfer money from
   * @param to     the account number to transfer money to
   * @param amount the amount of money to transfer
   */
  public void transfer(int from, int to, double amount) {
    // No need to change this
    Account fromAccount = findAccount(from);
    Account toAccount = findAccount(to);
    if (fromAccount != null && toAccount != null && fromAccount.getBalance() >= amount &&
        !fromAccount.isClosed() && !toAccount.isClosed()) {
      fromAccount.transferTo(toAccount, amount);
    }
  }

  /**
   * Calculates the total money in the bank across all accounts.
   *
   * @return the total money in the bank
   */
  public double totalMoneyInBank() {
    double total = 0;
    for (Account account : accounts.values()) {
      if (!account.isClosed()) {
        total += account.getBalance();
      }
    }
    return total;
  }

  /**
   * Return the details of all accounts in the bank as a string.
   */
  public String allAccounts() {
    ArrayList<Account> sortedAccounts = new ArrayList<>(accounts.values());
    // Double.compare(d1,d2) returns 0 if d1 is numerically equal to d2; 
    // a value less than 0 if d1 is numerically less than d2; 
    // and a value greater than 0 if d1 is numerically greater than d2
    sortedAccounts.sort((x, y) -> Double.compare(y.getBalance(), x.getBalance()));

    // Make it functional below
    String s = "";
    for (Account account : sortedAccounts) {
      if (!account.isClosed()) {
        s += account + "\n";
      }
    }
    return s;
  }

  /**
   * Return a map that contains accounts that belong to someone who has multiple accounts.
   * 
   * @return a map that contains (accountNumber, Account)
   */
  public Map<Integer, Account> multiAccounts() {
    Map<String, Integer> nameCount = new HashMap<>();
    Map<Integer, Account> duplicates = new HashMap<>();

    // Make the loop below functional
    for (Account account: accounts.values()) {
      nameCount.put(account.getOwner(), nameCount.getOrDefault(account.getOwner(), 0) + 1);
    }

    // Make the loop below functional
    for (Account account: accounts.values()) {
      if (nameCount.get(account.getOwner()) > 1) {
        duplicates.put(account.getAccountNumber(), account);
      }
    }
    
    // Do not change below
    return duplicates;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("Bank Status:\n");
    this.accounts.forEach((k, v) -> sb.append(v + "\n"));
    return sb.toString();
  }
}