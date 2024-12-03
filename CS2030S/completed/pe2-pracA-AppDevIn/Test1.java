class Test1 {
  public static void main(String[] args) {
    CS2030STest we = new CS2030STest();

    we.expectCompile("Checking Perishable is not instantiatable", 
                    "new Perishable();",
                    false);

    we.expectCompile("Checking Named is not instantiatable", 
                    "new Named();",
                    false);

    we.expect("Creating a new Fruit(() -> \"Apple\", () -> 5)",
        new Fruit(() -> "Apple", () -> 5).toString(),
        "Apple (expires in 5 days)");

    we.expect("Testing Fruit::getName",
        new Fruit(() -> "Apple", () -> 5).getName(),
        "Apple");

    we.expect("Testing Fruit::getDaysToExpiry",
        new Fruit(() -> "Apple", () -> 5).getDaysToExpiry(),
        5);

    we.expect("Creating a new Fruit(() -> \"Orange\", () -> 3)",
        new Fruit(() -> "Orange", () -> 3).toString(),
        "Orange (expires in 3 days)");
  }
}
