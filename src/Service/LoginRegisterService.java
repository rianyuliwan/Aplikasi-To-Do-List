package Service;

import Entity.LoginRegister;

public interface LoginRegisterService {
    public void registerUser(LoginRegister loginRegister);
    boolean loginService(String username, String password);
} 