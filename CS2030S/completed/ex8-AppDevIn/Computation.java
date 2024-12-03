public abstract class Computation {
  /**
   * Running the computation.
   * 
   * @param town The town to be computed.
   * @return The string containing minimum price
   *         for each type for each block within town.
   */
  public abstract String run(String town);
}
