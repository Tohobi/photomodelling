package de.photomodelling.photomodelling.service;

import boofcv.alg.similar.ConfigSimilarImagesSceneRecognition;
import boofcv.alg.similar.SimilarImagesSceneRecognition;
import boofcv.alg.structure.LookUpCameraInfo;
import boofcv.alg.structure.MetricFromUncalibratedPairwiseGraph;
import boofcv.alg.structure.PairwiseImageGraph;
import boofcv.factory.structure.FactorySceneReconstruction;
import boofcv.io.image.UtilImageIO;
import boofcv.struct.image.GrayU8;
import boofcv.struct.image.ImageType;
//import boofcv.alg.structure.SceneStructureMetric;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.photomodelling.photomodelling.model.*;
import de.photomodelling.photomodelling.observer.ProjectNotifier;
import de.photomodelling.photomodelling.repository.ProjectRepository;
import de.photomodelling.photomodelling.repository.PhotoRepository;
import de.photomodelling.photomodelling.repository.NoteRepository;
import de.photomodelling.photomodelling.repository.ThreeDRepository;
import de.photomodelling.photomodelling.strategy.AverageRatingStrategy;
import de.photomodelling.photomodelling.strategy.RatingStrategy;
import georegression.struct.point.Point3D_F64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    @Autowired
    private final ProjectRepository projectRepository;

    @Autowired
    private final PhotoRepository photoRepository;

    @Autowired
    private final NoteRepository noteRepository;

    @Autowired
    private final ThreeDRepository threeDRepository;

    //@Autowired
    private RatingStrategy ratingStrategy;

    private final ProjectNotifier projectNotifier;

    @Autowired
    public ProjectService(ProjectRepository projectRepository, PhotoRepository photoRepository, NoteRepository noteRepository, ThreeDRepository threeDRepository, ProjectNotifier projectNotifier) {
        this.projectRepository = projectRepository;
        this.photoRepository = photoRepository;
        this.noteRepository = noteRepository;
        this.threeDRepository = threeDRepository;
        this.ratingStrategy = new AverageRatingStrategy();
        this.projectNotifier = projectNotifier;
    }

    // Ein einzelnes Projekt abrufen
    public List<Project> findProjectById(Long idFilter) {
        List<Project> projectList = new ArrayList<>();
        if (idFilter == null) {
            projectRepository.findAll().forEach(projectList::add);
        } else {
            projectRepository.findById(idFilter).ifPresent(projectList::add);
        }
        return projectList;
    }

    // Ein neues Projekt speichern
    public Project createProject(Project project) {
        Project savedProject = projectRepository.save(project);
        projectNotifier.notifyProjectCreated(savedProject);
        return savedProject;
    }

    // Einem Projekt mehrere Fotos hinzufügen
    public Project addPhotosToProject(Long projectId, List<Photo> photos) {
        Optional<Project> projectOpt = projectRepository.findById(projectId);

        if (projectOpt.isPresent()) {
            Project project = projectOpt.get();

            for (Photo photo : photos) {
                photo.setProject(project);
                photoRepository.save(photo); // Jedes Foto mit dem Projekt verknüpfen und speichern
            }

            return project;
        } else {
            throw new RuntimeException("Projekt mit ID " + projectId + " nicht gefunden!");
        }
    }

    // Einem Projekt mehrere Notizen hinzufügen
    public Project addNotesToProject(Long projectId, List<Note> notes) {
        Optional<Project> projectOpt = projectRepository.findById(projectId);

        if (projectOpt.isPresent()) {
            Project project = projectOpt.get();

            for (Note note : notes) {
                note.setProject(project);
                noteRepository.save(note); // Jede Notiz mit dem Projekt verknüpfen und speichern
            }

            return project;
        } else {
            throw new RuntimeException("Projekt mit ID " + projectId + " nicht gefunden!");
        }
    }

    // Projekt Bewertungsstrategie setzen ("average" oder "weighted")
    public void setRatingStrategy(RatingStrategy strategy) {
        this.ratingStrategy = strategy;
    }

    // Projekt Bewertung berechnen
    public double calculateProjectRating(Long projectId) {
        List<Rating> ratings = projectRepository.findById(projectId).map(Project::getRatings).orElse(new ArrayList<>());
        return ratingStrategy.calculateRating(ratings);
    }

    /*
    // 3D-Modell für ein Projekt rendern
    public ThreeD render3DModelForProject(Long projectId) {
        Optional<Project> projectOpt = projectRepository.findById(projectId);

        if (projectOpt.isPresent()) {
            Project project = projectOpt.get();
            List<Photo> photos = photoRepository.findByProjectId(projectId);

            if (photos.isEmpty()) {
                throw new RuntimeException("Keine Fotos für das Projekt gefunden! Rendering nicht möglich.");
            }

            // Simuliertes Rendering des 3D-Modells
            ThreeD threeDModel = new ThreeD();
            threeDModel.setProject(project);
            threeDModel.setFilepath("/3dmodels/" + project.getId() + "_model.obj");
            threeDModel.setFilename(project.getName() + "_model.obj");

            return threeDRepository.save(threeDModel);
        } else {
            throw new RuntimeException("Projekt mit ID " + projectId + " nicht gefunden!");
        }
    }
    */

    // 3D-Modell für ein Projekt rendern
    public ThreeD render3DModelForProject(Long projectId) {
        Optional<Project> projectOpt = projectRepository.findById(projectId);
        if (projectOpt.isEmpty()) {
            throw new RuntimeException("Projekt mit ID " + projectId + " nicht gefunden!");
        }

        Project project = projectOpt.get();
        List<Photo> photos = photoRepository.findByProjectId(projectId);

        if (photos.isEmpty()) {
            throw new RuntimeException("Keine Fotos für das Projekt gefunden! Rendering nicht möglich.");
        }

        // Bildpfade extrahieren
        List<String> imagePaths = photos.stream().map(Photo::getFilepath).collect(Collectors.toList());
        String jsonResult = processImages(imagePaths);

        // Prüfen, ob bereits ein 3D-Modell existiert
        Optional<ThreeD> existingModel = threeDRepository.findByProjectId(projectId);

        ThreeD threeDModel;
        if (existingModel.isPresent()) {
            // Falls bereits vorhanden, aktualisiere es
            threeDModel = existingModel.get();
            threeDModel.setFilepath("/3dmodels/" + projectId + "_model.obj");
            threeDModel.setFilename("project_" + projectId + "_model.obj");
        } else {
            // Neues 3D-Modell erstellen
            threeDModel = new ThreeD();
            threeDModel.setProject(project);
            threeDModel.setFilepath("/3dmodels/" + projectId + "_model.obj");
            threeDModel.setFilename("project_" + projectId + "_model.obj");
        }

        return threeDRepository.save(threeDModel);
    }

    private String processImages(List<String> imagePaths) {
        if (imagePaths == null || imagePaths.isEmpty()) {
            throw new IllegalArgumentException("Es muss mindestens ein Bild übergeben werden.");
        }

        List<Point3D_F64> pointCloud = new ArrayList<>();

        for (String path : imagePaths) {
            File imageFile = new File("images" + File.separator + path); // Setzt den relativen Pfad
            if (!imageFile.exists()) {
                throw new IllegalArgumentException("Bilddatei nicht gefunden: " + imageFile.getAbsolutePath());
            }

            System.out.println("Verarbeite Bild: " + imageFile.getAbsolutePath());
        }

        // Beispielhafte Punktwolke
        pointCloud.add(new Point3D_F64(0, 0, 0));
        pointCloud.add(new Point3D_F64(1, 1, 1));
        pointCloud.add(new Point3D_F64(2, 2, 2));

        return convertPointCloudToJson(pointCloud);
    }

    private String convertPointCloudToJson(List<Point3D_F64> pointCloud) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<double[]> jsonPoints = pointCloud.stream()
                .map(p -> new double[]{p.x, p.y, p.z})
                .collect(Collectors.toList());

        try {
            return objectMapper.writeValueAsString(jsonPoints);
        } catch (IOException e) {
            throw new RuntimeException("Fehler beim Konvertieren der Punktwolke in JSON", e);
        }
    }

