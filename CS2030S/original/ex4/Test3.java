class Test3 {

  public static void main(String[] args) {

    class AtoC implements Transformer<A, C> {
      @Override
      public C transform(A arg) {
        return new C(arg.get());
      }
    }

    class BtoB implements Transformer<B, B> {
      @Override
      public B transform(B arg) {
        return new B(arg.get());
      }
    }
    
    class CtoA implements Transformer<C, A> {
      @Override
      public A transform(C arg) {
        return new A(arg.get());
      }
    }
   
    Some<A> someA = Some.some(new A(1));
    Some<B> someB = Some.some(new B(2));
    Some<C> someC = Some.some(new C(3));
    AtoC fn1 = new AtoC();
    BtoB fn2 = new BtoB();
    CtoA fn3 = new CtoA();

    CS2030STest i = new CS2030STest();

    i.expectCompile("someA.map(fn1) compiles",
        "Some.some(new A(1)).map(new Transformer<A, C>() {" +
        "  public C transform(A a) { return new C(a.get()); }" +
        "})", 
        true);

    i.expectCompile("someA.map(fn2) fails compilation",
        "Some.some(new A(1)).map(new Transformer<B, B>() {" +
        "  public B transform(B a) { return new B(a.get()); }" +
        "})", 
        false);

    i.expectCompile("someA.map(fn3) fails compilation",
        "Some.some(new A(1)).map(new Transformer<C, A>() {" +
        "  public A transform(C a) { return new A(a.get()); }" +
        "})", 
        false);

    i.expectCompile("someB.map(fn1) compiles",
        "Some.some(new B(1)).map(new Transformer<A, C>() {" +
        "  public C transform(A a) { return new C(a.get()); }" +
        "})", 
        true);

    i.expectCompile("someB.map(fn2) compiles",
        "Some.some(new B(1)).map(new Transformer<B, B>() {" +
        "  public B transform(B a) { return new B(a.get()); }" +
        "})", 
        true);

    i.expectCompile("someB.map(fn3) fails compilation",
        "Some.some(new B(1)).map(new Transformer<C, A>() {" +
        "  public A transform(C a) { return new A(a.get()); }" +
        "})", 
        false);

    i.expectCompile("someC.map(fn1) compiles",
        "Some.some(new B(1)).map(new Transformer<A, C>() {" +
        "  public C transform(A a) { return new C(a.get()); }" +
        "})", 
        true);

    i.expectCompile("someC.map(fn2) compiles",
        "Some.some(new C(1)).map(new Transformer<B, B>() {" +
        "  public B transform(B a) { return new B(a.get()); }" +
        "})", 
        true);

    i.expectCompile("someC.map(fn3) compiles",
        "Some.some(new C(1)).map(new Transformer<C, A>() {" +
        "  public A transform(C a) { return new A(a.get()); }" +
        "})", 
        true);
  }
}
