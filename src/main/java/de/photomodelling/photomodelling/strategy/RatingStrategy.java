package de.photomodelling.photomodelling.strategy;

import de.photomodelling.photomodelling.model.Rating;

import java.util.List;

public interface RatingStrategy {
    double calculateRating(List<Rating> ratings);
}
