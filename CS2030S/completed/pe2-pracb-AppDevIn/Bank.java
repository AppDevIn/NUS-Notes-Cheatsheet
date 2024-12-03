import cs2030s.fp.Maybe;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Represents a bank with multiple accounts and customers.
 */
public class Bank {

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
  private Maybe<Account> findAccount(int number) {
    // Map::get returns null if the account does not exist
    return Maybe.of(this.accounts.values().stream()
            .filter(x -> x.getAccountNumber() == number)
            .limit(1)
            .reduce(null, (id, acc) -> acc));
  }

  /**
   * Calculates the total money in the bank across all accounts.
   *
   * @return the total money in the bank
   */
  public double totalMoneyInBank() {
    return this.accounts.values().stream()
            .filter(acc -> !acc.isClosed())
            .reduce(0.0, (id, acc) -> id + acc.getBalance(), (id, acc) -> id + acc);
  }

  /**
   * Transfers the specified amount of money between accounts.
   *
   * @param from   the account number to transfer money from
   * @param to     the account number to transfer money to
   * @param amount the amount of money to transfer
   */
  public void transfer(int from, int to, double amount) {

    Maybe<Account> fromAccount = findAccount(from);
    Maybe<Account> toAccount = findAccount(to);

    Maybe<Pair<Account, Account>> newAccounts = Maybe.of(fromAccount.filter(a -> !a.isClosed() && a.getBalance() > 0).map(f ->
            toAccount.filter(a -> !a.isClosed())
                    .map(t -> f.transferTo(t, amount))
                    .orElse(null)
    ).orElse(null));

    newAccounts.ifPresent(a -> this.accounts.put(from, a.getFirst()));
    newAccounts.ifPresent(b -> this.accounts.put(to, b.getSecond()));


  }

  /**
   * Deposits the specified amount of money into the specified account.
   *
   * @param number the account number to deposit money into
   * @param amount the amount of money to deposit
   */
  public void deposit(int number, double amount) {
    this.findAccount(number).filter(a -> !a.isClosed()).ifPresent(acc -> this.accounts.put(number, acc.deposit(amount)));
  }

  /**
   * Withdraws the specified amount of money from the specified account.
   *
   * @param number the account number to withdraw money from
   * @param amount the amount of money to withdraw
   */
  public void withdraw(int number, double amount) {
    this.findAccount(number).filter(a -> !a.isClosed()).ifPresent(acc -> this.accounts.put(number, acc.withdraw(amount)));
  }

  /**
   * Combines two accounts and updates the accounts list.
   *
   * @param a the account number of the first account to combine
   * @param b the account number of the second account to combine
   */
  public void combineBankAccount(int a, int b) {
    Maybe<Account> accountA = findAccount(a).filter(acc -> !acc.isClosed());
    Maybe<Account> accountB = findAccount(b).filter(Account::isClosed);

    accountA.ifPresent(accA -> accountB.ifPresent(accB ->
            this.accounts.put(a, accA.combine(accB))
            ));
    Maybe<Account> newAccount = accountA.flatMap(accA ->
            Maybe.of(accountB.map(accA::combine).orElse(null))
    );

    newAccount.ifPresent(acc -> accounts.remove(b));
    newAccount.ifPresent(acc -> accounts.put(a, acc));
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
    String s = "";
    for (Account account : sortedAccounts) {
      if (!account.isClosed()) {
        s += account + "\n";
      }
    }
    return this.accounts.values().stream()
            .sorted((x,y) -> Double.compare(y.getBalance(), x.getBalance()))
            .filter(a -> !a.isClosed())
            .map(Account::toString)
            .reduce("", (id, acc) -> id + acc + "\n");
  }

  /**
   * Closes the account with the specified account number and removes it from the accounts list.
   *
   * @param number the account number of the account to close
   */
  public void closeAccount(int number) {
    this.findAccount(number).ifPresent(acc -> this.accounts.put(number, acc.close()));
  }

  /**
   * Checks if this bank is equals to another bank, by comparing the accounts.
   *
   * @param o the object to compare against
   * @return true if the two banks are equal; false otherwise.
   */
  @Override
  public boolean equals(Object o) {
    if (o instanceof Bank) {
      Bank c = (Bank) o;
      return c.accounts.equals(this.accounts);
    }
    return false;
  }

  /**
   * Return a string representation of the map of accounts.
   *
   * @return a string representation of this bank.
   */
  @Override
  public String toString() {
    return this.accounts.toString();
  }
}
