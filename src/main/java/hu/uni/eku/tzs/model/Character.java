package hu.uni.eku.tzs.model;

import lombok.*;


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
