class Test2 {

  public static void main(String[] args) {

    class AddOne implements Transformer<Integer, Integer> {
      @Override
      public Integer transform(Integer t) { 
        return t + 1; 
      }
    }

    class StrLen implements Transformer<String, Integer> {
      @Override
      public Integer transform(String t) { 
        return t.length(); 
      }
    }

    AddOne fn1 = new AddOne();
    StrLen fn2 = new StrLen();

    CS2030STest i = new CS2030STest();

    i.expect("Some.some(4).<Integer>map(fn1) === Some.some(5)",
        Some.some(4).<Integer>map(fn1), Some.some(5));

    i.expect("Some.some(5).map(fn1) === Some.some(6)",
        Some.some(5).map(fn1), Some.some(6));

    Some<Number> six = Some.some(4).map(fn1).map(fn1);

    i.expect("Some<Number> six = Some.some(4).map(fn1).map(fn1) === Some.<Number>some(6)", 
        six, Some.<Number>some(6));

    i.expectCompile("six.map(fn2) fails compilation",
        "Some.<Number>some(6).map(new Transformer<String, Integer>() {" +
        "  public Integer transform(String arg) { return arg.length(); }" +
        "})", 
        false);

    Some<String> mod = Some.some("CS2030S");

    i.expect("Some<String> mod = Some.some(\"CS2030S\") === [CS2030S]", 
        mod.toString(), "[CS2030S]");

    i.expect("mod.map(fn2) === Some.some(7)", 
        mod.map(fn2), Some.some(7));

    i.expect("mod is unchanged", 
        mod.toString(), "[CS2030S]");

    i.expect("mod.map(fn2).map(fn1) === Some.some(8)", 
        mod.map(fn2).map(fn1), Some.some(8));
  }
}
