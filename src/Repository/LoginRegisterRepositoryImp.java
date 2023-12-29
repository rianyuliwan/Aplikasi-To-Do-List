package Repository;

import Entity.LoginRegister;

public class LoginRegisterRepositoryImp implements LoginRegisterRepository {

    public LoginRegister[] loginRegisters = new LoginRegister[10];

    @Override
    public void save(LoginRegister loginRegister) {
        boolean usernameExists = false;

        for (LoginRegister existingUser : this.loginRegisters) {
            if (existingUser != null && existingUser.getUsername().equals
        (loginRegister.getUsername())) {
                usernameExists = true;
                break;
            }
        }

        if (!usernameExists) {
            String password = loginRegister.getPassword();
            if (password.length() >= 8 && !password.equals(
                    password.toLowerCase())) {
                int currentIndex = 0;
                boolean added = false;
                while (currentIndex < this.loginRegisters.length && !added) {
                    if (this.loginRegisters[currentIndex] == null) {
                        this.loginRegisters[currentIndex] = loginRegister;
                        added = true;
                    }
                    currentIndex++;
                }
                if (!added) {
                    throw new RuntimeException
                        ("Cannot add more users, the capacity is full!");
                }
            } else {
                throw new RuntimeException
                        ("Password must be at least 8 characters long and "
                                + "contain at least one uppercase letter.");
            }
        } else {
            throw new RuntimeException
                        ("Username already exists! "
                                + "Please choose a different username.");
        }
    }

    @Override
    public boolean LoginUser(String username, String paswword) {
        for (int i = 0; i < this.loginRegisters.length; i++) {
            if (this.loginRegisters[i] != null && this.loginRegisters[i]
                    .getUsername().equals(username) &&
                    this.loginRegisters[i].getPassword().equals(paswword)) {
                return true;
            }
        }
        return false;
    }
}