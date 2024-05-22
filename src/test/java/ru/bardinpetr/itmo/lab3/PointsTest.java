package ru.bardinpetr.itmo.lab3;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.bardinpetr.itmo.lab3.data.dao.impl.UserDAO;
import ru.bardinpetr.itmo.lab3.data.models.PointResult;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static ru.bardinpetr.itmo.lab3.MockFactory.createPointResult;
import static ru.bardinpetr.itmo.lab3.MockFactory.createUser;

@Slf4j
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PointsTest {

    private static TestDBProvider db;

    private static UserDAO userDAO;


    @BeforeAll
    static void beforeAll() {
        db = new TestDBProvider();

        userDAO = new UserDAO();
        userDAO.setManager(db.getEntityManager());
    }

    @AfterAll
    static void afterAll() {
        db.close();
    }

    @Test
    void shouldStoreUsers() {
        var user = createUser();
        userDAO.insert(user);

        var users = userDAO.findAll();
        assertThat(users)
                .filteredOn(i -> i.getLogin().equals(user.getLogin()))
                .hasSize(1)
                .extracting(i -> tuple(i.getLogin(), i.getPasswordHash(), i.getPointResults().size()))
                .containsOnly(tuple(user.getName(), user.getPasswordHash(), 0));
    }

    @Test
    void shouldStorePointResults() {
        var user = createUser();
        var result = createPointResult();

        userDAO.insert(user);
        var userId = userDAO.findByLogin(user.getLogin()).get().getId();

        userDAO.addPointResult(user, result);

        var dbResults = db
                .getEntityManager()
                .createNativeQuery(
                        "select * from point_result r " +
                                "where r.owner_id = ?"
                )
                .setParameter(1, userId)
                .getResultList();

        assertThat(dbResults).hasSize(1);
        var dbResult = (Object[]) dbResults.get(0);

        assertThat(result)
                .extracting(
                        i -> i.getPoint().getX(),
                        i -> i.getPoint().getY(),
                        i -> i.getArea().getR(),
                        PointResult::getIsInside
                )
                .containsExactly(dbResult[5], dbResult[6], dbResult[4], dbResult[2]);
    }

    @Test
    void shouldReturnPointResultsByOwner() {
        var users = Stream
                .generate(MockFactory::createUser)
                .limit(3)
                .toList();
        users.forEach(userDAO::insert);

        var results = Stream
                .generate(MockFactory::createPointResult)
                .limit(users.size() * 3L)
                .toList();

        var userResults = users
                .stream()
                .collect(Collectors.toMap(
                        i -> i,
                        i -> results
                ));

        userResults.forEach((u, r) ->
                r.forEach(i -> userDAO.addPointResult(u, i))
        );


        userResults.forEach((u, r) -> {
            assertThat(userDAO.getPointResults(u))
                    .usingElementComparatorIgnoringFields("id")
                    .containsExactlyElementsOf(results);
        });
    }
}
