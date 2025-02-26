package de.photomodelling.photomodelling.service;

import de.photomodelling.photomodelling.model.Point3D;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Full3DReconstructionService {

    public List<Point3D> reconstruct3DModel(List<String> imagePaths) {
        return pointCloud;
    }
}
