package itecafe;

import static itecafe.Items.itemFormat;
import static itecafe.Items.lookUpItem;
import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;
import static java.lang.Math.round;
import static java.lang.String.format;
import java.util.ArrayList;
import java.util.Scanner;

public class Waiter{
    /*
     * orderedItems<String[]>には注文された商品の情報が格納される。
     * String[0]…商品のフルネーム（例:"1.ホットコーヒーS"、"14.フレンチトースト"、など）。
     * String[1]…商品番号。
     * String[2]…商品名。
     * String[3]…商品サイズ。
     * String[4]…商品数量。
     */
    private ArrayList<String[]> orderedItems = new ArrayList<>();
    //チケットの有無
    private boolean haveTicket = false;
    //チケットの割引率
    private static double ticketDiscountRate = 0.15;
    //最終的な合計金額の記憶場所
    private long invoiceSum;
    
    public String[] getOrderedItemInfo(String searchName) {
        for(String[] strs: this.orderedItems) {
            if(strs[0].equals(searchName)) {
                return strs;
            }
        }
        return null;
    }
    public void printOrdered() {
        StringBuilder ordered = new StringBuilder();
        ordered.append("\n《注文済み商品》\n\n");
        for(String[] strs: this.orderedItems) {
            ordered.append(itemFormat(strs[0],30)).append("×").append(strs[4]).append("\n");
        }
        System.out.println(ordered);
    }
    public void printOrdered(int outNo) {
        StringBuilder ordered = new StringBuilder();
        for(String[] strs: this.orderedItems) {
            if(strs[1].equals(outNo)) {
                ordered.append(itemFormat(strs[0],30)).append("×").append(strs[4]).append("\n");
            }
        }
        System.out.println(ordered);
    }
    public boolean containsOrdered(String searchName) {
        for(String[] strs: this.orderedItems) {
            if(strs[0].equals(searchName)) {
                return true;
            }
        }
        return false;
    }
    public boolean containsOrdered(int searchNo) {
        for(String[] strs: this.orderedItems) {
            if(strs[1].equals(String.valueOf(searchNo))) {
                return true;
            }
        }
        return false;
    }
    public int containsOrderedNum(int searchNo) {
        int hits = 0;
        for(String[] strs: this.orderedItems) {
            if(strs[1].equals(String.valueOf(searchNo))) {
                hits++;
            }
        }
        return hits;
    }
    public boolean containsOrderedSize(int searchNo, String searchSize) {
        for(String[] strs: this.orderedItems) {
            if(strs[1].equals(String.valueOf(searchNo)) && strs[3].equals(searchSize)) {
                return true;
            }
        }
        return false;
    }
    public void checkTicket() {
        Scanner scan = new Scanner(System.in);
        while(true) {
            System.out.print("割引チケットはお持ちでしょうか.(Enterで決定(y/n)):");
            switch(scan.nextLine().trim().toLowerCase()) {
                case "y":
                    System.out.println("チケットをお預かり致します.");
                    this.haveTicket = true;
                    System.out.println("合計金額より " + (int) (ticketDiscountRate  * 100) + "% の割引が適用されます.\n");
                    return;
                case "n":
                    System.out.println("チケットは無しですね.\n");
                    return;
                default:
                    System.out.println("－－【入力エラー: y か n を半角で入力してください】－－");
            }
        }
    }
    
