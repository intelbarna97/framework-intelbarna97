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
@Table(name = "chapters")
public class WorksEntity {
    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "Title")
    private String Title;

    @Column(name = "LongTitle")
    private String LongTitle;

    @Column(name = "Date")
    private int Date;

    @Column(name = "GenreType")
    private String GenreType;
}
