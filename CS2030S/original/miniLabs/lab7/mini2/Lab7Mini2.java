import cs2030s.fp.Maybe;
import cs2030s.fp.Transformer;
import java.util.HashMap;
import java.util.Map;

class Lab7Mini2 {
  public static void main(String[] args) {
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
    System.out.println(NUSBank);
    NUSBank.transfer(1, 2, 50); // transfer $50 from account 1 to 2.
    System.out.println(NUSBank);
    NUSBank.transfer(1, 4, 50); // transfer $50 from account 1 to 4. (Failed, acc 4 is closed).
    System.out.println(NUSBank);
    NUSBank.transfer(3, 2, 150); // transfer $150 from account 3 to 2. (Failed, balance is not enough).
    System.out.println(NUSBank);
    NUSBank.transfer(9, 10, 150); // transfer $150 from account 9 to 10. (Failed, no such account exist).

    System.out.println(String.format("Total money in bank: %.2f", NUSBank.totalMoneyInBank()));
    System.out.println(String.format("All active accounts:\n%s", NUSBank.allAccounts()));
    System.out.println(String.format("Multi accounts:\n%s", NUSBank.multiAccounts()));
  }
}