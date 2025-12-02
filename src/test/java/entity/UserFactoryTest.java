package entity;

import entity.factories.UserFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UserFactoryTest {

    @Test
    void createReturnsNewUser() {
        UserFactory userFactory = new UserFactory();
        User user = userFactory.create("testuser", "password123");

        assertNotNull(user);
        assertEquals("testuser", user.getUserName());
        assertEquals("password123", user.getPassword());
    }
}
