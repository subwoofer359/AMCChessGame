package org.amc.game.chessserver;

import org.amc.Authorities;
import org.amc.DAOException;
import org.amc.User;
import org.amc.dao.DAOInterface;
import org.amc.game.chess.HumanPlayer;
import org.amc.game.chess.Player;
import org.amc.game.chessserver.spring.EmailValidator;
import org.amc.game.chessserver.spring.FullNameValidator;
import org.amc.game.chessserver.spring.PasswordValidator;
import org.amc.game.chessserver.spring.UserNameValidator;
import org.apache.log4j.Logger;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collections;

import javax.annotation.Resource;


@Controller
@RequestMapping("/signup")
public class SignUpController {

    private static final Logger logger = Logger.getLogger(SignUpController.class);
    
    
    static final String MESSAGE_MODEL_ATTR = "MESSAGE";
    static final String ERRORS_MODEL_ATTR = "errors";
    static final String SUCCESS_MSG = "account created";
    static final String ERROR_MSG = "Trouble creating account";
    static final String DEFAULT_AUTHORITY = "ROLE_USER";
    
    private PasswordEncoder encoder;
    
    private DAOInterface<User> userDAO;
    
    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView signUp(@ModelAttribute UserForm user , BindingResult errors) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("forward:/Login.jsp");
        
        checkFullNameValid(user.getFullName(), errors);
        checkUserNameValid(user.getUserName(), errors);
        checkPasswordValid(user.getPassword(), errors);        
        checkEmailAddressValid(user.getEmailAddress(), errors);

        try {
            if (errors.hasErrors()) {
            	mav.getModel().put(ERRORS_MODEL_ATTR, errors);
            	mav.getModel().put("userForm", user);
            } else {
                createUserEntity(user);
                mav.getModel().put(MESSAGE_MODEL_ATTR, SUCCESS_MSG);
            }
        } catch(DAOException dao){
            mav.getModel().put(MESSAGE_MODEL_ATTR, ERROR_MSG);
            logger.error("Error on accessing database:" + dao.getMessage());
            dao.printStackTrace();
        }
        return mav;
    }
    
    private void checkUserNameValid(String userName, Errors errors) {
    	Validator userNameValidator = new UserNameValidator(userDAO);
    	userNameValidator.validate(userName, errors);
    }
    
    private void checkEmailAddressValid(String emailAddr, Errors errors) {
    	Validator emailAddrValiditor = new EmailValidator();
    	emailAddrValiditor.validate(emailAddr, errors);
    }
 
    private void checkFullNameValid(String fullName, Errors errors) {
        Validator fullNameValidator = new FullNameValidator();
        fullNameValidator.validate(fullName, errors);
    }
    
    private void checkPasswordValid(String password, Errors errors) {
        Validator passwordValidator = new PasswordValidator();
        passwordValidator.validate(password, errors);
    }

    private void createUserEntity(UserForm user) throws DAOException {
    	Player player = new HumanPlayer(user.getFullName());
        player.setUserName(user.getUserName());
        createEntryInUserTable(player, user.getPassword(), user.getEmailAddress());
    }
    
    void createEntryInUserTable(Player player, String password, String emailAddress) throws DAOException {
        User user = new User();
        user.setName(player.getName());
        user.setUserName(player.getUserName());
        user.setPassword(encoder.encode(password).toCharArray());
        user.setPlayer(player);
        user.setEmailAddress(emailAddress);
        addDefaultAuthorities(user);
        userDAO.addEntity(user);
        
    }
    
    void addDefaultAuthorities(User user) {
        Authorities authorities = new Authorities();
        authorities.setAuthority(DEFAULT_AUTHORITY);
        authorities.setUser(user);
        user.setAuthorities(Collections.singletonList(authorities));
    }
    
    @Resource(name="myUserDAO")
    public void setUserDAO(DAOInterface<User> userDAO){
        this.userDAO = userDAO;
    }
    
    @Resource(name="myPasswordEncoder")
    public void setPasswordEncoder(PasswordEncoder encoder){
        this.encoder = encoder;
    }
    
    public static class UserForm {
        private String fullName;
        private String userName;
        private String password;
        private String emailAddress;
        public String getFullName() {
            return fullName;
        }
        public String getUserName() {
            return userName;
        }
        public String getPassword() {
            return password;
        }
        public String getEmailAddress() {
            return emailAddress;
        }
        public void setFullName(String fullName) {
            this.fullName = fullName;
        }
        public void setUserName(String userName) {
            this.userName = userName;
        }
        public void setPassword(String password) {
            this.password = password;
        }
        public void setEmailAddress(String emailAddress) {
            this.emailAddress = emailAddress;
        }
        
        @Override
        public String toString() {
            return fullName + "(" + userName + ") -" + emailAddress;
        }
    }
    
    
}
