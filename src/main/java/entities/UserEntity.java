package entities;


import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class UserEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="id")
    private int id ;

    @Override
    public String toString() {
        return "UserEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", mail='" + mail + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public UserEntity(int id, String name, String mail, String password) {
        this.id = id;
        this.name = name;
        this.mail = mail;
        this.password = password;
    }

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String mail;

    @Column(name = "password")
    private String password;

}
