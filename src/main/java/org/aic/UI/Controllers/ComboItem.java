package org.aic.UI.Controllers;

public class ComboItem {
    private String id;
    private String displayText;

    public ComboItem(String id, String displayText) {
        this.id = id;
        this.displayText = displayText;
    }

    public String getId() { return id; }

    @Override
    public String toString() { return displayText; }
}