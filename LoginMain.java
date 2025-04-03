import java.util.Scanner;

public class LoginMain {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String useername = sc.nextLine();
        String password = sc.nextLine();
        String role = sc.nextLine();

        Login user1 = new Login(useername, password, role);
        System.out.println(user1.getUsername());
    }
}
