/**
 * This is a Planet class constructor
 */

public class Planet {
    double xxPos;
    double yyPos;
    double xxVel;
    double yyVel;
    double mass;
    String imgFileName;

    /*
     * Constructor from parameters
     */

    public Planet (double xP, double yP, double xV, double yV, double m, String img) {
        xxPos = xP;
        yyPos = yP;
        xxVel = xV;
        yyVel = yV;
        mass = m;
        imgFileName = img;
    }

    /*
     * copy constructor
     */
    public Planet (Planet p) {
        xxPos = p.xxPos;
        yyPos = p.yyPos;
        xxVel = p.xxVel;
        yyVel = p.yyVel;
        mass = p.mass;
        imgFileName = p.imgFileName;  // String is immutable in Java and will be copied using "="
    }

    /*
     * Calculate distance method
     */

    public double calcDistance (Planet p) {
        return Math.sqrt((xxPos - p.xxPos) * (xxPos - p.xxPos) + (yyPos - p.yyPos) * (yyPos - p.yyPos));
    }

    public double calcForceExertedBy (Planet p) {
        double G = 6.67e-11;
        double distance = calcDistance(p);
        return G * mass * p.mass / distance / distance ;
    }

    public double calcForceExertedByX (Planet p) {
        return calcForceExertedBy(p) * (p.xxPos - xxPos) / calcDistance(p);
    }

    public double calcForceExertedByY (Planet p) {
        return calcForceExertedBy(p) * (p.yyPos - yyPos) / calcDistance(p);
    }

    public double calcNetForceExertedByX (Planet[] pArray) {
        double xForce = 0;
        for (Planet p: pArray) {
            if (p == this) continue;
            xForce += calcForceExertedByX(p);
        }
        return xForce;
    }

    public double calcNetForceExertedByY (Planet[] pArray) {
        double yForce = 0;
        for (Planet p: pArray) {
            if (p == this) continue;
            yForce += calcForceExertedByY(p);
        }
        return yForce;
    }

    public void update (double interval, double xForce, double yForce) {
        double xAcce = xForce / mass;
        double yAcce = yForce / mass;
        xxVel += xAcce * interval;
        yyVel += yAcce * interval;
        xxPos += xxVel * interval;
        yyPos += yyVel * interval;
    }
}
