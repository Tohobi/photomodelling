package de.photomodelling.photomodelling.controller;

import de.photomodelling.photomodelling.model.Rating;
import de.photomodelling.photomodelling.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/rating")
public class RatingController {

    @Autowired
    private RatingService ratingService;

    @GetMapping
    public List<Rating> getAllRatings() {
        return ratingService.findRatingById(null);
    }

    @GetMapping(path = "/byId/{id}")
    public List<Rating> getRatingById(@PathVariable("id") final Long id) {
        return ratingService.findRatingById(id);
    }

    // Neue POST-Methode zum Erstellen einer Bewertung
    @PostMapping("/create")
    public Rating createRating(@RequestBody Rating rating) {
        return ratingService.saveRating(rating);
    }

    @GetMapping("/project/{projectId}")
    public List<Rating> getRatingsByProject(@PathVariable Long projectId) {
        return ratingService.findRatingsByProjectId(projectId);
    }

    @GetMapping("/project/{projectId}/average")
    public double getAverageRating(@PathVariable Long projectId) {
        return ratingService.getAverageRatingForProject(projectId);
    }

    @PostMapping("/changeStrategy")
    public String changeRatingStrategy(@RequestParam String strategy) {
        ratingService.changeStrategy(strategy);
        return "Strategy changed to: " + strategy;
    }

}
