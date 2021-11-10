package com.bolsheviks.APMS.domain.Project;

import com.bolsheviks.APMS.domain.BaseEntity;
import com.bolsheviks.APMS.domain.ProjectProposal.ProjectProposal;
import com.bolsheviks.APMS.domain.Stage.Stage;
import com.bolsheviks.APMS.domain.User.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "projects")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Project extends BaseEntity {

    private String name;
    @ManyToOne
    private User userCaptain;
    @ManyToMany
    private List<User> usersMembersList;
    @ManyToOne
    private User userProjectManager;
    @ManyToMany
    private List<User> usersConsultantsList;
    private ProjectStatus projectStatus;
    private String information;
    @OneToMany
    private List<Stage> stageList;

    public Project(ProjectProposal projectProposal, User userCreator, User userProjectManager) {
        this.name = projectProposal.getName();
        this.userCaptain = userCreator;
        this.userProjectManager = userProjectManager;
        this.usersConsultantsList = List.copyOf(projectProposal.getConsultantList());
        this.projectStatus = ProjectStatus.IN_PROCESS;
        this.information = projectProposal.getInformation();
        this.stageList = List.copyOf(projectProposal.getStageList());
    }

    public boolean containsUserWithModifyRights(User user) {
        return userCaptain.equals(user)
                || userProjectManager.equals(user)
                || usersMembersList.contains(user);
    }

    public boolean containsUser(User user) {
        return containsUserWithModifyRights(user)
                || usersConsultantsList.contains(user);
    }
}
