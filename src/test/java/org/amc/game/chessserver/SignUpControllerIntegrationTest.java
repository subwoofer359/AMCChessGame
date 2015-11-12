package org.amc.game.chessserver;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.amc.game.chessserver.spring.EmailValidator;
import org.amc.game.chessserver.spring.FullNameValidator;
import org.amc.game.chessserver.spring.PasswordValidator;
import org.amc.game.chessserver.spring.UserNameValidator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({ "/SpringTestConfig.xml", "/GameServerSecurity.xml",
    "/GameServerWebSockets.xml", "/EmailServiceContext.xml" })
public class SignUpControllerIntegrationTest {

    private static final String URL = "/signup";
    private static final String MODEL_ATTR = "userForm";

    private static final String NAME = "adrian mclaughlin";
    private static final String INVALID_NAME = "carla_rae_stephenson";
    private static final String USER_NAME = "adrian";
    private static final String PASSWORD = "Password1";
    private static final String INVALID_PASSWORD = "ABCD.212";
    private static final String EMAIL_ADDRESS = "adrian@adrianmclaughlin.ie";
    private static final String FULL_NAME_FIELD = "fullName";
    private static final String USERNAME_FIELD = "userName";
    private static final String PASSWORD_FIELD = "password";
    private static final String EMAILADDRESS_FIELD = "emailAddress";

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    private DatabaseSignUpFixture signUpfixture = new DatabaseSignUpFixture();

    @Before
    public void setUp() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        this.signUpfixture.setUp();

    }

    @After
    public void tearDown() throws Exception {
        this.signUpfixture.tearDown();
    }

    @Test
    public void testAddUser() throws Exception {
        addUser(USER_NAME);
        addUser("sarah");
    }

    @Test
    public void testAddSameUser() throws Exception {
        addUser(USER_NAME);
        addUserError(USER_NAME);
    }

    private void addUser(String userName) throws Exception {
        this.mockMvc.perform(
                        post(URL).param(FULL_NAME_FIELD, NAME).param(USERNAME_FIELD, userName)
                                        .param(PASSWORD_FIELD, PASSWORD)
                                        .param(EMAILADDRESS_FIELD, EMAIL_ADDRESS))
                        .andExpect(status().isOk())
                        .andExpect(model().attributeExists(SignUpController.MESSAGE_MODEL_ATTR));
    }

    private void addUserError(String userName) throws Exception {
        this.mockMvc.perform(
                        post(URL).param(FULL_NAME_FIELD, NAME).param(USERNAME_FIELD, userName)
                                        .param(PASSWORD_FIELD, PASSWORD)
                                        .param(EMAILADDRESS_FIELD, EMAIL_ADDRESS))
                        .andExpect(status().isOk())
                        .andExpect(model().attributeExists(SignUpController.ERRORS_MODEL_ATTR))
                        .andExpect(model().attributeHasFieldErrorCode(MODEL_ATTR, USERNAME_FIELD,
                                        UserNameValidator.USERNAME_TAKEN_ERROR));

    }

    @Test
    public void testInvalidEmail() throws Exception {

        this.mockMvc.perform(
                        post(URL).param(FULL_NAME_FIELD, NAME).param(USERNAME_FIELD, USER_NAME)
                                        .param(PASSWORD_FIELD, PASSWORD)
                                        .param(EMAILADDRESS_FIELD, "eoeoeo"))
                        .andExpect(status().isOk())
                        .andExpect(model().attributeExists(SignUpController.ERRORS_MODEL_ATTR))
                        .andExpect(model().attributeHasFieldErrorCode(MODEL_ATTR,
                                        EMAILADDRESS_FIELD, EmailValidator.INVALID_EMAIL_ERROR));
    }

    @Test
    public void testInvalidFullName() throws Exception {
        testFullNameError(INVALID_NAME, FullNameValidator.INVALID_FULLNAME_ERROR);
    }

    @Test
    public void testNoFullName() throws Exception {
        testFullNameError("", FullNameValidator.NO_FULLNAME_ERROR);
    }

    private void testFullNameError(String fullName, String errorCode) throws Exception {
        this.mockMvc.perform(
                        post(URL).param(FULL_NAME_FIELD, fullName).param(USERNAME_FIELD, USER_NAME)
                                        .param(PASSWORD_FIELD, PASSWORD)
                                        .param(EMAILADDRESS_FIELD, EMAIL_ADDRESS))
                        .andExpect(status().isOk())
                        .andExpect(model().attributeExists(SignUpController.ERRORS_MODEL_ATTR))
                        .andExpect(model().attributeHasFieldErrorCode(MODEL_ATTR, FULL_NAME_FIELD,
                                        errorCode));
    }

    @Test
    public void testInvalidPassword() throws Exception {
        testPasswordError(INVALID_PASSWORD, PasswordValidator.INVALID_PASSWORD_ERROR);
    }

    @Test
    public void testNoPassword() throws Exception {
        testPasswordError("", PasswordValidator.NO_PASSWORD_ERROR);
    }

    private void testPasswordError(String password, String errorCode) throws Exception {
        this.mockMvc.perform(
                        post(URL).param(FULL_NAME_FIELD, NAME).param(USERNAME_FIELD, USER_NAME)
                                        .param(PASSWORD_FIELD, password)
                                        .param(EMAILADDRESS_FIELD, EMAIL_ADDRESS))
                        .andExpect(status().isOk())
                        .andExpect(model().attributeExists(SignUpController.ERRORS_MODEL_ATTR))
                        .andExpect(model().attributeHasFieldErrorCode(MODEL_ATTR, PASSWORD_FIELD,
                                        errorCode));
    }
}
