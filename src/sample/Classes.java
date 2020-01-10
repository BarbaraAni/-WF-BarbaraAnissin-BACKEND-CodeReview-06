package sample;

public class Classes {
    int class_id;
    String class_name;

    public Classes(int classId, String classname) {
        this.class_id = class_id;
        this.class_name = class_name;
    }

    public int getClassId() {
        return class_id;
    }

    public void setClassId(int class_id) {
        this.class_id = class_id;
    }

    public String getClassname() {
        return class_name;
    }

    public void setClassname(String class_name) {
        this.class_name = class_name;
    }
}
