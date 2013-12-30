import junit.framework.Assert;
import org.junit.Test;
import pl.lodz.p.ics.FeatureGenerator;
import pl.lodz.p.ics.Utils;
import pl.lodz.p.ics.model.Point;
import pl.lodz.p.ics.model.classification.Feature;
import pl.lodz.p.ics.model.classification.IntegralImage;

import java.awt.image.BufferedImage;
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

        List<Feature> featureList = featureGenerator.loadReferenceFeatures(Collections.singletonList("features/feature7-scheme.xml"));

        List<Feature> generatedFeatures = featureGenerator.generateFeaturesCollection(featureList, 10, 10, 1, 1, 1, 1);
        Utils.drawFeature(generatedFeatures, 10);

    }

}
