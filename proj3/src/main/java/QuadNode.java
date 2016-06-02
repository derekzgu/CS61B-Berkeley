/**
 * Created by chizhang on 6/2/16.
 */
public class QuadNode {

    private class Position {
        private Double longitude;   // x of axis
        private Double latitude;    // y of axis

        public Position(double lon, double lat) {
            this.longitude = lon;
            this.latitude = lat;
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

    // position
    private Position upperLeftPosition;
    private Position lowerRightPosition;
    // child node
    private QuadNode upperLeftChild;   // northwest, denoted as "1"
    private QuadNode upperRightChild;  // northeast, denoted as "2"
    private QuadNode lowerLeftChild;   // southwest, denoted as "3"
    private QuadNode lowerRightChild;  // southeast, denoted as "4"
    // associated picture
    private String picture;   // here we just denote the name as 1, 2, 3, 4 without the IMG_ROOT


}
