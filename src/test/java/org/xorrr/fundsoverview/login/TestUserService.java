package org.xorrr.fundsoverview.login;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class TestUserService {

    private UserService service;

    @Before
    public void setUp() {
        service = new UserServiceImpl();
    }

    @Test
    public void loginFailsWithWrongCredentials() {
        assertNull(service.login("wrong", "wrong"));
    }

    @Test
    public void loginWorksWithCorrectCredentials() {
        assertNotNull(service.login(System.getenv("USER"),
                System.getenv("PASS")));
    }

}
