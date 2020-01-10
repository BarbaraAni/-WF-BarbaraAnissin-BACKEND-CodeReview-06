package sample;

public class Teachers {
    int teacher_id;
    String name;
    String surname;
    String email;

    public int getTeacherId() {
        return teacher_id;
    }

    public void setTeacherId(int teacher_id) {
        this.teacher_id = teacher_id;
    }

    public Teachers(int teacher_id, String name, String surname, String email) {
        this.teacher_id = teacher_id;
        this.name = name;
        this.surname = surname;
        this.email = email;
    }

    public int getId() {
        return teacher_id;
    }

    public void setId(int teacher_id) {
        this.teacher_id = teacher_id;
    }

    public String getFirstname() {
        return name;
    }

    public void setFirstname(String name) {
        this.name = name;
    }

    public String getLastname() {
        return surname;
    }

    public void setLastname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}