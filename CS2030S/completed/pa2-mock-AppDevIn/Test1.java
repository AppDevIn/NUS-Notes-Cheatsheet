/**
 * Test 1.
 */
class Test1 {
  /**
   * Main method for Test1.
   *
   * @param args Ignored and unused command line arguments.
   */
  public static void main(String[] args) {

    CS2030STest we = new CS2030STest(0);

    we.expectReturn("collatz(11)", () -> Q1.collatz(11), 14L);
    we.expectReturn("collatz(5)", () -> Q1.collatz(5), 5L);
    we.expectReturn("collatz(4)", () -> Q1.collatz(4), 2L);
    we.expectReturn("collatz(3)", () -> Q1.collatz(3), 7L);
    we.expectReturn("collatz(2)", () -> Q1.collatz(2), 1L);
    we.expectReturn("collatz(1)", () -> Q1.collatz(1), 0L);
  }
}
