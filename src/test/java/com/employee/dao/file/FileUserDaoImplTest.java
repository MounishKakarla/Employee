package com.employee.dao.file;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.employee.exception.DataAccessException;
import com.employee.exception.DuplicateUserException;
import com.employee.exception.UserNotFoundException;
import com.employee.model.User;
import com.employee.security.Role;

import org.junit.jupiter.api.AfterEach;


public class FileUserDaoImplTest {
	private FileUserDaoImpl dao;
    private File testFile;

    @BeforeEach
    void setup() {
        testFile = new File("test-users.json");
        /*if (testFile.exists()) {
            testFile.delete();
        }*/
        dao = new FileUserDaoImpl(testFile.getAbsolutePath());
    }

    @AfterEach
    void cleanup() {
        if (testFile.exists()) {
            testFile.delete();
        }
    }
    @Test
    void createUser_success() throws Exception {
        assertDoesNotThrow(()->dao.createUser("mounish", "EMP001", Set.of(Role.EMPLOYEE)));

       // User user = dao.authenticate("mounish", "Wrong"); // password printed in console
       // assertNotNull(user);
    }
    @Test
    void createUser_Failure()throws Exception{
    	dao.createUser("admin","Emp002" , Set.of(Role.MANAGER));
    	
    	assertThrows(DuplicateUserException.class,
    			() ->dao.createUser("admin","Emp003" , Set.of(Role.ADMIN) ));
    }
    
     @Test
   void authenticate_Success()throws Exception{
   	User user =dao.authenticate("mounish", "Mouni@2003");
   	assertNotNull(user);
   	
   }
    @Test
    void authenticate_Failure()throws Exception{
    	User user=dao.authenticate("mounish", "wgydqd");
    	assertNotNull(user);
    }
    
    
    @Test
    void assignRole_Success() throws Exception {
        
         assertDoesNotThrow(()-> dao.assignRole("mounish", Set.of(Role.ADMIN, Role.MANAGER)));
     
    }
    @Test
    void assignRole_Failure()throws Exception {

    	UserNotFoundException  exception=assertThrows(UserNotFoundException.class, () ->
            dao.assignRole("ghost", Set.of(Role.ADMIN))
        );
       assertEquals(
          "User not found",
               exception.getMessage()
           );
    }
  @Test
   void changePassword_Success() throws Exception{
	   assertDoesNotThrow(()->dao.changePassword("mounish", "Mouni@2003")); 
   }
   @Test
   void changePassword_Failure()throws Exception{
	   UserNotFoundException exception=assertThrows(UserNotFoundException.class,()->dao.changePassword("ghost", "Mouni@2003"));
	   assertEquals("User Not Found",exception.getMessage());
	   
   }
   
   @Test
   void resetPassword_Success()throws Exception{
	   assertDoesNotThrow(()->dao.resetPassword("mounish"));
   }
  
 @Test
 void resetPassword_Failure()throws Exception{
	 UserNotFoundException exception=assertThrows(UserNotFoundException.class,()->dao.resetPassword("ghost"));
	   assertEquals("User Not Found",exception.getMessage());
 }
}
 


