package Service;

import Entity.LoginRegister;
import Repository.LoginRegisterRepositoryImp;


public class LoginRegisterServiceImp implements LoginRegisterService {
    private LoginRegisterRepositoryImp loginRegisterRepositoryImp;

    public LoginRegisterServiceImp
        (LoginRegisterRepositoryImp loginRegisterRepositoryImp) {
        this.loginRegisterRepositoryImp = loginRegisterRepositoryImp;
    }

    @Override
    public void registerUser(LoginRegister loginRegister) {
        this.loginRegisterRepositoryImp.save(loginRegister);
        System.out.println("Successfully register with username : "
                +loginRegister.getUsername());
    }


    @Override
    public boolean loginService(String username, String password) {
        if (this.loginRegisterRepositoryImp.LoginUser(username, password)) 
        {
            System.out.println("Succes Login");
            return true;
        } else {
            throw new RuntimeException("Invalid Login");
        }
    }
    

}   