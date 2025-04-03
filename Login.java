public class Login {
    private String username;
    private String password;
    private String role;

    Login(String user, String pass, String role){
        username = user;
        password = pass;
        this.role = role;
    }

    public void setUsername(String n){
        username = n;
    }

    public void setPassword(String p){
        password = p;
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

    public String getRole(){
        return role;
    }
}
