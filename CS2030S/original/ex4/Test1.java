class Test1 {

  public static void main(String[] args) {

    CS2030STest i = new CS2030STest();

    // Test Some some and toString
    i.expect("Some.<Integer>some(4)",
        Some.<Integer>some(4).toString(), "[4]");

    // Test Some some and toString with type inference
    i.expect("Some.some(4)",
        Some.some(4).toString(), "[4]");

    // Test equals
    i.expect("Some.some(4).equals(4)",
        Some.some(4).equals(4), false);

    // Test equals
    i.expect("Some.some(Some.some(0)).equals(Some.some(Some.some(0)))",
        Some.some(Some.some(0)).equals(Some.some(Some.some(0))), true);

    // Test equals
    i.expect("Some.some(Some.some(0)).equals(Some.some(0))",
        Some.some(Some.some(0)).equals(Some.some(0)), false);

    // Test equals
    i.expect("Some.some(0).equals(Some.some(Some.some(0)))",
        Some.some(0).equals(Some.some(Some.some(0))), false);

    // Test Some some and toString
    i.expect("Some.some(\"body once told me\")",
        Some.some("body once told me").toString(), "[body once told me]");

    // Test equals
    i.expect("Some.some(Some.some(0)).equals(Some.some(0))",
        Some.some(Some.some(0)).equals(Some.some(0)), false);
    
    A a11 = new A(1);
    A a12 = new B(1);
    A a13 = new C(1);

    A a2 = new B(2);

    // Test equals
    i.expect("Some.some(a11).equals(Some.some(a12))",
        Some.some(a11).equals(Some.some(a12)), true);

    // Test equals
    i.expect("Some.some(a11).equals(Some.some(a13))",
        Some.some(a11).equals(Some.some(a13)), true);

    // Test equals
    i.expect("Some.some(a13).equals(Some.some(a12))",
        Some.some(a13).equals(Some.some(a12)), true);

    // Test equals
    i.expect("Some.some(a11).equals(Some.some(a2))",
        Some.some(a11).equals(Some.some(a2)), false);

    // Test equals
    i.expect("Some.some(a12).equals(Some.some(a2))",
        Some.some(a11).equals(Some.some(a2)), false);

    // Test equals
    i.expect("Some.some(a13).equals(Some.some(a22))",
        Some.some(a13).equals(Some.some(a2)), false);
  }
}
