package com.bolsheviks.APMS.domain;

import com.bolsheviks.APMS.domain.ProjectProposal.ProjectProposal;
import com.bolsheviks.APMS.domain.ProjectProposal.ProjectProposalRepository;
import com.bolsheviks.APMS.domain.User.Role;
import com.bolsheviks.APMS.domain.User.User;
import com.bolsheviks.APMS.domain.User.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MockService {

    private final ProjectProposalRepository projectProposalRepository;
    private final UserRepository userRepository;

    public MockService(ProjectProposalRepository projectProposalRepository,
                       UserRepository userRepository) {
        this.projectProposalRepository = projectProposalRepository;
        this.userRepository = userRepository;

        createMocks();
    }

    private void createMocks() {
        createProjectProposalMocks();
        createAdmin();
        createUser("Сергей", "Мишутин", "Сергеевич", Role.USER);
        createUser("Сергей", "Макаров", "Сергеевич", Role.USER);
        createUser("Илья", "Лекомцев", "Анатольевич", Role.USER, "user", "user");
        createUser("Дмитрий", "Никитин", "Алексеевич", Role.USER, "captain", "captain");
    }

    private void createAdmin() {
        User admin = new User();
        admin.setLogin("admin");
        admin.setPassword("admin");
        admin.setRole(Role.BUSINESS_ADMINISTRATOR);
        userRepository.save(admin);
    }

    private void createProjectProposalMocks() {
        ProjectProposal firstProjectProposal = new ProjectProposal();
        firstProjectProposal.setName("АСУ проектной деятельностью");
        firstProjectProposal.setInformation("Разработка АСУПД для ИжГТУ");
        List<User> users = new ArrayList<>();
        users.add(createUser("Шишлина", "Наталья", "Васильевна", Role.CURATOR, "manager", "manager"));
        firstProjectProposal.setProjectManagersList(users);
        ArrayList<User> consultants = new ArrayList<>();
        consultants.add(createUser("Пигалев", "Сергей", "Александрович", Role.CURATOR, "curator", "curator"));
        consultants.add(createUser("Сенилов", "Михал", "Андреич", Role.CURATOR));
        firstProjectProposal.setConsultantList(consultants);
        firstProjectProposal.setStageNames("Разработка и анализ требований");
        projectProposalRepository.save(firstProjectProposal);

        ProjectProposal secondProjectProposal = new ProjectProposal();
        secondProjectProposal.setName("Виртуальные лаборатории");
        secondProjectProposal.setInformation("Разработка виртуальных лабораторий на движке Unity");
        List<User> secondUsers = new ArrayList<>();
        secondUsers.add(createUser("Архипов", "Игорь", "Олегович", Role.CURATOR));
        secondProjectProposal.setProjectManagersList(secondUsers);
        ArrayList<User> secondConsultants = new ArrayList<>();
        secondConsultants.add(createUser("Линус", "Торвальдс", "Иванович", Role.CURATOR));
        secondConsultants.add(createUser("Хайсэ", "Сасаки", "Андреич", Role.CURATOR));
        secondProjectProposal.setConsultantList(secondConsultants);
        secondProjectProposal.setStageNames("Разработка и анализ требований/Презентация прототипа/Поддержка продукта");
        projectProposalRepository.save(secondProjectProposal);
    }

    private User createUser(String firstName, String lastName, String patronymic, Role role) {
        User user = new User();
        user.setRole(role);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPatronymic(patronymic);

        userRepository.save(user);
        return user;
    }

    private User createUser(String firstName, String lastName, String patronymic, Role role, String login, String password) {
        User user = new User();
        user.setLogin(login);
        user.setPassword(password);
        user.setRole(role);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPatronymic(patronymic);

        userRepository.save(user);
        return user;
    }
}
