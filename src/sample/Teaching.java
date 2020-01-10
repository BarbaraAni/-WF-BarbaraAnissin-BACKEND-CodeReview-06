package sample;

public class Teaching {
    int fk_teacher_id;
    int fk_class_id;

    public Teaching(int fk_teacher_id, int fk_class_id) {
        this.fk_teacher_id = fk_teacher_id;
        this.fk_class_id = fk_class_id;
    }

    public int getFk_teacher_id() {
        return fk_teacher_id;
    }

    public void setFk_teacher_id(int fk_teacher_id) {
        this.fk_teacher_id = fk_teacher_id;
    }

    public int getFk_class_id() {
        return fk_class_id;
    }

    public void setFk_class_id(int fk_class_id) {
        this.fk_class_id = fk_class_id;
    }
}
