package GUI;

public class Register {
    private String username;
    private String password;
    private String confirmPass;
    private String role;

    Register(String user, String pass){
        username = user;
        password = pass;
    }

    Register(String user, String pass, String conPass, String role){
        username = user;
        password = pass;
        confirmPass = conPass;
        this.role = role;
    }

    public void setUsername(String n){
        username = n;
    }

    public void setPassword(String p){
        password = p;
    }

    public void setConfirmPass(String cp){
        confirmPass = cp;
    }

    public void setRole(String r){
        role = r;
    }

    public String getUsername(){
        return username;
    }

    public String getPassword(){
        return password;
    }

    public String getConfirmPass(){
        return confirmPass;
    }

    public String getRole(){
        return role;
    }

    @Override
    public String toString(){
        return "Username: " + username + "\nPassword: " + password + "\nRole: " + role;
    }

}
