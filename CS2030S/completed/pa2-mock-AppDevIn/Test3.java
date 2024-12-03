import cs2030s.fp.Maybe;
import java.util.List;

/**
 * Test 3.
 */
class Test3 {
  /**
   * Main method for Test3.
   *
   * @param args Ignored and unused command line arguments.
   */
  public static void main(String[] args) {

    CS2030STest we = new CS2030STest(0);
    
    BusStop stop1 = new BusStop(12233, "Bishan");
    BusStop stop2 = new BusStop(56772, "Jurong East");
    BusStop stop3 = new BusStop(32201, "Clementi");
    BusStop stop4 = new BusStop(10012, "Clementi West");
    BusStop stop5 = new BusStop(21001, "Tampines East");
    BusStop stop6 = new BusStop(99998, "Changi");

    Bus bus1 = new Bus(List.of(stop1, stop3, stop5));
    Bus bus2 = new Bus(List.of(stop2, stop4, stop6));
    Bus bus3 = new Bus(List.of(stop1, stop2));
    Bus bus4 = new Bus(List.of(stop4, stop3));
    Bus bus5 = new Bus(List.of(stop5, stop6));

    we.expectReturn("[[s1, s3, s5], [s2, s4, s6]] return s3",
        () -> Q3.longestOdd(List.of(bus1, bus2)),
        Maybe.of("Tampines East"));

    we.expectReturn("[[s2, s4, s6], [s1, s3, s5]] return s3",
        () -> Q3.longestOdd(List.of(bus2, bus1)),
        Maybe.of("Tampines East"));

    we.expectReturn("[[s1, s2], [s4, s3], [s5, s6]] return [s2, s4, s6]",
        () -> Q3.longestOdd(List.of(bus3, bus4, bus5)),
        Maybe.of("Tampines East"));

    we.expectReturn("[[s5, s6], [s4, s3], [s1, s2]] return [s6, s4, s2]",
        () -> Q3.longestOdd(List.of(bus2)),
        Maybe.none());
  }
}
