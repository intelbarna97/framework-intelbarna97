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
public class Paragraph {
    private int id;

    private int paragraphNum;

    private String plainText;

    private int characterId;

    private int chapterId;
}
