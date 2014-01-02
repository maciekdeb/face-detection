import org.junit.Test;
import pl.lodz.p.ics.FeatureGenerator;
import pl.lodz.p.ics.Utils;
import pl.lodz.p.ics.model.DataSample;
import pl.lodz.p.ics.model.classification.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * User: maciek
 * Date: 15.12.13
 * Time: 22:14
 */
public class ClassifierTest {

    @Test
    public void testPasses() throws IOException {

        List<DataSample> dataSamplesFaces = Utils.loadDataSet(new File("/home/maciek/baza-test/train/face/"), 1.0);
        List<DataSample> dataSamplesNonFaces = Utils.loadDataSet(new File("/home/maciek/baza-test/train/non-face/"), -1.0);
        List<DataSample> dataSamples = new ArrayList<>();
        dataSamples.addAll(dataSamplesFaces);
        dataSamples.addAll(dataSamplesNonFaces);
        System.out.println("Zbiór treningowy\nIlość próbek twarzy: " + dataSamplesFaces.size() + "\nIlość próbek nie twarzy: "+dataSamplesNonFaces.size());

        List<Feature> features1 = Utils.loadFeatures(new File("/home/maciek/Dropbox/workspaces/java/face-detection/features/feature1/xml/"));
        List<Feature> features2 = Utils.loadFeatures(new File("/home/maciek/Dropbox/workspaces/java/face-detection/features/feature2/xml/"));
        List<Feature> features3 = Utils.loadFeatures(new File("/home/maciek/Dropbox/workspaces/java/face-detection/features/feature3/xml/"));
        List<Feature> features4 = Utils.loadFeatures(new File("/home/maciek/Dropbox/workspaces/java/face-detection/features/feature4/xml/"));
        List<Feature> features5 = Utils.loadFeatures(new File("/home/maciek/Dropbox/workspaces/java/face-detection/features/feature5/xml/"));
        List<Feature> features6 = Utils.loadFeatures(new File("/home/maciek/Dropbox/workspaces/java/face-detection/features/feature6/xml/"));
        List<Feature> features7 = Utils.loadFeatures(new File("/home/maciek/Dropbox/workspaces/java/face-detection/features/feature7/xml/"));
        List<Feature> features = new ArrayList<>();
        features.addAll(features1);
        features.addAll(features2);
        features.addAll(features3);
        features.addAll(features4);
        features.addAll(features5);
        features.addAll(features6);
        features.addAll(features7);
        System.out.println("\nLiczba cech:\n1-rodzaju: " + features1.size() + "\n2-rodzaju: " + features2.size() + "\n3-rodzaju: " + features3.size() + "\n4-rodzaju: " + features4.size() + "\n5-rodzaju: " + features5.size() + "\n6-rodzaju: " + features6.size() + "\n7-rodzaju: " + features7.size());


        StrongClassifier strongClassifier = new StrongClassifier(features, dataSamples);
        strongClassifier.learn(5);

        System.out.println("\nWartosci wspolczynnikow klasyfikatora silnego: " + Arrays.toString(strongClassifier.getOutputAlfa()));
        System.out.println("\nKlasyfikatory slabe: " + Arrays.toString(strongClassifier.getOutputClassifiers()));
    }

}
