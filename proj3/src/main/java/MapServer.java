import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;
import java.util.List;

/* Maven is used to pull in these dependencies. */
import com.google.gson.Gson;

import javax.imageio.ImageIO;

import static spark.Spark.*;

/**
 * This MapServer class is the entry point for running the JavaSpark web server for the BearMaps
 * application project, receiving API calls, handling the API call processing, and generating
 * requested images and routes.
 *
 * @author Alan Yao
 */
public class MapServer {
    /**
     * The root upper left/lower right longitudes and latitudes represent the bounding box of
     * the root tile, as the images in the img/ folder are scraped.
     * Longitude == x-axis; latitude == y-axis.
     */
    public static final double ROOT_ULLAT = 37.892195547244356, ROOT_ULLON = -122.2998046875,
            ROOT_LRLAT = 37.82280243352756, ROOT_LRLON = -122.2119140625;
    /**
     * Each tile is 256x256 pixels.
     */
    public static final int TILE_SIZE = 256;
    /**
     * HTTP failed response.
     */
    private static final int HALT_RESPONSE = 403;
    /**
     * Route stroke information: typically roads are not more than 5px wide.
     */
    public static final float ROUTE_STROKE_WIDTH_PX = 5.0f;
    /**
     * Route stroke information: Cyan with half transparency.
     */
    public static final Color ROUTE_STROKE_COLOR = new Color(108, 181, 230, 200);
    /**
     * The tile images are in the IMG_ROOT folder.
     */
    private static final String IMG_ROOT = "img/";
    /**
     * The OSM XML file path. Downloaded from <a href="http://download.bbbike.org/osm/">here</a>
     * using custom region selection.
     **/
    private static final String OSM_DB_PATH = "berkeley.osm";
    /**
     * Each raster request to the server will have the following parameters
     * as keys in the params map accessible by,
     * i.e., params.get("ullat") inside getMapRaster(). <br>
     * ullat -> upper left corner latitude,<br> ullon -> upper left corner longitude, <br>
     * lrlat -> lower right corner latitude,<br> lrlon -> lower right corner longitude <br>
     * w -> user viewport window width in pixels,<br> h -> user viewport height in pixels.
     **/
    private static final String[] REQUIRED_RASTER_REQUEST_PARAMS = {"ullat", "ullon", "lrlat",
            "lrlon", "w", "h"};
    /**
     * Each route request to the server will have the following parameters
     * as keys in the params map.<br>
     * start_lat -> start point latitude,<br> start_lon -> start point longitude,<br>
     * end_lat -> end point latitude, <br>end_lon -> end point longitude.
     **/
    private static final String[] REQUIRED_ROUTE_REQUEST_PARAMS = {"start_lat", "start_lon",
            "end_lat", "end_lon"};
    /* Define any static variables here. Do not define any instance variables of MapServer. */
    private static GraphDB g;

    private static QuadTree qTree;  // construct the QuadTree before asking query to save time
    private static final int maxDepth = 7;
    private static LinkedList<GraphNode> route;

    /**
     * Place any initialization statements that will be run before the server main loop here.
     * Do not place it in the main function. Do not place initialization code anywhere else.
     * This is for testing purposes, and you may fail tests otherwise.
     **/
    public static void initialize() {
        route = null;
        g = new GraphDB(OSM_DB_PATH);
        qTree = new QuadTree(maxDepth);
    }

