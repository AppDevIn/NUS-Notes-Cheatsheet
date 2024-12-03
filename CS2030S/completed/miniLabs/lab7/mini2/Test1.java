import java.util.List;
import java.util.HashMap;
import java.util.Map;

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
    Account[] accounts = new Account[] {
      new Account(1, "Alice", 100.0),
      new Account(2, "Bob", 150.0),
      new Account(3, "Charlie", 90.0),
      new Account(4, "Alice", 70.0, true)
    };

    Map<Integer, Account> map = new HashMap<>();
    for (Account a : accounts) {
      map.put(a.getAccountNumber(), 
          new Account(a.getAccountNumber(), a.getOwner(), a.getBalance(), a.isClosed()));
    }

    Bank NUSBank = new Bank(map);

    NUSBank.transfer(1, 2, 50); // transfer $50 from account 1 to 2.
    print("NUSBank.transfer(1, 2, 50)");
    i.expectReturn(
        "Account 1 balance: 50",
        () -> map.get(1).getBalance(), 50.0);
    i.expectReturn(
        "Account 2 balance: 200",
        () -> map.get(2).getBalance(), 200.0);

    NUSBank.transfer(1, 4, 50); // transfer $50 from account 1 to 4. (Failed, acc 4 is closed).
    print("NUSBank.transfer(1, 4, 50) should fail");
    i.expectReturn(
        "Account 1 balance: 50",
        () -> map.get(1).getBalance(), 50.0);
    i.expectReturn(
        "Account 4 balance: 70",
        () -> map.get(4).getBalance(), 70.0);

    NUSBank.transfer(3, 2, 150); // transfer $150 from account 3 to 2. (Failed, balance is not enough).
    print("NUSBank.transfer(3, 2, 150) should fail");
    i.expectReturn(
        "Account 2 balance: 50",
        () -> map.get(2).getBalance(), 200.0);
    i.expectReturn(
        "Account 3 balance: 70",
        () -> map.get(3).getBalance(), 90.0);

    NUSBank.transfer(9, 10, 150); // transfer $150 from account 9 to 10. (Failed, no such account exist).

    print("totalMoneyInBank");
    i.expectReturn(
        "NUSBank.totalMoneyInBank()",
        () -> NUSBank.totalMoneyInBank(), 340.0);

    print("allAccounts");
    i.expectReturn(
        "NUSBank.allAccounts()",
        () -> NUSBank.allAccounts(), "Acc ID: 2, balance: 200.00\nAcc ID: 3, balance: 90.00\nAcc ID: 1, balance: 50.00\n");

    print("multiAccounts()");
    i.expectReturn(
        "NUSBank.multiAccounts()",
        () -> NUSBank.multiAccounts(), Map.of(1, map.get(1), 4, map.get(4)));
    }
  }
}