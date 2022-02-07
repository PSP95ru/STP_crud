package net.mma.mmapromotion.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "fighter")
public class Fighter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(targetEntity = CardPostion.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "Card_id")
    private CardPostion cardpostion;
    @Column(name = "first_name")
    private String firstname;
    @Column(name = "second_name")
    private String secondname;
    @Column(name = "Nationality")
    private String Nationality;
    @Column(name = "Age")
    private int age;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Fighter )) return false;
        return id != null && id.equals(((Fighter) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return id.toString() + cardpostion.getId().toString() +
                firstname + secondname + Nationality + age;
    }
}