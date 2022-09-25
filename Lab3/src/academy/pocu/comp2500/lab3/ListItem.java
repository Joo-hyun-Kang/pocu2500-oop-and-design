package academy.pocu.comp2500.lab3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListItem {
    private String text;
    private ArrayList<ListItem> sublistItems;
    private char bulletStyle;

    public ListItem(String text) {
        this(text, '*');
    }

    public ListItem(String text, char bulletStyle) {
        this.text = text;
        this.sublistItems = new ArrayList<>();
        this.bulletStyle = bulletStyle;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public char getBulletStyle() {
        return bulletStyle;
    }

    public void setBulletStyle(char bulletStyle) {
        this.bulletStyle = bulletStyle;
    }

    public void addSublistItem(ListItem sublistItem) {
        this.sublistItems.add(sublistItem);
    }

    public void removeSublistItem(int index) {
        this.sublistItems.remove(index);
    }

    public ListItem getSublistItem(int index) {
        return this.sublistItems.get(index);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%c %s%s", this.bulletStyle, this.text, System.lineSeparator()));

        strinifySublistRecursive(this.sublistItems, 1, sb);

        return sb.toString();
    }

    private void strinifySublistRecursive(ArrayList<ListItem> subListItems, int depth, StringBuilder sb) {
        if (subListItems.size() == 0) {
            return;
        }

        char[] indent = new char[depth * 4];

        Arrays.fill(indent, ' ');

        String indentToString = new String(indent);

        for (ListItem sublistItem : subListItems) {
            sb.append(String.format("%s%c %s%s", indentToString, sublistItem.getBulletStyle(), sublistItem.getText(), System.lineSeparator()));
        }

        for (ListItem sublistItem : subListItems) {
            strinifySublistRecursive(sublistItem.sublistItems, depth + 1, sb);
        }
    }
}