/**
 * Encapsulates an immutable rent data coming from a single
 * row of the CSV file.  It is a simple class with getters.
 * We use getters to simplify computation.
 *
 * @author: Adi Yoga S. Prabawa
 * @version: CS2030S AY24/25 Semester 1, Ex 8
 */
public class RentData {
  /** The town. */
  private final String town;

  /** The block within town. */
  private final String block;

  /** The type of house. */
  private final String type;

  /** The monthly rent price. */
  private final int rent;

  /**
   * Initializes RentData with the given information from CSV.
   * 
   * @param town The town.
   * @param block The block within town.
   * @param type The type of house.
   * @param rent The monthly rent price.
   */
  public RentData(String town, String block, String type, String rent) {
    this.town = town;
    this.block = block;
    this.type = type;
    this.rent = Integer.parseInt(rent);
  }

  /**
   * Accessor for the town.
   * 
   * @return The town.
   */
  public String getTown() {
    return this.town;
  }

  /**
   * Accessor for the block within town.
   * 
   * @return The block within town.
   */
  public String getBlock() {
    return this.block;
  }

  /**
   * Accessor for the type of house.
   * 
   * @return The type of house.
   */
  public String getType() {
    return this.type;
  }

  /**
   * Accessor for the monthly rent price.
   * 
   * @return The monthly rent price.
   */
  public int getRent() {
    return this.rent;
  }
}