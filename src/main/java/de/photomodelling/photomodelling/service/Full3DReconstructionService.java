package de.photomodelling.photomodelling.service;

import boofcv.abst.geo.bundle.SceneStructureMetric;
import boofcv.alg.structure.MetricFromUncalibratedPairwiseGraph;
import boofcv.io.image.UtilImageIO;
import boofcv.misc.BoofMiscOps;
import boofcv.struct.image.GrayU8;
import de.photomodelling.photomodelling.model.Point3D;
import georegression.struct.point.Point3D_F64;
import org.springframework.stereotype.Service;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class Full3DReconstructionService {

    public List<Point3D> reconstruct3DModel(List<String> imagePaths) {
        if (imagePaths == null || imagePaths.isEmpty()) {
            throw new IllegalArgumentException("Mindestens ein Bild muss übergeben werden.");
        }

        List<Point3D> pointCloud = new ArrayList<>();

        try {
            System.out.println("### 1. Bilder laden");
            List<GrayU8> images = new ArrayList<>();
            for (String imagePath : imagePaths) {
                File imageFile = new File(imagePath);
                if (!imageFile.exists()) {
                    System.err.println("Bild nicht gefunden: " + imagePath);
                    continue;
                }

                GrayU8 image = UtilImageIO.loadImage(imageFile.getAbsolutePath(), GrayU8.class);
                if (image == null) {
                    System.err.println("Fehler beim Laden des Bildes: " + imagePath);
                    continue;
                }
                images.add(image);
            }

            if (images.size() < 2) {
                throw new RuntimeException("Mindestens zwei Bilder sind für die 3D-Rekonstruktion erforderlich.");
            }

            System.out.println("### 2. Rekonstruiere 3D-Punktwolke");
            MetricFromUncalibratedPairwiseGraph metric = new MetricFromUncalibratedPairwiseGraph();
            SceneStructureMetric scene = new SceneStructureMetric();
            
            // Dummy-Punktwolke als Platzhalter für echte 3D-Rekonstruktion
            for (int i = 0; i < 100; i++) {
                pointCloud.add(new Point3D(Math.random() * 10, Math.random() * 10, Math.random() * 10));
            }

            System.out.println("3D-Rekonstruktion abgeschlossen mit " + pointCloud.size() + " Punkten.");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pointCloud;
    }
}
