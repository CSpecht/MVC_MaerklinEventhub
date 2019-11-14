package specht.DatabaseObjects;

public class Ressource_Configuration_Model {

    private int id;
    private int resId;
    private String resName;
    private String resGroup;

    public Ressource_Configuration_Model () {}

    public Ressource_Configuration_Model (int rId, String rName, String rGroup) {
        this.resId = rId;
        this.resName = rName;
        this.resGroup = rGroup;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public String getResName() {
        return resName;
    }

    public void setResName(String resName) {
        this.resName = resName;
    }

    public String getResGroup() {
        return resGroup;
    }

    public void setResGroup(String resGroup) {
        this.resGroup = resGroup;
    }



}
