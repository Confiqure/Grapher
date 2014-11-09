package grapher;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import javax.swing.JFrame;

/**
 *
 * Program to graph simple data trends and analyze data.
 * 
 * @author Dylan Wheeler
 */
public class Grapher extends JFrame {
    
    /**
     * List of all of the equations to graph.
     */
    public static final ArrayList<Equation> entries = new ArrayList<>();
    
    /**
     *
     * Creates new instance of the Grapher UI.
     * 
     * @param positive boolean indicating if the graph should be focused on only the positive axes or all four quadrants of the coordinate plane
     * @param title the title of the graph
     * @param dimension dimensions of the graph
     */
    public Grapher(final boolean positive, final String title, final Dimension dimension) {
        setTitle("Grapher By Dylan Wheeler - " + title);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(new Graph(positive), "Center");
        setPreferredSize(dimension);
        pack();
    }
    
    /**
     *
     * Main method.
     * 
     * @param args first argument is boolean true if graphing on positive axes or false if graphing on all four quadrants, second argument indicates dimensions, third argument is title (spaces are marked by underscores) and the subsequent arguments are the lines to graph format {name,equation,color}
     */
    public static void main(final String args[]) {
        final boolean positive = Boolean.parseBoolean(args[0]);
        final Dimension dimension = new Dimension(Integer.parseInt(args[1].split(",")[0]), Integer.parseInt(args[1].split(",")[1]));
        final String title = args[2].replace("_", " ");
        System.out.println("positive: " + positive);
        System.out.println("dimension: " + dimension);
        System.out.println("title: " + title);
        for (int i = 3; i < args.length; i ++) {
            parseArg(args[i]);
            System.out.println("line" + (i - 2) + ": " + args[i]);
        }
        final Grapher g = new Grapher(positive, title, dimension);
        g.setLocationRelativeTo(g.getOwner());
        g.setVisible(true);
    }
    
    private static void parseArg(final String arg) {
        if (!arg.startsWith("{") || !arg.endsWith("}")) {
            System.err.println("Unable to parse argument: " + arg);
            return;
        }
        final String[] parts = arg.substring(1, arg.length() - 1).split(",");
        if (parts.length != 3) {
            System.err.println("Unable to parse argument: " + arg);
            return;
        }
        Color c = Color.black;
        switch (parts[2].toLowerCase()) {
            case "black":
                c = Color.black;
                break;
            case "blue":
                c = Color.blue;
                break;
            case "green":
                c = Color.green;
                break;
            case "magenta":
                c = Color.magenta;
                break;
            case "orange":
                c = Color.orange;
                break;
            case "pink":
                c = Color.pink;
                break;
            case "red":
                c = Color.red;
                break;
            case "yellow":
                c = Color.yellow;
                break;
        }
        parts[1] = parts[1].replace("+-", "-").replace("-+", "-").replace("-", "+-");
        if (!parts[1].contains("+")) {
            parts[1] += "+0";
        }
        final String[] eq = parts[1].split("\\+");
        if (eq.length != 2) {
            System.err.println("Error solving equation: " + parts[1]);
        }
        entries.add(new Equation(parts[0], Double.parseDouble(eq[0].substring(0, eq[0].length() - 1)), Double.parseDouble(eq[1]), c));
    }
    
    public static class Equation {

        /**
         * Name of the equation.
         */
        public String name;

        /**
         * Slope.
         */
        public double m;

        /**
         * Y-intercept.
         */
        public double b;

        /**
         * Color.
         */
        public Color c;

        /**
         *
         * Creates new Equation.
         * 
         * @param name name of the Equation
         * @param m slope of the line of the Equation
         * @param b y-intercept of the line of the Equation
         * @param c color of the line of the Equation
         */
        public Equation(final String name, final double m, final double b, final Color c) {
            this.name = name;
            this.m = m;
            this.b = b;
            this.c = c;
        }

    }
    
}