    public static void main(String[] args) {
        initialize();
        staticFileLocation("/page");
        /* Allow for all origin requests (since this is not an authenticated server, we do not
         * care about CSRF).  */
        before((request, response) -> {
            response.header("Access-Control-Allow-Origin", "*");
            response.header("Access-Control-Request-Method", "*");
            response.header("Access-Control-Allow-Headers", "*");
        });

        /* Define the raster endpoint for HTTP GET requests. I use anonymous functions to define
         * the request handlers. */
        get("/raster", (req, res) -> {
            HashMap<String, Double> params =
                    getRequestParams(req, REQUIRED_RASTER_REQUEST_PARAMS);
            /* The png image is written to the ByteArrayOutputStream */
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            /* getMapRaster() does almost all the work for this API call */
            Map<String, Object> rasteredImgParams = getMapRaster(params, os);
            /* On an image query success, add the image data to the response */
            if (rasteredImgParams.containsKey("query_success")
                    && (Boolean) rasteredImgParams.get("query_success")) {
                String encodedImage = Base64.getEncoder().encodeToString(os.toByteArray());
                rasteredImgParams.put("b64_encoded_image_data", encodedImage);
            }
            /* Encode response to Json */
            Gson gson = new Gson();
            return gson.toJson(rasteredImgParams);
        });

        /* Define the routing endpoint for HTTP GET requests. */
        get("/route", (req, res) -> {
            HashMap<String, Double> params =
                    getRequestParams(req, REQUIRED_ROUTE_REQUEST_PARAMS);
            LinkedList<Long> route = findAndSetRoute(params);
            return !route.isEmpty();
        });

        /* Define the API endpoint for clearing the current route. */
        get("/clear_route", (req, res) -> {
            clearRoute();
            return true;
        });

        /* Define the API endpoint for search */
        get("/search", (req, res) -> {
            Set<String> reqParams = req.queryParams();
            String term = req.queryParams("term");
            Gson gson = new Gson();
            /* Search for actual location data. */
            if (reqParams.contains("full")) {
                List<Map<String, Object>> data = getLocations(term);
                return gson.toJson(data);
            } else {
                /* Search for prefix matching strings. */
                List<String> matches = getLocationsByPrefix(term);
                return gson.toJson(matches);
            }
        });

        /* Define map application redirect */
        get("/", (request, response) -> {
            response.redirect("/map.html", 301);
            return true;
        });
    }

    /**
     * Validate & return a parameter map of the required request parameters.
     * Requires that all input parameters are doubles.
     *
     * @param req            HTTP Request
     * @param requiredParams TestParams to validate
     * @return A populated map of input parameter to it's numerical value.
     */
    private static HashMap<String, Double> getRequestParams(spark.Request req, String[] requiredParams) {
        Set<String> reqParams = req.queryParams();
        HashMap<String, Double> params = new HashMap<>();
        for (String param : requiredParams) {
            if (!reqParams.contains(param)) {
                halt(HALT_RESPONSE, "Request failed - parameters missing.");
            } else {
                try {
                    params.put(param, Double.parseDouble(req.queryParams(param)));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    halt(HALT_RESPONSE, "Incorrect parameters - provide numbers.");
                }
            }
        }
        return params;
    }


