package org.amc.game.chessserver;

import org.amc.Authorities;
import org.amc.DAOException;
import org.amc.User;
import org.amc.dao.DAO;
import org.amc.game.chess.HumanPlayer;
import org.amc.game.chess.Player;
import org.amc.game.chessserver.spring.EmailValidator;
import org.amc.game.chessserver.spring.UserNameValidator;
import org.apache.log4j.Logger;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;

import javax.annotation.Resource;


@Controller
@RequestMapping("/signup")
public class SignUpController {

    private static final Logger logger = Logger.getLogger(SignUpController.class);
    
    
    static final String MESSAGE_MODEL_ATTR = "MESSAGE";
    static final String ERRORS_MODEL_ATTR = "ERRORS";
    static final String SUCCESS_MSG = "account created";
    static final String USERTAKEN_MSG = "Username is already taken";
    static final String ERROR_MSG = "Trouble creating account";
    static final String DEFAULT_AUTHORITY = "ROLE_USER";
    static final String INVALID_EMAIL_ADDRESS_MSG = "Invalid Email Address";
    
    private PasswordEncoder encoder;
    
    private DAO<User> userDAO;
    
    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView signUp(Errors errors, String name, String userName, String password, String emailAddress) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("redirect:/Login.jsp");
        
        checkUserNameValid(userName, errors);
        checkEmailAddressValid(emailAddress, errors);
        try {
            if (errors.hasErrors()) {
            	mav.getModel().put("errors", errors);
            	if(isUserNameInvalid(errors)) {
            		mav.getModel().put(EmailValidator.EMAIL_ADDR_FIELD, USERTAKEN_MSG);
            	} else if(isEmailAddressInvalid(errors)) {
            		mav.getModel().put(ERRORS_MODEL_ATTR, INVALID_EMAIL_ADDRESS_MSG);
            	}
            } else {
                createUserEntity(name, userName, password, emailAddress);
                mav.getModel().put(MESSAGE_MODEL_ATTR, SUCCESS_MSG);
            }
        } catch(DAOException dao){
            mav.getModel().put(ERRORS_MODEL_ATTR, ERROR_MSG);
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
    
    private boolean isUserNameInvalid(Errors errors) {
    	return !errors.getFieldErrors(UserNameValidator.USERNAME_FIELD).isEmpty();
    }
    
    private boolean isEmailAddressInvalid(Errors errors) {
    	return !errors.getFieldErrors("emailAddress").isEmpty();
    }

    private void createUserEntity(String name, String userName, String password, String emailAddress) throws DAOException {
    	Player player = new HumanPlayer(name);
        player.setUserName(userName);
        createEntryInUserTable(player, password, emailAddress);
    }
    
    void createEntryInUserTable(Player player, String password, String emailAddress) throws DAOException {
        User user = new User();
        user.setName(player.getName());
        user.setUserName(player.getUserName());
        user.setPassword(encoder.encode(password).toCharArray());
        user.setPlayer(player);
        addDefaultAuthorities(user);
        userDAO.addEntity(user);
        
    }
    
    String hashPassword(String password){
        return encoder.encode(password);
    }
    
    void addDefaultAuthorities(User user) throws DAOException {
        Authorities authorities = new Authorities();
        authorities.setAuthority(DEFAULT_AUTHORITY);
        authorities.setUser(user);
        user.setAuthorities(Arrays.asList(authorities));
    }
    @Resource(name="myUserDAO")
    public void setUserDAO(DAO<User> userDAO){
        this.userDAO = userDAO;
    }
    
    @Resource(name="myPasswordEncoder")
    public void setPasswordEncoder(PasswordEncoder encoder){
        this.encoder = encoder;
    }
}
