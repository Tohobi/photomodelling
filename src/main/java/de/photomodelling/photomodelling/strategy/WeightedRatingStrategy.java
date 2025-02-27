package de.photomodelling.photomodelling.strategy;

import de.photomodelling.photomodelling.model.Rating;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WeightedRatingStrategy implements RatingStrategy {
    @Override
    public double calculateRating(List<Rating> ratings) {
        double total = 0;
        double weight = 1.0;
        double weightSum = 0;

        for (Rating rating : ratings) {
            total += rating.getScore() * weight;
            weightSum += weight;
            weight *= 0.9; // Neuere Bewertungen haben mehr Gewicht
        }

        return weightSum == 0 ? 0 : total / weightSum;
    }
}
