package de.photomodelling.photomodelling.strategy;

import de.photomodelling.photomodelling.model.Rating;

import java.util.List;

public class AverageRatingStrategy implements RatingStrategy {
    @Override
    public double calculateRating(List<Rating> ratings) {
        return ratings.stream().mapToInt(Rating::getScore).average().orElse(0);
    }
}
