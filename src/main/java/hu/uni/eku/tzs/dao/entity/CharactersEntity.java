package hu.uni.eku.tzs.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;

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
    private String charName;

    @Column(name = "Abbrev")
    private String abbrev;

    @Column(name = "Description")
    private String description;
}
