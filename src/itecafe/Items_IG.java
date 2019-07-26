package itecafe;

import static itecafe.Items.Categories.*;

public class Items_IG {
    public static void itemInstancesGenerate() {
         /*
         * 商品の属性は、商品番号、カテゴリ（種類）、商品名、サイズ、サイズごとの値段からなる。
         * 商品の種類、名称、値段は最低限入力必須。
         * サイズが存在しない場合、サイズを入力せず、値段をint型でも入力できる。
         * サイズ名を無名にしたい場合は、代わりに"N"を用いる（よって"N"はサイズ名にできない）。
         */
        //どんな順番でインスタンス化しても、商品リストは自動的にカテゴリ別に商品番号の昇順に並べ替えられる
        Items hotCoffee = new Items(ドリンク, "ホットコーヒー", "S,M", "280,330");
        Items tea = new Items(ドリンク, "紅茶", "S,M", "260,310");
        Items iceCoffee = new Items(ドリンク, "アイスコーヒー", "S,M,L,LL", "200,250,300,350");
        Items iceTea = new Items(ドリンク, "アイスティー", "S,M", "260,310");
        Items shortCake = new Items(スイーツ, "ショートケーキ", "N", "400");
        Items cheeseCake = new Items(スイーツ, "チーズケーキ", 400);
        Items chocolateCake = new Items(スイーツ, "チョコレートケーキ", "N,SPECIAL", "450, 1000");
        Items chocoBananaParfait = new Items(スイーツ, "チョコバナナパフェ", 390);
        Items strawberryParfait = new Items(スイーツ, "イチゴパフェ", 390);
        Items meatPasta = new Items(軽食, "ミートパスタ", 650);
        Items mixPizza = new Items(軽食, "ミックスピザ", 700);
        Items croissant = new Items(パン, "クロワッサン", 180);
        Items toastSandwich = new Items(パン, "トーストサンド", 200);
        Items frenchToast = new Items(パン, "フレンチトースト", 210);
    }
}
