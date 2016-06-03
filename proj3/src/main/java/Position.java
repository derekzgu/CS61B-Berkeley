/**
 * Created by chizhang on 6/2/16.
 */
public class Position {
    private Double longitude;   // x of axis
    private Double latitude;    // y of axis

    public Position(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public Position(Position p) {
        this.longitude = p.longitude;
        this.latitude = p.latitude;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public double getLatitude() {
        return this.latitude;
    }

    @Override
    public int hashCode() {
        return (int) (31 * longitude + latitude);
    }

    @Override
    public boolean equals(Object p) {
        if (this.getClass() != p.getClass()) {
            return false;
        }
        Position q = (Position) p;
        return this.longitude.equals(q.getLongitude()) && this.latitude.equals(q.getLatitude());
    }

    // return a new Position, which is the middle of p1 and p2
    public static Position calculateMiddle(Position p1, Position p2) {
        double newLon = (p1.getLongitude() + p2.getLongitude()) / 2.0;
        double newLat = (p1.getLatitude() + p2.getLatitude()) / 2.0;
        return new Position(newLon, newLat);
    }
}
