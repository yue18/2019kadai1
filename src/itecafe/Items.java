package itecafe;

import static java.lang.Integer.parseInt;
import static java.lang.Math.round;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

public class Items implements Comparable<Items>{
    public enum Categories {
        ドリンク("〔ドリンク〕"),
        スイーツ("〔スイーツ〕"),
        軽食("〔軽食〕"),
        パン("〔パン〕");
        
        private final String text;
        
        private Categories(final String text) {
            this.text = text;
        }
        @Override
        public String toString() {
            return this.text;
        }
    };
    static Set<Items> itemList = new TreeSet<>();
    private int no;
    private Categories category;
    private String name;
    private Map<String, Integer> sizePrice = new LinkedHashMap<>();
    
    @Override
    public boolean equals(Object o) {
        if(o == this) {
            return true;
        }
        if(o == null) {
            return false;
        }
        if(!(o instanceof Items)) {
            return false;
        }
        Items it = (Items) o;
        return this.name.trim().equals(it.name.trim());
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + Objects.hashCode(this.name);
        return hash;
    }
    
    @Override
    public int compareTo(Items obj) {
        if(this.getCategory().ordinal() < obj.getCategory().ordinal()) {
            return -1;
        }
        if(this.getCategory().ordinal() > obj.getCategory().ordinal()) {
            return 1;
        }
        if(this.no < obj.no) {
            return -1;
        }
        if(this.no > obj.no) {
            return 1;
        }
        return 0;
    }
    
    public static Items lookUpItem(int itemNo) {
    //商品番号に対応する商品インスタンスを返す。見つからなければnullを返す。
        for(Items it: itemList) {
            if(it.getNo() == itemNo) {
                return it;
            }
        }
        return null;
    }
    public static void printItemList() {
        String[] ctgr = new String[Categories.values().length];
        for(int i = 0; i < ctgr.length; i++) {
            ctgr[i] = Categories.values()[i].toString();
        }
        System.out.println("\n～ＭＥＮＵ～\n");
        Categories judge = Categories.values()[0];
        StringBuilder sb = new StringBuilder();
        sb.append(judge).append("\n");
        StringBuilder sb2 = new StringBuilder();
        for(Items item: itemList) {
            if(judge != item.category) {
                sb.append("\n").append(item.category).append("\n");
                judge = item.category;
            }
            sb2.append(item.no).append(".").append(item.name);
            sb.append(itemFormat(sb2, 28));
            sb2.delete(0, sb2.length());
            for(String size: item.sizePrice.keySet()) {
                if(size.equals("N")) {
                    sb.append(" (\\").append(item.getPrice("N"));
                    sb.append("[税込 \\").append(item.getPriceInTax("N")).append("]) ");
                } else {
                    sb.append(" (").append(size).append("：\\").append(item.getPrice(size));
                    sb.append("[税込 \\").append(item.getPriceInTax(size)).append("]) ");
                }
            }
            sb.deleteCharAt(sb.length() - 1);
            sb.append("\n");
        }
        System.out.println(sb);
    }
    public static String itemFormat(String target, int length){
        int byteDiff = (getByteLength(target, Charset.forName("UTF-8"))-target.length())/2;
        return String.format("%-"+(length-byteDiff)+"s", target);
    }
    public static String itemFormat(StringBuilder targets, int length){
        String target = new String(targets);
        int byteDiff = (getByteLength(target, Charset.forName("UTF-8"))-target.length())/2;
        return String.format("%-"+(length-byteDiff)+"s", target);
    }
    public static int getByteLength(String string, Charset charset) {
        return string.getBytes(charset).length;
    }
    public int getNo() {
        return this.no;
    }
    public void setNo(int inNo) {
        this.no = inNo;
    }
    public Categories getCategory() {
        return this.category;
    }
    public void setCategory(Categories inCategory) {
        this.category = inCategory;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String inName) {
        this.name = inName;
    }
    public StringBuilder getSystemName() {
        StringBuilder sysName = new StringBuilder();
        sysName.append(this.no).append(".").append(this.name);
        return sysName;
    }
    public Set<String> getSizes() {
        return sizePrice.keySet();
    }
    public String[] getSizeArray() {
        String[] sizeArray = new String[sizePrice.size()];
        int sizeNum = 0;
        for(String size: sizePrice.keySet()) {
            sizeArray[sizeNum] = size;
            sizeNum++;
        }
        return sizeArray;
    }
    public StringBuilder getFullName(String outSize) {
        StringBuilder fullName = new StringBuilder();
        fullName.append(this.getSystemName());
        if(!"N".equals(outSize)) {
            fullName.append(outSize);
        }
        return fullName;
    }
    public int getPrice(String outSize) {
        return this.sizePrice.get(outSize);
    }
    public int getPriceInTax(String outSize) {
        int priceInTax = (int) round(this.sizePrice.get(outSize) * 1.08);
        return priceInTax;
    }
    public Map<String, Integer> getSizePrice() {
        return this.sizePrice;
    }
    public void updateSizePrice(String inSizes, String inPrices) {
        ArrayList<String> sizeList = seekPartition(inSizes);
        ArrayList<String> priceList = seekPartition(inPrices);
        this.sizePrice.clear();
        for(int i = 0; i < sizeList.size() && i < priceList.size(); i++) {
            this.sizePrice.put(sizeList.get(i), parseInt(priceList.get(i)));
        }
    }
    private static ArrayList<String> seekPartition(String str) {
        str = str.trim();
        ArrayList<String> outValues = new ArrayList<>();
        int i = 0;
        while(i < str.length()) {
            if(str.substring(i).contains(",")) {
                outValues.add(str.substring(i, str.indexOf(",", i)).trim());
                i = str.indexOf(",", i) + 1;
            } else {
                outValues.add(str.substring(i).trim());
                i = str.length();
            }
        }
        return outValues;
    }
    
    public Items(int newNo, Categories newCategory, String newName, String newSizes, String newPrices) {
        //サイズが複数ある場合、サイズ、価格ともにサイズごとにコンマ(,)で区切って入力する。
        for(Items it: itemList) {
            if(it.getNo() == newNo) {
                newNo++;
            }
        }
        this.no = newNo;
        this.name = newName;
        this.category = newCategory;
        ArrayList<String> sizeList = seekPartition(newSizes);
        ArrayList<String> priceList = seekPartition(newPrices);
        for(int i = 0; i < sizeList.size() && i < priceList.size(); i++) {
            this.sizePrice.put(sizeList.get(i), parseInt(priceList.get(i)));
        }
        itemList.add(this);  //作成した商品を商品リストに追加
    }
    public Items(int newNo, Categories newCategory, String newName, String newPrice) {
        this(newNo, newCategory, newName, "N", newPrice);
    }
    public Items(int newNo, Categories newCategory, String newName, int newPrice) {
        this(newNo, newCategory, newName, "N", String.valueOf(newPrice));
    }
    public Items(Categories newCategory, String newName, String newSizes, String newPrices) {
        this(itemList.size() + 1, newCategory, newName, newSizes, newPrices);
    }
    public Items(Categories newCategory, String newName, String newPrice) {
        this(newCategory, newName, "N", newPrice);
    }
    public Items(Categories newCategory, String newName, int newPrice) {
        this(newCategory, newName, "N", String.valueOf(newPrice));
    }
}