    /**
     * Handles raster API calls, queries for tiles and rasters the full image. <br>
     * <p>
     * The rastered photo must have the following properties:
     * <ul>
     * <li>Has dimensions of at least w by h, where w and h are the user viewport width
     * and height.</li>
     * <li>The tiles collected must cover the most longitudinal distance per pixel
     * possible, while still covering less than or equal to the amount of
     * longitudinal distance per pixel in the query box for the user viewport size. </li>
     * <li>Contains all tiles that intersect the query bounding box that fulfill the
     * above condition.</li>
     * <li>The tiles must be arranged in-order to reconstruct the full image.</li>
     * <li>If a current route exists, lines of width ROUTE_STROKE_WIDTH_PX and of color
     * ROUTE_STROKE_COLOR are drawn between all nodes on the route in the rastered photo.
     * </li>
     * </ul>
     * Additional image about the raster is returned and is to be included in the Json response.
     * </p>
     *
     * @param params Map of the HTTP GET request's query parameters - the query bounding box and
     *               the user viewport width and height.
     * @param os     An OutputStream that the resulting png image should be written to.
     * @return A map of parameters for the Json response as specified:
     * "raster_ul_lon" -> Double, the bounding upper left longitude of the rastered image <br>
     * "raster_ul_lat" -> Double, the bounding upper left latitude of the rastered image <br>
     * "raster_lr_lon" -> Double, the bounding lower right longitude of the rastered image <br>
     * "raster_lr_lat" -> Double, the bounding lower right latitude of the rastered image <br>
     * "raster_width"  -> Double, the width of the rastered image <br>
     * "raster_height" -> Double, the height of the rastered image <br>
     * "depth"         -> Double, the 1-indexed quadtree depth of the nodes of the rastered image.
     * Can also be interpreted as the length of the numbers in the image string. <br>
     * "query_success" -> Boolean, whether an image was successfully rastered. <br>
     * @see #REQUIRED_RASTER_REQUEST_PARAMS
     */
    public static Map<String, Object> getMapRaster(Map<String, Double> params, OutputStream os) {
        HashMap<String, Object> rasteredImageParams = new HashMap<>();
        // collect the result of QuadNode list in sorted order
        List<QuadNode> result = qTree.respond(params);
        // get rastered image boundary
        Position rasterUpperLeft = result.get(0).getUpperLeftPosition();
        Position rasterLoweRight = result.get(result.size() - 1).getLowerRightPosition();
        rasteredImageParams.put("raster_ul_lon", rasterUpperLeft.getLongitude());
        rasteredImageParams.put("raster_ul_lat", rasterUpperLeft.getLatitude());
        rasteredImageParams.put("raster_lr_lon", rasterLoweRight.getLongitude());
        rasteredImageParams.put("raster_lr_lat", rasterLoweRight.getLatitude());
        // get width and height
        int i;
        for (i = 1; i < result.size(); i++) {
            if (!Position.hasSameLatitude(result.get(i).getUpperLeftPosition(), result.get(i - 1).getUpperLeftPosition()))
                break;
        }
        assert result.size() % i == 0;
        if (result.size() % i != 0) System.out.println("The result raster picture size is not correct.");
        int width = TILE_SIZE * i, height = TILE_SIZE * result.size() / i;
        rasteredImageParams.put("raster_width", width);
        rasteredImageParams.put("raster_height", height);
        // get depth
        rasteredImageParams.put("depth", result.get(0).getDepth());

        // write the image to the file
        String[] images = new String[result.size()];
        int index = 0;
        for (QuadNode q : result) {
            images[index] = q.getPicture() + ".png";
            index += 1;
        }
        BufferedImage im = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = im.getGraphics();
        try {
            // draw the map
            int x = 0, y = 0;
            for (String image : images) {
                BufferedImage bi = ImageIO.read(new File(IMG_ROOT + image));
                g.drawImage(bi, x, y, null);
                x += bi.getWidth();
                if (x > im.getWidth() - 1) {
                    x = 0;
                    y += bi.getHeight();
                }
            }
            rasteredImageParams.put("query_success", true);
            // draw the route
            if (route != null) {
                Stroke s = new BasicStroke(MapServer.ROUTE_STROKE_WIDTH_PX,
                        BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
                ((Graphics2D) g).setStroke(s);
                g.setColor(MapServer.ROUTE_STROKE_COLOR);
                GraphNode previous = null;
                for (GraphNode n : route) {
                    if (previous != null) {
                        // calculate the relative coordination
                        int x1 = getRelativeX(previous, rasterUpperLeft.getLongitude(),
                                rasterLoweRight.getLongitude(), width);
                        int y1 = getRelativeY(previous, rasterUpperLeft.getLatitude(),
                                rasterLoweRight.getLatitude(), height);
                        int x2 = getRelativeX(n, rasterUpperLeft.getLongitude(),
                                rasterLoweRight.getLongitude(), width);
                        int y2 = getRelativeY(n, rasterUpperLeft.getLatitude(),
                                rasterLoweRight.getLatitude(), height);
                        g.drawLine(x1, y1, x2, y2);
                    }
                    previous = n;
                }
            }
            ImageIO.write(im, "png", os); // write the image to os
        } catch (IOException e) {
            System.out.println("The image is not present in the img/ folder.");
            rasteredImageParams.put("query_success", false);
        }

        return rasteredImageParams;
    }

    // transform the graph (lon, lat) to relative x pixel
    private static int getRelativeX(GraphNode n, double raster_ul_lon, double raster_lr_lon, int width) {
        double lonPerPixel = (raster_lr_lon - raster_ul_lon) / width;
        return (int) ((n.getPosition().getLongitude() - raster_ul_lon) / lonPerPixel);
    }

    private static int getRelativeY(GraphNode n, double raster_ul_lat, double raster_lr_lat, int height) {
        double latPerPixel = (raster_lr_lat - raster_ul_lat) / height;
        return (int) ((n.getPosition().getLatitude() - raster_ul_lat) / latPerPixel);
    }

    /**
     * Searches for the shortest route satisfying the input request parameters, sets it to be the
     * current route, and returns a <code>LinkedList</code> of the route's node ids for testing
     * purposes. <br>
     * The route should start from the closest node to the start point and end at the closest node
     * to the endpoint. Distance is defined as the euclidean between two points (lon1, lat1) and
     * (lon2, lat2).
     *
     * @param params from the API call described in REQUIRED_ROUTE_REQUEST_PARAMS
     * @return A LinkedList of node ids from the start of the route to the end.
     */
    public static LinkedList<Long> findAndSetRoute(Map<String, Double> params) {
        // find route, which is a LinkedList of GraphNodes
        route = findRoute(params);
        // draw the route should happen inside getMapRaster method!
        LinkedList<Long> routeInId = new LinkedList<>();
        for (GraphNode n : route) {
            routeInId.add(n.getId());
        }
        return routeInId;
    }

    private static LinkedList<GraphNode> findRoute(Map<String, Double> params) {
        // get the query
        double start_lon = params.get("start_lon");
        double start_lat = params.get("start_lat");
        double end_lon = params.get("end_lon");
        double end_lat = params.get("end_lat");
        Position start = new Position(start_lon, start_lat);
        Position end = new Position(end_lon, end_lat);
        // find the start and end GraphNode
        GraphNode startNode = g.getClosestNode(start);
        GraphNode endNode = g.getClosestNode(end);
        // it should follow the basic rule of A* search, use Euclidean distance as heuristic
        Set<GraphNode> exploredSet = new HashSet<>();
        PriorityQueue<AStarSearchNode> frontier = new PriorityQueue<>();
        frontier.add(new AStarSearchNode(startNode, endNode));

        while (!frontier.isEmpty()) {
            AStarSearchNode currentSearchNode = frontier.remove();
            if (exploredSet.contains(currentSearchNode.getNode())) continue;
            // if the remove node is goal
            if (currentSearchNode.isGoal()) {
                return currentSearchNode.getRoute();
            }
            // add currentSearchNode to exploredSet
            exploredSet.add(currentSearchNode.getNode());
            // add it to the result
            Iterable<AStarSearchNode> neighbors = currentSearchNode.getSuccessor();
            for (AStarSearchNode a : neighbors) {
                if (!exploredSet.contains(a.getNode())) {
                    frontier.add(a);
                }
            }
        }
        // can't find a route
        return null;
    }

    /**
     * Clear the current found route, if it exists.
     */
    public static void clearRoute() {
        // it should clear the result and drawing
        route = null;
    }

    /**
     * In linear time, collect all the names of OSM locations that prefix-match the query string.
     *
     * @param prefix Prefix string to be searched for. Could be any case, with our without
     *               punctuation.
     * @return A <code>List</code> of the full names of locations whose cleaned name matches the
     * cleaned <code>prefix</code>.
     */
    public static List<String> getLocationsByPrefix(String prefix) {
        return g.getLocationsByPrefix(prefix);
    }

    /**
     * Collect all locations that match a cleaned <code>locationName</code>, and return
     * information about each node that matches.
     *
     * @param locationName A full name of a location searched for.
     * @return A list of locations whose cleaned name matches the
     * cleaned <code>locationName</code>, and each location is a map of parameters for the Json
     * response as specified: <br>
     * "lat" -> Number, The latitude of the node. <br>
     * "lon" -> Number, The longitude of the node. <br>
     * "name" -> String, The actual name of the node. <br>
     * "id" -> Number, The id of the node. <br>
     */
    public static List<Map<String, Object>> getLocations(String locationName) {
        List<Map<String, Object>> result = new LinkedList<>();
        List<GraphNode> graphNodes = g.getListGraphNodeByName(GraphDB.cleanString(locationName));
        for (GraphNode n : graphNodes) {
            Map<String, Object> m = new HashMap<>();
            m.put("lat", n.getPosition().getLatitude());
            m.put("lon", n.getPosition().getLongitude());
            m.put("name", n.getName());
            m.put("id", n.getId());
            result.add(m);
        }
        return result;
    }

}
