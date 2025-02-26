package de.photomodelling.photomodelling.observer;

import de.photomodelling.photomodelling.model.Project;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProjectNotifier {
    private List<ProjectObserver> observers = new ArrayList<>();

    public void addObserver(ProjectObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(ProjectObserver observer) {
        observers.remove(observer);
    }

    public void notifyProjectCreated(Project project) {
        for (ProjectObserver observer : observers) {
            observer.onProjectCreated(project);
        }
    }
}
