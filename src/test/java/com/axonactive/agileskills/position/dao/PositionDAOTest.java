package com.axonactive.agileskills.position.dao;

import com.axonactive.agileskills.position.entity.PositionEntity;
import com.axonactive.agileskills.position.entity.PositionStatusEnum;
import com.axonactive.agileskills.position.requiredskill.entity.RequiredSkillEntity;
import com.axonactive.agileskills.position.requiredskill.entity.RequiredTopicEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PositionDAOTest {

    private static EntityManager em;
    private static EntityManagerFactory emf;
    private static PositionDAO positionDAO;
    private static Connection connection;

    @BeforeAll
    static void setUpDB() throws SQLException, IOException {
        String jdbcURL = "jdbc:h2:mem:testdb";
        String username = "sa";
        String password = "1234";

        connection = DriverManager.getConnection(jdbcURL, username, password);

        emf = Persistence.createEntityManagerFactory("agileskills-test");
        em = emf.createEntityManager();
        positionDAO = new PositionDAO(em);

        InputStream inputStream = PositionDAOTest.class.getResourceAsStream("/testDB.sql");
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sqlBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sqlBuilder.append(line).append("\n");
        }
        reader.close();

        Statement statement = connection.createStatement();
        statement.execute(sqlBuilder.toString());
    }

    @AfterAll
    static void tearDown() throws SQLException {
        connection.close();
    }

    private boolean isListOfStringSorted(List<String> listOfStrings) {
        Iterator<String> iter = listOfStrings.iterator();
        String current, previous = iter.next();
        while (iter.hasNext()) {
            current = iter.next();
            if (previous.compareTo(current) > 0) {
                return false;
            }
            previous = current;
        }
        return true;
    }

    @Test
    void findByStatus_TenOpenPosition_ReturnListOfTen() {
        List<PositionEntity> searchResult = positionDAO.findByStatus(PositionStatusEnum.OPEN);
        assertEquals(10, searchResult.size());
        for (PositionEntity positionEntity : searchResult) {
            assertEquals(PositionStatusEnum.OPEN, positionEntity.getStatus());
        }
    }

    @Test
    void searchStatus_NinePositionWithJava_ReturnSortedList() {
        // will also find JavaScript
        List<PositionEntity> searchResult = positionDAO.searchStatus("Java", PositionStatusEnum.OPEN);
        assertEquals(9, searchResult.size());

        // In Position Name & Note
        assertTrue(searchResult.stream()
                .anyMatch(pos -> pos.getName().contains("Java")));
        assertTrue(searchResult.stream()
                .anyMatch(pos -> pos.getNote().contains("Java")));

        List<RequiredSkillEntity> requiredSkillList = searchResult.stream()
                .flatMap(pos -> pos.getRequiredSkillList().stream()).collect(Collectors.toList());

        // In Skill Name & Description
        assertTrue(requiredSkillList.stream()
                .anyMatch(rskill -> rskill.getSkill().getName().contains("Java")));
        assertTrue(requiredSkillList.stream()
                .anyMatch(rskill -> rskill.getSkill().getDescription().contains("Java")));

        List<RequiredTopicEntity> requiredTopicList = requiredSkillList.stream().flatMap(rskill -> rskill.getRequiredTopicList().stream()).collect(Collectors.toList());
        // In topic Name & Topic Description
        assertTrue(requiredTopicList.stream()
                .anyMatch(rtopic -> rtopic.getTopic().getName().contains("Java")));
        assertTrue(requiredTopicList.stream()
                .anyMatch(rtopic -> rtopic.getTopic().getDescription().contains("Java")));

        List<String> positionNameList = searchResult.stream().map(PositionEntity::getName).collect(Collectors.toList());
        assertTrue(isListOfStringSorted(positionNameList));
    }

    @Test
    void searchStatus_SameNameDifferentTeam_ReturnSortedByTeamList() {
        List<PositionEntity> searchResult = positionDAO.searchStatus("Python Engineer", PositionStatusEnum.OPEN);
        assertEquals(4, searchResult.size());
        assertTrue(searchResult.stream().map(PositionEntity::getName).allMatch(e -> e.equals("Python Engineer")));

        List<String> teamNameList = searchResult.stream().map(e -> e.getTeam().getName()).collect(Collectors.toList());

        assertTrue(isListOfStringSorted(teamNameList));
    }

    @Test
    void searchStatus_ClosedPosition_ReturnEmptyList() {
        List<PositionEntity> searchResult = positionDAO.searchStatus("QA Engineer", PositionStatusEnum.OPEN);
        assertEquals(0, searchResult.size());
    }

    @Test
    void searchStatus_BlankWord_ReturnAllOpenSortedList() {
        List<PositionEntity> searchResult = positionDAO.searchStatus("", PositionStatusEnum.OPEN);
        assertEquals(10, searchResult.size());
        for (PositionEntity positionEntity : searchResult) {
            assertEquals(PositionStatusEnum.OPEN, positionEntity.getStatus());
        }
        List<String> positionNameList = searchResult.stream().map(PositionEntity::getName).collect(Collectors.toList());
        assertTrue(isListOfStringSorted(positionNameList));
    }

    @Test
    void searchStatus_NonExistingWord_ReturnEmptyList() {
        List<PositionEntity> searchResult = positionDAO.searchStatus("NonExistingWord", PositionStatusEnum.OPEN);
        assertEquals(0, searchResult.size());
    }

    @Test
    void searchStatus_NameWithWhitespaceAndRandomCase_ReturnList() {
        List<PositionEntity> testEntity = positionDAO.searchStatus(" datABase admInIsTratOr  ", PositionStatusEnum.OPEN);
        assertTrue(testEntity.stream().anyMatch(positionEntity -> positionEntity.getName().contains("Database Administrator")));
    }

    @Test
    void searchStatus_NoteWithWhitespaceAndRandomCase_ReturnList() {
        List<PositionEntity> testEntity = positionDAO.searchStatus(" Nostrum ExerciTationeM ", PositionStatusEnum.OPEN);
        assertTrue(testEntity.stream().anyMatch(positionEntity -> positionEntity.getNote().contains("nostrum exercitationem")));
    }

    @Test
    void searchStatus_SkillNameWithWhitespaceAndRandomCase_ReturnList() {
        List<PositionEntity> testEntity = positionDAO.searchStatus(" JAvA ", PositionStatusEnum.OPEN);

        assertEquals(9, testEntity.size());
        assertTrue(testEntity.stream().flatMap(pos -> pos.getRequiredSkillList().stream()).anyMatch(rskill -> rskill.getSkill().getName().contains("Java")));
    }

    @Test
    void searchStatus_SkillDescriptionWithWhitespaceAndRandomCase_ReturnList() {
        List<PositionEntity> testEntity = positionDAO.searchStatus(" AnDrOid ", PositionStatusEnum.OPEN);
        assertEquals(4, testEntity.size());
        assertTrue(testEntity.stream().flatMap(pos -> pos.getRequiredSkillList().stream()).anyMatch(rskill -> rskill.getSkill().getDescription().contains("Android")));
    }

    @Test
    void searchStatus_TopicDescriptionWithWhitespaceAndRandomCase_ReturnList() {
        List<PositionEntity> testEntity = positionDAO.searchStatus(" FuNctIon dEfiNitIon, argUMeNts, retuRn vAlUes ", PositionStatusEnum.OPEN);
        assertEquals(4, testEntity.size());
        assertTrue(testEntity.stream()
                .flatMap(pos -> pos.getRequiredSkillList().stream())
                .flatMap(rskill -> rskill.getRequiredTopicList().stream())
                .anyMatch(rtopic -> rtopic.getTopic().getDescription().contains("function definition, arguments, return values")));
    }

    @Test
    void searchStatus_TopicNameWithWhitespaceAndRandomCase_ReturnList() {
        List<PositionEntity> testEntity = positionDAO.searchStatus(" DatA StrUctuRes ", PositionStatusEnum.OPEN);
        assertEquals(6, testEntity.size());
        assertTrue(testEntity.stream()
                .flatMap(pos -> pos.getRequiredSkillList().stream())
                .flatMap(rskill -> rskill.getRequiredTopicList().stream())
                .anyMatch(rtopic -> rtopic.getTopic().getName().contains("Data Structures")));
    }
}