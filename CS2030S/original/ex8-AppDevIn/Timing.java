import java.io.FileNotFoundException;
import java.time.Duration;
import java.time.Instant;

public class Timing {
  /**
   * The program compares the timing between sequential
   * and parallel computation.
   * 
   * @param args Command line arguments
   */
  public static void main(String[] args) {
    try {
      long seq = Timing.time(new Sequential());
      long par = Timing.time(new Parallel());
      System.out.println("Sequential: " + seq + "ms");
      System.out.println("WithCF    : " + par + "ms");
      System.out.println("  Speedup : " + String.format("%.2f", seq * 100.0 / par) + "%");
    } catch (FileNotFoundException e) {
      System.out.println("File Not Found!");
    }
  }

  /**
   * Record the time taken to compute a single computation.
   * 
   * @param rent The computation.
   * @return The time taken in millisecond.
   */
  public static long time(Computation rent) {
    Instant start = Instant.now();
    rent.run("BISHAN");
    Instant end = Instant.now();
    Duration time = Duration.between(start, end);
    return time.toMillis();
  }
}
