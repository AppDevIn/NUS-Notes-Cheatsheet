import javax.swing.Box;

class Test4 {

  public static void main(String[] args) {
    CS2030STest i = new CS2030STest();

    i.expect("Some.some(4).map(new JackInTheBox<>())",
        Some.some(4).map(new JackInTheBox<>()), Some.some(Some.some(4)));

    i.expect("Some.some(Some.some(5)).map(new JackInTheBox<>())",
        Some.some(Some.some(5)).map(new JackInTheBox<>()), Some.some(Some.some(Some.some(5))));

    i.expect("Some.some(4).map(new JackInTheBox<>()).map(new JackInTheBox<>())",
        Some.some(4).map(new JackInTheBox<>()).map(new JackInTheBox<>()),
        Some.some(Some.some(Some.some(4))));
    
    i.expectCompile("Some<Some<Some<A>>> x = Some.some(Some.some(new A(4)))" +
        ".map(new JackInTheBox<>()) compiles",
        "Some<Some<Some<A>>> x = Some.some(Some.some(new A(4))).map(new JackInTheBox<>());",
        true);
  }
}
