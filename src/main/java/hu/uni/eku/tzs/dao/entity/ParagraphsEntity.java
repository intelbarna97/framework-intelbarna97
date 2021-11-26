package hu.uni.eku.tzs.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "paragraphs")
public class ParagraphsEntity {
    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "ParagraphNum")
    private int paragraphNum;

    @Column(name = "PlainText")
    private String plainText;

    @Column(name = "character_id")
    private int characterId;

    @Column(name = "chapter_id")
    private int chapterId;
}
