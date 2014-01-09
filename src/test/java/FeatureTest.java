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

    @Test
    public void testPasses() {

        FeatureGenerator featureGenerator = new FeatureGenerator();

        int[] which = new int[]{3, 4, 5, 6, 7, 8, 9, 10};

        for (int i : which) {
            List<Feature> featureList = Utils.loadFeatures(Collections.singletonList("features/feature" + i + "/feature" + i + "-scheme.xml"));

            List<Feature> generatedFeatures = featureGenerator.generateResizedFeatures(featureList, 24, 24, 1, 1);
            generatedFeatures = featureGenerator.generateMovedFeatures(generatedFeatures, 24, 24, 1, 1);

            featureGenerator.saveFeature(generatedFeatures, "features/feature" + i + "/xml/feature" + i);

            Utils.drawFeature(generatedFeatures, "features/feature" + i + "/images/feature" + i, 10);
        }

    }

}
