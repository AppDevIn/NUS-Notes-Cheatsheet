import java.util.Scanner;

/**
 * The main class for CS2030S Ex 1.
 *
 * @author dcsaysp
 * @version CS2030S AY24/25 Semester 1
 */
class Ex1 {
  public static void main(String[] args) {

    // Create a scanner to read from standard input.
    Scanner sc = new Scanner(System.in);

    // Create a simulation.  The CoffeeSimulation 
    // constructor will read the simulation parameters 
    // and initial events using the scanner.
    Simulation simulation = new CoffeeSimulation(sc);

    // Create a new simulator and run the simulation
    new Simulator(simulation).run();

    // Clean up the scanner.
    sc.close();
  }
}
