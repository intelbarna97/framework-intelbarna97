package hu.uni.eku.tzs.model;

import lombok.*;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Paragraph {
    private int id;

    private int paragraphNum;

    private String plainText;

    private int characterId;

    private int chapterId;
}
