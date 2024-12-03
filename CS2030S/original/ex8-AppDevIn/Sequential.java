import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Encapsulates a sequential computation.
 *
 * @author: Adi Yoga S. Prabawa
 * @version: CS2030S AY24/25 Semester 1, Ex 8
 */
public class Sequential extends Computation {
  /** Data source. */
  private Source source;

  /**
   * Constructor for this sequential computation.
   * Initializing data source.
   * 
   * @throws FileNotFoundException If the file not found.
   */
  public Sequential() throws FileNotFoundException {
    this.source = new Source();
  }

  /**
   * Running the computation sequentially.
   * 
   * @param town The town to be computed.
   * @return The string containing minimum price
   *         for each type for each block within town.
   */
  @Override
  public String run(String town) {
    String[] blocks = this.source.findBlock(town);
    StringBuffer buffer = new StringBuffer();

    for (String block : blocks) {
      buffer.append(this.processBlock(town, block));
    }
    
    return buffer.toString();
  }

  /**
   * Process the price for each block sequentially.
   * Find the type, and for each type, find the minimum price.
   * 
   * @param town The town to be computed.
   * @param block The block to be computed.
   * @return The string containing minimum price
   *         for each type within block within town.
   */
  private String processBlock(String town, String block) {
    String[] types = this.source.findTypeInBlock(town, block);
    StringBuffer buffer = new StringBuffer();

    buffer.append(town + ", " + block + "\n");

    for (String type : types) {
      buffer.append("  " + type + ": " + this.source.findMinPrice(town, block, type) + "\n");
    }

    return buffer.toString();
  }
  
  /**
   * The program read a single string indicating the town from
   * standard input.  Then it initializes the computation and
   * run the computation with the given town.  If the source data
   * is not found, the program catch the error and displays an
   * error message indicating file not found.
   * 
   * @param args Command line arguments
   */
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    try {
      Sequential rent = new Sequential();
      System.out.println(rent.run(sc.next()));
    } catch (FileNotFoundException e) {
      System.out.println("File Not Found!");
    } finally {
      sc.close();
    }
  }
}
