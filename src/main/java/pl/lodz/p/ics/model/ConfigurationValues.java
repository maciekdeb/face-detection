package pl.lodz.p.ics.model;

import com.google.common.base.Functions;
import com.google.common.collect.Lists;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import pl.lodz.p.ics.model.transform.Ellipse;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * User: maciek
 * Date: 11.11.13
 * Time: 19:27
 */
public class ConfigurationValues {

    public static int ALFA;
    public static int CENTERS_NUMBER;

    public static int ELIPSE_WIDTH;
    public static int ELIPSE_HEIGHT;
    public static double ELIPSE_HEIGHT_EXPANSION_COEFFICIENT;
    public static double ELIPSE_HEIGHT_REDUCTION_COEFFICIENT;

    public static List<String> URLS;

    public static String OUTPUT_PREFIX;
    public static String OUTPUT_DIRECTIONAL_IMAGE;
    public static String OUTPUT_VOTES_IMAGE;
    public static String OUTPUT_ELIPSES_IMAGE;

    static {
        try {
            Configuration config = new PropertiesConfiguration("config.properties");

            ALFA = config.getInt("alfa");
            CENTERS_NUMBER = config.getInt("centers.number");
            ELIPSE_WIDTH = config.getInt("elipse.width");
            ELIPSE_HEIGHT = config.getInt("elipse.height");
            ELIPSE_HEIGHT_EXPANSION_COEFFICIENT = config.getDouble("elipse.height_expansion_coefficient");
            ELIPSE_HEIGHT_REDUCTION_COEFFICIENT = config.getDouble("elipse.height_reduction_coefficient");
            URLS = Lists.transform(config.getList("urls"), Functions.toStringFunction());

            OUTPUT_PREFIX = config.getString("output.prefix");
            OUTPUT_DIRECTIONAL_IMAGE = OUTPUT_PREFIX + config.getString("output.directional_image");
            OUTPUT_VOTES_IMAGE = OUTPUT_PREFIX + config.getString("output.votes_image");
            OUTPUT_ELIPSES_IMAGE = OUTPUT_PREFIX + config.getString("output.elipses_on_image");

        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
    }

    public static List<URL> getURLS() {
        List<URL> result = new ArrayList<URL>();

        for (String path : URLS) {
            result.add(ConfigurationValues.class.getResource(path));
        }

        return result;
    }

    public static Ellipse getReferenceElipse() {
        return new Ellipse(ELIPSE_WIDTH, ELIPSE_HEIGHT, ELIPSE_HEIGHT_EXPANSION_COEFFICIENT, ELIPSE_HEIGHT_REDUCTION_COEFFICIENT);
    }

}