    private void order(Items orderItem, String orderSize, int orderNum, boolean overWrite) {
        StringBuilder orderSb = new StringBuilder();
        String orderInfo[] = {orderItem.getFullName(orderSize).toString(), String.valueOf(orderItem.getNo()), orderItem.getName(), orderSize, String.valueOf(orderNum)};
        if(overWrite) {
            orderSb.append(orderInfo[0]).append(" の注文数を ×").append(getOrderedItemInfo(orderInfo[0])[4]);
            orderSb.append(" → ×").append(orderNum).append(" に変更しました.\n");
            this.orderedItems.remove(getOrderedItemInfo(orderInfo[0]));
        } else {
            orderSb.append(orderInfo[0]);
            orderSb.append(" を ×").append(orderNum).append(" 注文しました.\n");
        }
        this.orderedItems.add(orderInfo);
        System.out.println(orderSb);
    }
    public void takeOrder() {
        Scanner scan = new Scanner(System.in);
        int inNum = 0; //入力された商品番号
        System.out.println("ご注文を承ります.\n");
        checkTicket();
        while(true) {
            
            //商品番号を入力する
            System.out.println("商品番号を入力してください.(注文完了は 0, 注文取り消しは -1, ");
            System.out.print("メニュー再表示は -2, 注文済み商品の確認は -3 を入力. Enterで決定):");
            
            try {
                String inStr;
                inStr = scan.nextLine();
                inNum = Integer.parseInt(inStr.trim());
                switch(inNum) {
                    case 0:
                        if(!this.orderedItems.isEmpty()) {
                            System.out.println("\n注文内容をご確認ください.");
                            printOrdered();
                            System.out.print("ご注文は以上でよろしいでしょうか？(y/n):");
                            switch(scan.nextLine().trim().toLowerCase()) {
                                case "y":
                                    System.out.println("\nかしこまりました. それではごゆっくりどうぞ.");
                                    return;
                                case "n":
                                    System.out.println("注文を再開します.\n");
                                    continue;
                                default:
                                    System.out.println("y,n以外が入力されたので注文を再開します.\n");
                                    continue;
                            }
                        }
                        System.out.println("－－【入力エラー:1つ以上の商品を注文してください】－－\n");
                        continue;
                    case -1:
                        if(!this.orderedItems.isEmpty()) {
                            while(true) {
                                printOrdered();
                                System.out.print("注文を取り消す商品の商品番号を入力してください(0で注文に戻る):");
                                try {
                                    int cancelNo = parseInt(scan.nextLine());
                                    if(cancelNo != 0) {
                                        cancelOrder(cancelNo);
                                    } else {
                                        System.out.println("注文を再開します.\n");
                                        break;
                                    }
                                } catch(NumberFormatException e) {
                                    System.out.println("－－【入力エラー:使用不可能なデータが含まれているか、数値が大きすぎます。0以上の整数を半角で入力してください】－－");
                                }
                                if(this.orderedItems.isEmpty()) {
                                    break;
                                }
                            }
                            continue;
                        }
                        System.out.println("－－【入力エラー:1つも商品が注文されていません】－－\n");
                        continue;
                    case -2:
                        Items.printItemList();
                        continue;
                    case -3:
                        if(!this.orderedItems.isEmpty()) {
                            printOrdered();
                            continue;
                        }
                        System.out.println("－－【入力エラー:1つも商品が注文されていません】－－\n");
                        continue;
                }
                Items outItem = lookUpItem(inNum);
                System.out.print(outItem.getNo() + "." + outItem.getName() + " を注文しますか？(y/n):");
                switch(scan.nextLine().trim().toLowerCase()) {
                    case "y":
                        boolean exSize = true;  //サイズ入力パートで入力を中断する時にfalseにする
                        boolean overWrite = false;  //商品が注文済み、かつ注文数を上書き変更する時にtrueにする
                        String orderSize = "N";
                        if(outItem.getSizes().size() >= 2) {
                            while(true) {
                                System.out.print(">サイズを入力してください.");
                                if(outItem.getSizes().contains("N")) {
                                    System.out.print("無名のサイズは N で指定できます.");
                                }
                                System.out.print("(0で注文中断):");
                                orderSize = scan.nextLine().trim().toUpperCase();
                                if(outItem.getSizePrice().containsKey(orderSize)) {
                                    if(containsOrdered(outItem.getFullName(orderSize).toString())) {
                                        System.out.println(outItem.getFullName(orderSize) + " は既に注文されています.");
                                        System.out.print("注文数を上書き変更しますか？(y/n):");
                                        switch(scan.nextLine().trim().toLowerCase()) {
                                            case "y":
                                                overWrite = true;
                                                break;
                                            case "n":
                                                System.out.println("－－注文を中断しました－－");
                                                exSize = false;
                                                break;
                                            default:
                                                System.out.println("－－y,n以外が入力されたので注文を中断しました－－");
                                                exSize = false;
                                                break;
                                        }
                                    }
                                    break;
                                }
                                if("0".equals(orderSize) || "+0".equals(orderSize) || "-0".equals(orderSize)) {
                                    System.out.println("－－注文を中断しました－－\n");
                                    exSize = false;
                                    break;
                                } else if(orderSize.contains("�")) {
                                    System.out.println("－－【入力エラー:半角英数字で入力してください】－－");
                                } else {
                                    System.out.println("－－サイズ\"" + orderSize + "\"はありません－－");
                                }
                            }
                        }
                        if(exSize) {
                            while(true) {
                                if(outItem.getSizes().size() != 1) {
                                    System.out.print(">");
                                }
                                System.out.print(">数量を入力してください(0で注文中断):");
                                try {
                                    String orderNum = scan.nextLine().trim();
                                    int orderNumInt = Integer.parseInt(orderNum);
                                    if(orderNumInt != 0) {
                                        if(orderNumInt > 0) {
                                            order(outItem, orderSize, orderNumInt, overWrite);
                                            break;
                                        } else {
                                            System.out.println("－－【入力エラー:0以上の整数を半角で入力してください】－－");
                                        }
                                    } else {
                                        System.out.println("－－注文を中断しました－－\n");
                                        break;
                                    }
                                } catch(NumberFormatException e) {
                                    System.out.println("－－【入力エラー:使用不可能なデータが含まれているか、数値が大きすぎます。0以上の整数を半角で入力してください】－－");
                                }
                            }
                        }
                        break;
                    case "n":
                        System.out.println("－－注文を中断しました－－\n");
                        break;
                    default:
                        System.out.println("－－y,n以外が入力されたので注文を中断しました－－\n");
                }
            } catch(NumberFormatException e) {
                System.out.println("－－【入力エラー:使用不可能なデータが含まれているか、数値が大きすぎます。-2以上の整数を半角で入力してください】－－\n");
            } catch(NullPointerException e) {
                if(inNum > 0) {
                    System.out.println("－－入力された商品番号[" + inNum + "]は存在しませんでした.－－\n");
                } else {
                    System.out.println("－－【入力エラー:-2以上の整数を半角で入力してください】－－\n");
                }
            }
        }
    }
    public void cancelOrder(int cancelNo) {
        StringBuilder cancelSb = new StringBuilder();
        Scanner scan = new Scanner(System.in);
        Items cancelItem = lookUpItem(cancelNo);
        switch(containsOrderedNum(cancelNo)) {
            case 1:
                for(String[] strs: this.orderedItems) {
                    if(strs[1].equals(String.valueOf(cancelNo))) {
                        System.out.print(strs[0] + " の注文を取り消しますか？(y/n):");
                        switch(scan.nextLine()) {
                            case "y":
                                this.orderedItems.remove(strs);
                                System.out.println(strs[0] + " の注文を取り消しました.\n");
                                return;
                            case "n":
                                return;
                            default:
                                return;
                        }
                    }
                }
                break;
            case 0:
                cancelSb.append(cancelItem.getSystemName()).append(" は注文されていません.");
                System.out.println(cancelSb);
                return;
            default:
                System.out.println("以下の " + containsOrderedNum(cancelNo) + " 項目が見つかりました.\n");
                printOrdered(cancelNo);
                String cancelSize;
                while(true) {
                    System.out.print("\nこの中から注文を取り消す商品のサイズを入力してください(0で中断):");
                    cancelSize = scan.nextLine().trim().toUpperCase();
                    if("0".equals(cancelSize) || "+0".equals(cancelSize) || "-0".equals(cancelSize)) {
                        System.out.println("注文受付に戻ります.\n");
                        return;
                    } else if(containsOrderedSize(cancelNo, cancelSize)) {
                        for(String[] strs: this.orderedItems) {
                            if(strs[1].equals(String.valueOf(cancelNo)) && strs[3].equals(cancelSize)) {
                                System.out.print(strs[0] + " の注文を取り消しますか？(y/n):");
                                switch(scan.nextLine()) {
                                    case "y":
                                        this.orderedItems.remove(strs);
                                        System.out.println(strs[0] + " の注文を取り消しました.\n");
                                        return;
                                    case "n":
                                        System.out.println("注文受付に戻ります.\n");
                                        return;
                                    default:
                                        System.out.println("y,n以外が入力されたので注文受付に戻ります.\n");
                                        return;
                                }
                            }
                        }
                    } else if(cancelSize.contains("�")) {
                        System.out.println("半角英数字で入力してください");
                    } else {
                        System.out.println("－－サイズ\"" + cancelSize + "\"はありません－－");
                    }
                }
        }
    }
    public void printInvoice() {
        StringBuilder invoice = new StringBuilder();
        long sum = 0L;
        long sumInTax = 0L;
        double discount = 1.0;
        if(this.haveTicket) {
            discount -= ticketDiscountRate;
        }
        invoice.append("―――――――――――――――――――――――――――――――\n").append("\t\t\t　《請求書》\n\n");
        Items lookIt;
        long priceInTax;
        long pricesInTax;
        for(String[] strs: this.orderedItems) {
            lookIt = lookUpItem(parseInt(strs[1]));
            sum += lookIt.getPrice(strs[3]) * parseLong(strs[4]);
            priceInTax = lookIt.getPriceInTax(strs[3]);
            pricesInTax = priceInTax * parseLong(strs[4]);
            sumInTax += pricesInTax;
            invoice.append("　").append(itemFormat(strs[0],30)).append(format("　　　 %-15s", "\\" + priceInTax)).append("\n");
            invoice.append("　").append(format("%29s", "×" + strs[4])).append(format("%28s", "\\" + pricesInTax)).append("\n");
        }
        invoice.append("‐‐‐‐‐‐‐‐‐‐‐‐‐‐‐‐‐‐‐‐‐‐‐‐‐‐‐‐‐‐‐\n");
        invoice.append(format("\t\t\t　小　計%23s\n", "\\" + sumInTax));
        invoice.append(format("\t\t\t　 (内消費税%19s)\n", "\\" + (sumInTax - sum)));
        if(haveTicket) {
            invoice.append(format("\t\t\t　チケット割%19s\n", "-\\" + round(sumInTax * ticketDiscountRate)));
        } else {
            invoice.append(format("\t\t\t　チケット割%19s\n", "-\\0"));
        }
        invoice.append(format("\n\t\t\t\t合　計%22s\n", "\\" + round(sumInTax * discount)));
        invoice.append("―――――――――――――――――――――――――――――――\n");
        System.out.println("・\n・\n・");
        System.out.println("こちら請求書になります.\n");
        System.out.println(invoice);
        invoiceSum = round(sumInTax * discount);
    }
    public void Accounting() {
        Scanner scan = new Scanner(System.in);
        System.out.println("・\n・\n・");
        System.out.println("～お会計～\n");
        while(true) {
            System.out.print("合計金額は \\" + invoiceSum + " になります.\nお支払い金額を入力してください:");
            try {
                long payAmount = parseLong(scan.nextLine());
                if(payAmount < invoiceSum) {
                    System.out.println("－－【入力エラー:お支払い金額が足りません.合計金額以上の整数を半角で入力してください】－－\n");
                    continue;
                }
                if(payAmount == invoiceSum) {
                    System.out.println(" \\" + payAmount + " ちょうど,頂きますね.");
                } else {
                    System.out.println(" \\" + payAmount + " 頂きますね.");
                    System.out.println(" \\" + (payAmount - invoiceSum) + " お返しいたします.");
                }
                System.out.println("こちらレシートになります.");
                printReceipt(payAmount);
                System.out.println("ご来店ありがとうございました.またのお越しをお待ちしております.");
                return;
            } catch(NumberFormatException e) {
                System.out.println("－－【入力エラー:使用不可能なデータが含まれているか、数値が大きすぎます。合計金額以上の整数を半角で入力してください】－－\n");
            }
        }
    }
    public void printReceipt(long payAmount) {
        StringBuilder receipt = new StringBuilder();
        long sum = 0L;
        long sumInTax = 0L;
        double discount = 1.0;
        if(this.haveTicket) {
            discount -= ticketDiscountRate;
        }
        receipt.append("\n―――――――――――――――――――――――――――――――—\n\t\t　　 ■■■ ITECafe ■■■\n\n");
        Items lookIt;
        long priceInTax;
        long pricesInTax;
        for(String[] strs: this.orderedItems) {
            lookIt = lookUpItem(parseInt(strs[1]));
            sum += lookIt.getPrice(strs[3]) * parseLong(strs[4]);
            priceInTax = lookIt.getPriceInTax(strs[3]);
            pricesInTax = priceInTax * parseLong(strs[4]);
            sumInTax += pricesInTax;
            receipt.append("　").append(itemFormat(strs[0],30)).append(format("　　　 %-15s", "\\" + priceInTax)).append("\n");
            receipt.append("　").append(format("%29s", "×" + strs[4])).append(format("%28s", "\\" + pricesInTax)).append("\n");
        }
        receipt.append("‐‐‐‐‐‐‐‐‐‐‐‐‐‐‐‐‐‐‐‐‐‐‐‐‐‐‐‐‐‐‐\n");
        receipt.append(format("\t\t\t　小　計%23s\n", "\\" + sumInTax));
        receipt.append(format("\t\t\t　 (内消費税%19s)\n", "\\" + (sumInTax - sum)));
        if(haveTicket) {
            receipt.append(format("\t\t\t　チケット割%19s\n", "-\\" + round(sumInTax * ticketDiscountRate)));
        } else {
            receipt.append(format("\t\t\t　チケット割%19s\n", "-\\0"));
        }
        receipt.append(format("\n\t\t\t\t合　計%22s\n", "\\" + round(sumInTax * discount)));
        receipt.append(format("\t\t\t\tお支払%22s\n", "\\" + payAmount));
        receipt.append(format("\t\t\t\tお釣り%22s\n", "\\" + (payAmount - round(sumInTax * discount))));
        receipt.append("―――――――――――――――――――――――――――――――\n");
        System.out.println(receipt);
    }
}
