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
public class Chapter {

    private int id;

    private int act;

    private int scene;

    private String description;

    private int workId;
}
