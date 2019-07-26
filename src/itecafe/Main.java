package itecafe;

import static itecafe.Items_IG.itemInstancesGenerate;
import static itecafe.Security.logIn;

public class Main {

    public static void main(String[] args) {
        logIn();                   //ログイン（パスワードのみ）
        
        itemInstancesGenerate();   //商品インスタンス生成
        
        System.out.println("■■■ ITECafeシステム ■■■");
        
        Items.printItemList();     //メニューを表示する
        Waiter wt = new Waiter();  //仮想ウェイター呼び出し
        wt.takeOrder();            //仮想ウェイターに注文を受けてもらう
        wt.printInvoice();         //請求書発行
        wt.Accounting();           //会計し終了
    }
}
