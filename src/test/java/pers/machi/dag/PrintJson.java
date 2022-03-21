package pers.machi.dag;

public class PrintJson {
    public static void main(String[] args) {


        String json1 = "[{\"id\":1}" +
                ",{\"id\":3}" +
                ",{\"id\":2}" +
                ",{\"id\":4}]";
        String json2 = "[{\"src\":{\"id\":1},\"tgt\":{\"id\":3}}" +
                ",{\"src\":{\"id\":1},\"tgt\":{\"id\":2}}" +
                ",{\"src\":{\"id\":2},\"tgt\":{\"id\":3}}]";

        System.out.println(json1);
        System.out.println(json2);

    }
}
