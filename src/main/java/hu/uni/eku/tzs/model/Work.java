package hu.uni.eku.tzs.model;

import lombok.*;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Work {
    private int id;

    private String title;

    private String longTitle;

    private int date;

    private String genreType;
}