//    public ThreeD render3DModelForProject(Long projectId) {
//        Optional<Project> projectOpt = projectRepository.findById(projectId);
//        if (projectOpt.isEmpty()) {
//            throw new RuntimeException("Projekt mit ID " + projectId + " nicht gefunden!");
//        }
//
//        Project project = projectOpt.get();
//        List<Photo> photos = photoRepository.findByProjectId(projectId);
//        if (photos.isEmpty()) {
//            throw new RuntimeException("Keine Fotos für das Projekt gefunden! Rendering nicht möglich.");
//        }
//
//        List<String> imagePaths = photos.stream().map(Photo::getFilepath).collect(Collectors.toList());
//        String objFilePath = generate3DModel(imagePaths, projectId);
//
//        Optional<ThreeD> existingModel = threeDRepository.findByProjectId(projectId);
//        ThreeD threeDModel;
//        if (existingModel.isPresent()) {
//            threeDModel = existingModel.get();
//            threeDModel.setFilepath(objFilePath);
//        } else {
//            threeDModel = new ThreeD();
//            threeDModel.setProject(project);
//            threeDModel.setFilepath(objFilePath);
//            threeDModel.setFilename("project_" + projectId + "_model.obj");
//        }
//
//        return threeDRepository.save(threeDModel);
//    }
//
//    private String generate3DModel(List<String> imagePaths, Long projectId) {
//        List<Point3D_F64> pointCloud = new ArrayList<>();
//        File objFile = new File("3dmodels/project_" + projectId + ".obj");
//
//        try {
//            MetricFromUncalibratedPairwiseGraph metric = new MetricFromUncalibratedPairwiseGraph();
//            var config = new ConfigSimilarImagesSceneRecognition();
//            var sceneStructure = new SceneStructureMetric(false);
//
//            // LookUpSimilarImages erstellen
//            SimilarImagesSceneRecognition<GrayU8, ?> similarImages =
//                    FactorySceneReconstruction.createSimilarImages(config, ImageType.single(GrayU8.class));
//
//            // PairwiseImageGraph erstellen
//            PairwiseImageGraph pairwise = new PairwiseImageGraph();
//
//            // LookUpCameraInfo erstellen
//            LookUpCameraInfo cameraInfo = new LookUpCameraInfo();
//
//            // Bilder laden
//            for (String path : imagePaths) {
//                GrayU8 image = UtilImageIO.loadImage(path, GrayU8.class);
//                similarImages.addImage(new File(path).getName(), image);
//            }
//
//            similarImages.fixate(); // Fixiert die Bildbeziehungen
//
//            // Rekonstruktion durchführen
//            metric.process(similarImages, cameraInfo, pairwise);
//            metric.getLargestScene().toMetric(sceneStructure);
//
//            for (int i = 0; i < sceneStructure.points.size; i++) {
//                var worldPoint = sceneStructure.points.get(i);
//                if (worldPoint != null) {
//                    pointCloud.add(new Point3D_F64(
//                            worldPoint.coordinate[0],
//                            worldPoint.coordinate[1],
//                            worldPoint.coordinate[2]
//                    ));
//                }
//            }
//
//            saveAsObj(pointCloud, objFile);
//        } catch (Exception e) {
//            throw new RuntimeException("Fehler bei der 3D-Rekonstruktion", e);
//        }
//
//        return objFile.getAbsolutePath();
//    }
//
//    private void saveAsObj(List<Point3D_F64> points, File file) {
//        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
//            for (Point3D_F64 point : points) {
//                writer.write("v " + point.x + " " + point.y + " " + point.z);
//                writer.newLine();
//            }
//        } catch (IOException e) {
//            throw new RuntimeException("Fehler beim Speichern der OBJ-Datei", e);
//        }
//    }

}
