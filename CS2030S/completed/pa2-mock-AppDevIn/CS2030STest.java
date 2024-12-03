import java.net.URI;
import java.util.List;
import java.util.function.Supplier;
import javax.tools.DiagnosticCollector;
import javax.tools.SimpleJavaFileObject;
import javax.tools.ToolProvider;

/**
 * A helper class to test CS2030S labs.
 */
class CS2030STest {

  private static final String ANSI_RESET = "\u001B[0m";
  private static final String ANSI_RED = "\u001B[41m";
  private static final String ANSI_GREEN = "\u001B[1m\u001B[32m";
  private static final boolean DEBUG = true;

  private int pass;
  private int fail;
  private int mark;

  private int localPass;
  private int localFail;

  public CS2030STest(int mark) {
    this.pass = 0;
    this.fail = 0;
    this.mark = mark;
  }

  public static void dprintln(String msg) {
    if (CS2030STest.DEBUG) {
      System.out.println(msg);
    }
  }

  public static void dprint(String msg) {
    if (CS2030STest.DEBUG) {
      System.out.print(msg);
    }
  }

  /**
   * Test if two objects are equals.
   *
   * @param test A description of the test.
   * @param output The output from an expression.
   * @param expect The expected output from that expression.
   * @return this object.
   */
  public CS2030STest expect(String test, Object output, Object expect) {
    System.out.print(test);
    if ((expect == null && output == null) || output.equals(expect)) {
      dprintln(".. " + ANSI_GREEN + "ok" + ANSI_RESET);
      this.pass++;
      this.localPass++;
    } else {
      dprintln(".. " + ANSI_RED + "failed" + ANSI_RESET);
      dprintln("  expected: " + expect);
      dprintln("  got this: " + output);
      this.fail++;
      this.localFail++;
    }
    return this;
  }

  /**
   * Test if a given supplier produces a given object.
   *
   * @param <T> The type of object the given task will produce.
   * @param test A description of the test.
   * @param task The task to run.
   * @param expect The expected output from that expression.
   * @return this object.
   */
  public <T> CS2030STest expect(String test, Supplier<T> task, Object expect) {
    dprint(test);
    try {
      T output = task.get();
      if ((expect == null && output == null) || output.equals(expect)) {
        dprintln(".. " + ANSI_GREEN + "ok" + ANSI_RESET);
        this.pass++;
        this.localPass++;
      } else {
        dprintln(".. " + ANSI_RED + "failed" + ANSI_RESET);
        dprintln("  expected: " + expect);
        dprintln("  got this: " + output);
        this.fail++;
        this.localFail++;
      }
    } catch (Exception e) {
      dprintln(".. " + ANSI_RED + "failed" + ANSI_RESET);
      dprintln("  with exception: " + e.getMessage());
      e.printStackTrace();
      this.fail++;
      this.localFail++;
    }
    return this;
  }

  /**
   * Test if a given producer returns a value.  Wrapper around expect(..)
   * to simplify caller.
   *
   * @param <T> The type of object the given task will produce.
   * @param test A description of the test.
   * @param task The task to run.
   * @param expect The expected output from that expression.
   * @return this object.
   */
  public <T> CS2030STest expectReturn(String test, Supplier<T> task, Object expect) {
    return this.expect(test + " returns " + expect, task, expect);
  }

  /**
   * Test if an expression throws an exception.
   *
   * @param test A description of the test.
   * @param task A lambda expression of the expression.
   * @param expectedE A exception instance of the same type as the expected one.
   * @return this object.
   */
  public CS2030STest expectException(String test, Runnable task, Exception expectedE) {
    dprint(test + " throws " + expectedE.getClass().getSimpleName());
    boolean gotException = false;
    try {
      task.run();
    } catch (Exception e) {
      if (e.getClass().equals(expectedE.getClass())) {
        gotException = true;
      }
    }
    if (gotException) {
      dprintln(".. " + ANSI_GREEN + "ok" + ANSI_RESET);
      this.pass++;
      this.localPass++;
    } else {
      dprintln(".. " + ANSI_RED + "failed" + ANSI_RESET);
      dprintln("  did not catch expected exception " + expectedE.getClass());
      this.fail++;
      this.localFail++;
    }
    return this;
  }

  /**
   * Test if an expression compiles with/without error.
   *
   * @param test A description of the test.
   * @param statement The java statement to compile
   * @param success Whether the statement is expected to compile or not 
   *     (true if yes; false otherwise)
   * @return this object.
   */
  public CS2030STest expectCompile(String test, String statement, boolean success) {
    dprint(test);

    class JavaSourceFromString extends SimpleJavaFileObject {
      final String code;

      JavaSourceFromString(String code) {
        super(URI.create("string:///TempClass.java"), Kind.SOURCE);
        this.code = "class TempClass {void foo(){" +  code + ";}}";
      }

      @Override
      public CharSequence getCharContent(boolean ignoreEncodingErrors) {
        return code;
      }
    }

    boolean noError = ToolProvider
        .getSystemJavaCompiler()
        //.getTask(null, null, new DiagnosticCollector<>(), null, null, 
        .getTask(null, null, null, null, null, 
            List.of(new JavaSourceFromString(statement)))
        .call();

    if (noError != success) {
      dprintln(".. " + ANSI_RED + "failed" + ANSI_RESET);
      this.fail++;
      this.localFail++;
      if (!success) {
        dprintln("  expected compilation error but it compiles fine.");
      } else {
        dprintln("  expected the statement to compile without errors but it does not.");
      }
    } else {
      dprintln(".. " + ANSI_GREEN + "ok" + ANSI_RESET);
      this.pass++;
      this.localPass++;
    }
    return this;
  }
  
  public void wantLocalSummary() {
    dprintln("PASS : " + this.localPass);
    dprintln("FAIL : " + this.localFail);
    dprintln("RATE = " + String.format("%.2f", (this.localPass * 100.0)/(this.localPass + this.localFail)) + "%");

    this.localPass = 0;
    this.localFail = 0;
  }
  
  public void wantSummary() {
    dprintln("PASS : " + this.pass + "/" + (this.pass + this.fail));
    dprintln("FAIL : " + this.fail + "/" + (this.pass + this.fail));

    if (!CS2030STest.DEBUG) {
      System.out.println(this.fail == 0 ? this.mark : 0);
    }
  }
}
