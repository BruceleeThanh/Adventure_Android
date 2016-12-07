package studio.crazybt.adventure.models;

/**
 * Created by Brucelee Thanh on 02/12/2016.
 */

public class SpinnerItem {
    String label;
    Integer iconId;

    public SpinnerItem() {
    }

    public SpinnerItem(String label, Integer iconId) {
        this.label = label;
        this.iconId = iconId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Integer getIconId() {
        return iconId;
    }

    public void setIconId(Integer iconId) {
        this.iconId = iconId;
    }
}
