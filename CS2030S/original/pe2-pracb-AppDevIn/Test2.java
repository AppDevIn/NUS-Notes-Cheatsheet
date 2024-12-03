import java.util.List;

/**
 * Test 2.
 */
class Test2 {

  private static void print(String s) {
    System.out.println("\n---- " + s + " ----");
  }

  /**
   * Main method for Test2.
   *
   * @param args Ignored and unused command line arguments.
   */
  public static void main(String[] args) {

    CS2030STest i = new CS2030STest();

    print("Test: Getting the account number");
    {
      Customer c = new Customer("Han", "Millennium Falcon");
      Account a = new Account(5, 100.0, c);
      i.expectReturn(
          "Customer c = new Customer(\"Han\", \"Millennium Falcon\");\n"
          + "Account a = new Account(5, 100.0, c);\n"
          + "a.getAccountNumber();",
          () -> a.getAccountNumber(), 5);
    }

    print("Test: Getting the account balance");
    {
      Customer c = new Customer("Han", "Millennium Falcon");
      Account a = new Account(5, 100.0, c);
      i.expectReturn(
          "Customer c = new Customer(\"Han\", \"Millennium Falcon\");\n"
          + "Account a = new Account(5, 100.0, c);\n"
          + "a.getBalance();",
          () -> a.getBalance(), 100.0);
    }

    print("Test: Depositing and getting the account balance");
    {
      Customer c = new Customer("Han", "Millennium Falcon");
      Account a1 = new Account(5, 100.0, c);
      Account a2 = a1.deposit(50.0);
      i.expectReturn(
          "Customer c = new Customer(\"Han\", \"Millennium Falcon\");\n"
          + "Account a1 = new Account(5, 100.0, c);\n"
          + "Account a2 = a1.deposit(50.0);\n"
          + "a1.getBalance();",
          () -> a1.getBalance(), 100.0);
      i.expectReturn(
          "a2.getBalance();",
          () -> a2.getBalance(), 150.0);
    }

    print("Test: Withdrawing and getting the account balance");
    {
      Customer c = new Customer("Han", "Millennium Falcon");
      Account a1 = new Account(5, 100.0, c);
      Account a2 = a1.withdraw(60.0);
      i.expectReturn(
          "Customer c = new Customer(\"Han\", \"Millennium Falcon\");\n"
          + "Account a1 = new Account(5, 100.0, c1);\n"
          + "Account a2 = a1.withdraw(60.0);\n"
          + "a1.getBalance();",
          () -> a1.getBalance(), 100.0);
      i.expectReturn(
          "a2.getBalance();",
          () -> a2.getBalance(), 40.0);
    }

    print("Test: Transferring from one account to another");
    {
      Customer c1 = new Customer("Han", "Millennium Falcon");
      Customer c2 = new Customer("Chewie", "Millennium Falcon");
      Account a1 = new Account(5, 100.0, c1);
      Account a2 = new Account(10, 150.0, c2); 
      Pair<Account, Account> pair = a1.transferTo(a2, 50);
      Account a3 = pair.getFirst();
      Account a4 = pair.getSecond();
      i.expectReturn(
          "Customer c1 = new Customer(\"Han\", \"Millennium Falcon\");\n"
          + "Customer c2 = new Customer(\"Chewie\", \"Millennium Falco\");\n"
          + "Account a1 = new Account(5, 100.0, c1);\n"
          + "Account a2 = new Account(10, 150.0, c2);\n"
          + "Pair<Account, Account> pair = a1.transferTo(a2, 50);\n"
          + "Account a3 = pair.getFirst();\n"
          + "Account a4 = pair.getSecond();\n"
          + "a1.getBalance();",
          () -> a1.getBalance(), 100.0);
      i.expectReturn(
          "a2.getBalance();", 
          () -> a2.getBalance(), 150.0);
      i.expectReturn(
          "a3.getBalance();", 
          () -> a3.getBalance(), 50.0);
      i.expectReturn(
          "a4.getBalance();", 
          () -> a4.getBalance(), 200.0);
    }

    print("Test: Combining two accounts");
    {
      Customer c1 = new Customer("Han", "Millennium Falcon");
      Customer c2 = new Customer("Chewie", "Millennium Falcon");
      Account a1 = new Account(5, 100.0, c1);
      Account a2 = new Account(10, 150.0, c2); 
      Account a3 = a1.combine(a2);
      i.expectReturn(
          "Customer c1 = new Customer(\"Han\", \"Millennium Falcon\");\n"
          + "Customer c2 = new Customer(\"Chewie\", \"Millennium Falcon\");\n"
          + "Account a1 = new Account(5, 100.0, c1);\n"
          + "Account a2 = new Account(10, 150.0, c2);\n"
          + "Account a3 = a1.combine(a2);\n"
          + "a1.getBalance();",
          () -> a1.getBalance(), 100.0);  
      i.expectReturn(
          "a2.getBalance();",
          () -> a2.getBalance(), 150.0);  
      i.expectReturn(
          "a3.getBalance();",
          () -> a3.getBalance(), 250.0);  
      i.expectReturn(
          "a3.getAccountNumber();",
          () -> a3.getAccountNumber(), 5);  
      i.expectReturn(
          "a3.getOwners();",
          () -> List.of(a3.getOwners()), List.of(c1, c2));
    }
  }
}
