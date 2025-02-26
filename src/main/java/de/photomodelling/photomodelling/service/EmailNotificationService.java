package de.photomodelling.photomodelling.service;

import de.photomodelling.photomodelling.model.Project;
import de.photomodelling.photomodelling.observer.ProjectObserver;
import org.springframework.stereotype.Component;

@Component
public class EmailNotificationService implements ProjectObserver {

    @Override
    public void onProjectCreated(Project project) {
        System.out.println("Sende E-Mail: Neues Projekt '" + project.getName() + "' wurde erstellt!");
    }
}
