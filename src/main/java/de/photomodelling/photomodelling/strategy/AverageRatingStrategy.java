package de.photomodelling.photomodelling.strategy;

import de.photomodelling.photomodelling.model.Rating;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AverageRatingStrategy implements RatingStrategy {
    @Override
    public double calculateRating(List<Rating> ratings) {
        return ratings.stream().mapToInt(Rating::getScore).average().orElse(0);
    }
}
