package hu.uni.eku.tzs.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Data;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Character {
    private int id;

    private String charName;

    private String abbrev;

    private String description;
}
