package de.photomodelling.photomodelling.observer;

import de.photomodelling.photomodelling.model.Project;

public interface ProjectObserver {
    void onProjectCreated(Project project);
}
