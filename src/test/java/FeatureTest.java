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

        String whichFeature = "feature1";

        List<Feature> featureList = Utils.loadFeatures(Collections.singletonList("features/" + whichFeature + "/" + whichFeature + "-scheme.xml"));

        List<Feature> generatedFeatures = featureGenerator.generateResizedFeatures(featureList, 24, 24, 1, 1);
        generatedFeatures = featureGenerator.generateMovedFeatures(generatedFeatures, 24, 24, 2, 2);

        featureGenerator.saveFeature(generatedFeatures, "features/" + whichFeature + "/xml/" + whichFeature);

        Utils.drawFeature(generatedFeatures, "features/" + whichFeature + "/images/" + whichFeature, 10);

    }

}
