package Repository;

import Entity.LoginRegister;

public interface LoginRegisterRepository {
    void save(LoginRegister loginRegister);
    boolean LoginUser(String username, String paswword);
}