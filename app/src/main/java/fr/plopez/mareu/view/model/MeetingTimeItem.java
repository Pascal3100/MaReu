package fr.plopez.mareu.view.model;

public class MeetingTimeItem {

    private final String time;
    private Boolean isChecked = false;

    public MeetingTimeItem(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public String getHour() {
        return time.split(":")[0];
    }

    public String getMinutes() {
        return time.split(":")[1];
    }

    public Boolean isChecked() {
        return isChecked;
    }

    public void setChecked(Boolean checked) {
        isChecked = checked;
    }
}
