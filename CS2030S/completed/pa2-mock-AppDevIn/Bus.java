import java.util.List;

public class Bus {
  private List<BusStop> stops;

  public Bus(List<BusStop> stops) {
    this.stops = stops;
  }

  public List<BusStop> getStops() {
    return this.stops;
  }

  @Override
  public String toString() {
    return this.stops + "";
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj instanceof Bus) {
      Bus bus = (Bus) obj;
      return this.stops.equals(bus.stops);
    }
    return false;
  }
}
