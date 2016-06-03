/**
 * Created by chizhang on 6/2/16.
 */
public class QuadNode {
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
    // the depth of the node
    private int depth;

    public QuadNode(double ulLon, double ulLat, double lrLon, double lrLat, String picture, int depth) {
        this.upperLeftPosition = new Position(ulLon, ulLat);
        this.lowerRightPosition = new Position(lrLon, lrLat);
        this.picture = picture;
        this.depth = depth;
        this.upperLeftChild = null;
        this.upperRightChild = null;
        this.lowerLeftChild = null;
        this.lowerRightChild = null;
    }

    public QuadNode(Position upperLeft, Position lowerRight, String picture, int depth) {
        this.upperLeftPosition = new Position(upperLeft);
        this.lowerRightPosition = new Position(lowerRight);
        this.picture = picture;
        this.depth = depth;
        this.upperLeftChild = null;
        this.upperRightChild = null;
        this.lowerLeftChild = null;
        this.lowerRightChild = null;
    }

    // child can't be null, return false if setChild failed
    public boolean setChild(QuadNode child, int direction) {
        if (child == null) return false;
        switch (direction) {
            case 1: this.upperLeftChild = child; break;
            case 2: this.upperRightChild = child; break;
            case 3: this.lowerLeftChild = child; break;
            case 4: this.lowerRightChild = child; break;
            default: return false;
        }
        return true;
    }

    public int getDepth() {
        return depth;
    }

    public Position getUpperLeftPosition() {
        return new Position(upperLeftPosition);
    }

    public Position getLowerRightPosition() {
        return new Position(lowerRightPosition);
    }

    public String getPicture() {
        return picture;
    }

}
