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
}
