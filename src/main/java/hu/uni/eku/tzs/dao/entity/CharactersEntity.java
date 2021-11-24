package hu.uni.eku.tzs.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "characters")
public class CharactersEntity {
    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "CharName")
    private String CharName;

    @Column(name = "Abbrev")
    private String Abbrev;

    @Column(name = "Description")
    private String Description;
}
