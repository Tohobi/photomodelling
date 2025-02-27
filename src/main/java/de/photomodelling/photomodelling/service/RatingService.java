package de.photomodelling.photomodelling.service;

import de.photomodelling.photomodelling.model.Project;
import de.photomodelling.photomodelling.model.Rating;
import de.photomodelling.photomodelling.repository.ProjectRepository;
import de.photomodelling.photomodelling.repository.RatingRepository;
import de.photomodelling.photomodelling.strategy.AverageRatingStrategy;
import de.photomodelling.photomodelling.strategy.RatingStrategy;
import de.photomodelling.photomodelling.strategy.WeightedRatingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RatingService {

    @Autowired
    private final RatingRepository ratingRepository;

    @Autowired
    private final ProjectRepository projectRepository;

    private final AverageRatingStrategy averageRatingStrategy;
    private final WeightedRatingStrategy weightedRatingStrategy;

    private RatingStrategy currentStrategy;

    @Autowired
    public RatingService(RatingRepository ratingRepository, ProjectRepository projectRepository, AverageRatingStrategy averageRatingStrategy, WeightedRatingStrategy weightedRatingStrategy) {
        this.ratingRepository = ratingRepository;
        this.projectRepository = projectRepository;
        this.averageRatingStrategy = averageRatingStrategy;
        this.weightedRatingStrategy = weightedRatingStrategy;
        this.currentStrategy = averageRatingStrategy;
    }

    public List<Rating> findRatingById(Long idFilter) {
        List<Rating> ratingList = new ArrayList<>();
        if (idFilter == null) {
            ratingRepository.findAll().forEach(ratingList::add);
        } else {
            ratingRepository.findById(idFilter).ifPresent(ratingList::add);
        }
        return ratingList;
    }

    public Rating saveRating(Rating rating) {
        if (rating.getProject() == null || rating.getProject().getId() == null) {
            throw new RuntimeException("Ein Rating muss einem gültigen Projekt zugeordnet sein.");
        }

        // Hole das existierende Projekt aus der Datenbank
        Optional<Project> projectOpt = projectRepository.findById(rating.getProject().getId());

        if (projectOpt.isPresent()) {
            rating.setProject(projectOpt.get());  // Setze die korrekte Project-Referenz
            return ratingRepository.save(rating); // Speichere das Rating mit der validen Project-Referenz
        } else {
            throw new RuntimeException("Projekt mit ID " + rating.getProject().getId() + " nicht gefunden.");
        }
    }

    public List<Rating> findRatingsByProjectId(Long projectId) {
        return ratingRepository.findByProjectId(projectId);
    }

    public double getAverageRatingForProject(Long projectId) {
        List<Rating> ratings = findRatingsByProjectId(projectId);
        return currentStrategy.calculateRating(ratings);
    }

    public void changeStrategy(String strategyType) {
        if ("weighted".equalsIgnoreCase(strategyType)) {
            this.currentStrategy = weightedRatingStrategy;
        } else {
            this.currentStrategy = averageRatingStrategy; // Fallback zu Average
        }
    }
}
