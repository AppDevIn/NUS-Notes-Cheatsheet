/**
 * Test 1.
 */
class Test1 {
  private static void print(String s) {
    System.out.println("\n---- " + s + " ----");
  }

  /**
   * Main method for Test1.
   *
   * @param args Ignored and unused command line arguments.
   */
  public static void main(String[] args) {

    CS2030STest i = new CS2030STest();
    
    print("Test: get name of customer");
    {
      Customer c = new Customer("Han", "Millennium Falcon");
      i.expectReturn(
          "Customer c = new Customer(\"Han\", \"Millennium Falcon\");\n"
          + "c.getName();",
          () -> c.getName(), "Han");
    }

    print("Test: get address of customer");
    {
      Customer c = new Customer("Han", "Millennium Falcon");
      i.expectReturn(
          "Customer c = new Customer(\"Han\", \"Millennium Falcon\");\n"
          + "c.getAddress();",
          () -> c.getAddress(), "Millennium Falcon");
    }

    print("Test: Change name of customer");
    {
      Customer c1 = new Customer("Han", "Millennium Falcon");
      Customer c2 = c1.changeName("Chewie");
      i.expectReturn(
          "Customer c1 = new Customer(\"Han\", \"Millennium Falcon\");\n"
          + "Customer c2 = c1.changeName(\"Chewie\")\n"
          + "c1.getName();",
          () -> c1.getName(), "Han");
      i.expectReturn(
          "c2.getName();",
          () -> c2.getName(), "Chewie");
    }
    
    print("Test: Change address of customer");
    {
      Customer c1 = new Customer("Han", "Millennium Falcon");
      Customer c2 = c1.changeAddress("Tatooine");
      i.expectReturn(
          "Customer c1 = new Customer(\"Han\", \"Millennium Falcon\");\n"
          + "Customer c2 = c1.changeAddress(\"Tatooine\")\n"
          + "c1.getAddress();",
          () -> c1.getAddress(), "Millennium Falcon");
      i.expectReturn(
          "c2.getAddress();",
          () -> c2.getAddress(), "Tatooine");
    }
  }
}
