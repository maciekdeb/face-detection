import org.junit.Test;
import pl.lodz.p.ics.Utils;
import pl.lodz.p.ics.model.DataSample;
import pl.lodz.p.ics.model.Point;
import pl.lodz.p.ics.model.classification.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.*;

/**
 * User: maciek
 * Date: 15.12.13
 * Time: 22:14
 */
public class ClassifierTest {

    @Test
    public void testPasses() throws IOException {

        List<DataSample> dataSamplesFaces = Utils.loadDataSet(new File("/home/maciek/baza-test/train/face/"), 1.0);
        List<DataSample> dataTestFaces = Utils.loadDataSet(new File("/home/maciek/baza-test/test/face/"), 1.0);

        List<DataSample> dataSamplesNonFaces = Utils.loadDataSet(new File("/home/maciek/baza-test/train/non-face/"), 0.0);
        List<DataSample> dataTestNonFaces = Utils.loadDataSet(new File("/home/maciek/baza-test/test/non-face/"), 0.0);

        System.out.println("Zbiór treningowy\nIlość próbek z twarzą: " + dataSamplesFaces.size() + "\nIlość próbek bez twarzy: " + dataSamplesNonFaces.size());
        System.out.println("Zbiór testowy\nIlość próbek z twarzą: " + dataTestFaces.size() + "\nIlość próbek bez twarzy: " + dataTestNonFaces.size());


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



        StrongClassifier strongClassifier = /*Utils.loadClassifier("strongClassifier-100-1388787495674");*/new StrongClassifier(features, dataSamplesFaces, dataSamplesNonFaces);
        strongClassifier.learn(230);

        System.out.print("\nWartosci wspolczynnikow klasyfikatora silnego: ");
        for (double d : strongClassifier.getAlfaFactor()) {
            System.out.print(d + " ");
        }
        System.out.println("\nKlasyfikatory slabe: " + Arrays.toString(strongClassifier.getWeakClassifiers()));

        List<DataSample> dataTest = new ArrayList<>();
        dataTest.addAll(dataTestFaces);
        dataTest.addAll(dataTestNonFaces);
        double ratio = 0;
        int testSize = 900;
        boolean used[] = new boolean[testSize];

        for (int i = 1; i < testSize; i++) {

            int index;
            do {
                index = new Random().nextInt(testSize);
            } while (used[index] == true);

            DataSample ds = dataTest.get(index);
            double faceDetect = strongClassifier.detect(ds.getIntegralImage(), new Point(0, 0));

            if (faceDetect == ds.getY()) {
                ratio++;
            }
            System.out.print("\nPróbka nr: " + i + "\tWartość oczekiwana: " + ds.getY() + " wart. otrzymana: " + faceDetect);

            System.out.println("\n\tWspolczynnik poprawnych: " + ratio / i);
        }

        Utils.saveClassifier(strongClassifier, "strongClassifier-230-");
    }



}
