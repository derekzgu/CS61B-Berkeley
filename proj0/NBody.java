/*
 * This is a class for NBody simulator
 */

public class NBody {

    public static void main (String[] args) {
        double T = Double.parseDouble(args[0]);
        double dt = Double.parseDouble(args[1]);
        String filename = args[2];
        double radius = readRadius(filename);
        Planet[] pArray = readPlanets(filename);
        String background = "./images/starfield.jpg";

        StdDraw.setScale(-radius, radius);
        StdDraw.clear();
        StdDraw.picture(0, 0, background);

        for (Planet p: pArray) {
            p.draw();
        }

        StdDraw.show(2000);
    }

    public static double readRadius (String filename) {
        In in = new In(filename);
        int numPlanets = in.readInt();
        return in.readDouble();
    }

    public static Planet[] readPlanets (String filename) {
        In in = new In(filename);
        int numPlanets = in.readInt();
        double radius = in.readDouble();
        Planet[] pArray = new Planet[numPlanets];
        String imageRoot = "./images/";
        for (int i = 0; i < numPlanets; i++) {
            pArray[i] = new Planet(0.0, 0.0, 0.0, 0.0, 0.0, "");
            pArray[i].xxPos = in.readDouble();
            pArray[i].yyPos = in.readDouble();
            pArray[i].xxVel = in.readDouble();
            pArray[i].yyVel = in.readDouble();
            pArray[i].mass = in.readDouble();
            pArray[i].imgFileName = imageRoot + in.readString();
        }
        return pArray;
    }
}