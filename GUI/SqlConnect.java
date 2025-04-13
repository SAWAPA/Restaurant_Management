package GUI;

public class SqlConnect {
    private String url;
    private String username;
    private String password;

    private String urlD;
    private String usernameD;
    private String passwordD;

    SqlConnect(){

    }

    SqlConnect(String u, String user, String p){
        url = u;
        username = user;
        password = p;
    }

    public void setUrl(String u){
        url = u;
    }

    public void setUserSql(String user){
        username = user;
    }

    public void setPassSql(String p){
        password = p;
    }

    public String getUrl(){
        return url;
    }

    public String getUserSql(){
        return username;
    }

    public String getPassSql(){
        return password;
    }

    public String getUrlD(){
        urlD = "jdbc:mysql://localhost:3306/restaurant";
        return urlD;
    }

    public String getUserSqlD(){
        usernameD = "root";
        return usernameD;
    }

    public String getPassSqlD(){
        passwordD = "50473";
        return passwordD;
    }
}