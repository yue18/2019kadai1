package itecafe;

import java.util.Scanner;

public class Security {
    private static final String password = "Cafe_Pumpkin";  //ITECafeシステムのパスワード
    
    public static void logIn() {  //ログインは試作機能
        Scanner scan = new Scanner(System.in);
        System.out.print("パスワードを入力(Enterで決定)※試作機能.パスワードは Cafe_Pumpkin :");
        while(!scan.nextLine().equals(password)) {
            System.out.println("－－【入力エラー:パスワードが違います】－－");
            System.out.print("ITECafeシステムのパスワードを入力してください(Enterで決定):");
        }
        System.out.println("ログイン成功.\n");
    }
}
