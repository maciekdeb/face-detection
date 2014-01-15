import org.junit.Test;
import pl.lodz.p.ics.FeatureGenerator;
import pl.lodz.p.ics.Utils;
import pl.lodz.p.ics.model.classification.Feature;

import java.util.Collections;
import java.util.List;

/**
 * User: maciek
 * Date: 15.12.13
 * Time: 22:14
 */
public class FeatureTest {

    String directory = "features19";
    int size = 19;

    @Test
    public void testPasses() {

        FeatureGenerator featureGenerator = new FeatureGenerator();

        int[] which = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

        for (int i : which) {
            List<Feature> featureList = Utils.loadFeatures(Collections.singletonList(directory + "/feature" + i + "/feature" + i + "-scheme.xml"));

            List<Feature> generatedFeatures = featureGenerator.generateResizedFeatures(featureList, size, size, 1, 1);
            generatedFeatures = featureGenerator.generateMovedFeatures(generatedFeatures, size, size, 1, 1);

            featureGenerator.saveFeature(generatedFeatures, directory + "/feature" + i + "/xml/feature" + i);

            Utils.drawFeature(generatedFeatures, directory + "/feature" + i + "/images/feature" + i, 10);
        }

    }

}
