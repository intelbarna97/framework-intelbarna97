package hu.uni.eku.tzs.model;

import lombok.*;


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
