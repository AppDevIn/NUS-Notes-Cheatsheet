import java.util.HashMap;
import java.util.Map;

class Test3 {

  private static Customer[] customers;
  private static Account[] accounts;

  private static void print(String s) {
    System.out.println("\n---- " + s + " ----");
  }

  private static Bank createBank() {
    customers = new Customer[] {
        new Customer("Han", "Millennium Falcon"),
        new Customer("Chewiee", "Millennium Falcon"),
        new Customer("Ah Beng", "Clementi")
    };

    accounts = new Account[] {
        new Account(2030, 100.0, customers[0]),
        new Account(4246, 150.0, customers[1]), 
        new Account(8790, 160.0, customers[2])
    };

    Map<Integer, Account> map = new HashMap<>();
    for (Account a : accounts) {
      map.put(a.getAccountNumber(), 
          new Account(a.getAccountNumber(), a.getBalance(), a.getOwners()));
    }

    return new Bank(map);
  }

  public static void main(String[] args) {

    CS2030STest i = new CS2030STest();

    print("Test: List of all accounts");
    {
      Bank c = createBank();
      System.out.println(c.allAccounts());
    }

    print("Test: Deposit into an account");
    {
      Bank c = createBank();
      c.deposit(2030, 30);
      i.expect(
          "Bank c = createBank();\n"
          + "c.deposit(2030, 30)",
          () -> c, new Bank(Map.of(
              2030, new Account(2030, 130.0, customers[0]), 
              4246, accounts[1], 
              8790, accounts[2])));
    }

    print("Test: Withdraw from account");
    {
      Bank c = createBank();
      c.withdraw(4246, 200);
      i.expect(
          "Bank c = createBank();\n"
          + "c.withdraw(4246, 200)",
          () -> c, new Bank(Map.of(
              2030, accounts[0],
              4246, new Account(4246, -50, customers[1]),
              8790, accounts[2])));
    }

    print("Test: Transfer from one account to another");
    {
      Bank c = createBank();
      c.transfer(2030, 8790, 90.0);
      i.expect(
          "Bank c = createBank();\n"
          + "c.transfer(2030, 8790, 90.0)",
          () -> c, new Bank(Map.of(
              2030, new Account(2030, 10, customers[0]),
              4246, accounts[1],
              8790, new Account(8790, 250, customers[2])
              )));
    }

    print("Test: Can't transfer if from account has negative balance");
    {
      Bank c = createBank();
      c.withdraw(4246, 200.0);
      c.transfer(4246, 8790, 100.0);
      i.expect(
          "Bank c = createBank();\n"
          + "c.withdraw(4246, 200.0)"
          + "c.transfer(4246, 8790, 100.0)",
          () -> c, new Bank(Map.of(
              2030, accounts[0],
              4246, new Account(4246, -50, customers[1]),
              8790, accounts[2]
              )));
    }

    print("Test: ok to transfer if to account has negative balance");
    {
      Bank c = createBank();
      c.withdraw(4246, 200.0);
      c.transfer(8790, 4246, 150.0);
      i.expect(
          "Bank c = createBank();\n"
          + "c.withdraw(4246, 200.0)"
          + "c.transfer(8790, 4246, 150.0)",
          () -> c, new Bank(Map.of(
              2030, accounts[0],
              4246, new Account(4246, 100, customers[1]),
              8790, new Account(8790, 10, customers[2])
              )));
    }

    print("Test: close an account");
    {
      Bank c = createBank();
      c.closeAccount(8790);
      i.expect(
          "Bank c = createBank();\n"
          + "c.closeAccount(8790)",
          () -> c, new Bank(Map.of(
              2030, accounts[0],
              4246, accounts[1],
              8790, new Account(8790, 160, true, customers[2])
              )));
    }

    print("Test: list of accounts after closing one");
    {
      Bank c = createBank();
      c.closeAccount(8790);
      i.expect(
          "Bank c = createBank();\n"
          + "c.closeAccount(8790)\n"
          + "c.allAccounts()",
          () -> c.allAccounts(), 
          "Chewiee | 4246 | 150.0\n"
          + "Han | 2030 | 100.0\n");
    }

    print("Test: calculate total in bank with closed account");
    {
      Bank c = createBank();
      c.closeAccount(8790);
      i.expect(
          "Bank c = createBank();\n"
          + "c.closeAccount(8790)\n"
          + "c.totalMoneyInBank()",
          () -> c.totalMoneyInBank(), 250.0);
    }

    print("Test: combine two accounts");
    {
      Bank c = createBank();
      c.closeAccount(8790);
      c.combineBankAccount(2030, 8790);
      i.expect(
          "Bank c = createBank();\n"
          + "c.closeAccount(8790)"
          + "c.combineBankAccount(2030, 8790)",
          () -> c, new Bank(Map.of(
              2030, new Account(2030, 260.0, customers[0], customers[2]),
              4246, accounts[1]
              )));
    }

    print("Test: Can't combine from an account that is not closed");
    {
      Bank c = createBank();
      c.combineBankAccount(2030, 8790);
      i.expect(
          "Bank c = createBank();\n"
          + "c.combineBankAccount(2030, 8790)",
          () -> c, new Bank(Map.of(
              2030, accounts[0],
              4246, accounts[1],
              8790, accounts[2]
              )));
    }

    print("Test: Can't combine into an account that is closed");
    {
      Bank c = createBank();
      c.closeAccount(2030);
      c.combineBankAccount(2030, 8790);
      i.expect(
          "Bank c = createBank();\n"
          + "c.closeAccount(2030)\n"
          + "c.combineBankAccount(2030, 8790)",
          () -> c, new Bank(Map.of(
              2030, new Account(2030, 100.0, true, customers[0]),
              4246, accounts[1],
              8790, accounts[2]
              )));
    }

    print("Test: Deposit into a non-existing account");
    {
      Bank c = createBank();
      c.deposit(1101, 80.0);
      i.expect(
          "Bank c = createBank();\n"
          + "c.deposit(1101, 80.0)",
          () -> c, new Bank(Map.of(
              2030, accounts[0],
              4246, accounts[1], 
              8790, accounts[2])));
    }

    print("Test: Witdraw from a non-existing account");
    {
      Bank c = createBank();
      c.withdraw(1101, 80.0);
      i.expect(
          "Bank c = createBank();\n"
          + "c.withdraw(1101, 80.0)",
          () -> c, new Bank(Map.of(
              2030, accounts[0],
              4246, accounts[1],
              8790, accounts[2])));
    }

    print("Test: Transfering from a non-existing account");
    {
      Bank c = createBank();
      c.transfer(1101, 2030, 80.0);
      i.expect(
          "Bank c = createBank();\n"
          + "c.transfer(1101, 2030, 80.0)",
          () -> c, new Bank(Map.of(
              2030, accounts[0],
              4246, accounts[1],
              8790, accounts[2]
              )));
    }

    print("Test: Transfering to a non-existing account");
    {
      Bank c = createBank();
      c.transfer(2030, 1101, 80.0);
      i.expect(
          "Bank c = createBank();\n"
          + "c.transfer(2030, 1101, 80.0)",
          () -> c, new Bank(Map.of(
              2030, accounts[0],
              4246, accounts[1],
              8790, accounts[2]
              )));
    }

    print("Test: Closing a non-existing account");
    {
      Bank c = createBank();
      c.closeAccount(1101);
      i.expect(
          "Bank c = createBank();\n"
          + "c.closeAccount(1101)",
          () -> c, new Bank(Map.of(
              2030, accounts[0],
              4246, accounts[1],
              8790, accounts[2]
              )));
    }

    print("Test: Combining a non-existing account");
    {
      Bank c = createBank();
      c.combineBankAccount(1101, 2030);
      i.expect(
          "Bank c = createBank();\n"
          + "c.combineBankAccount(1101, 2030)",
          () -> c, new Bank(Map.of(
              2030, accounts[0],
              4246, accounts[1],
              8790, accounts[2]
              )));
    }
  }
}
