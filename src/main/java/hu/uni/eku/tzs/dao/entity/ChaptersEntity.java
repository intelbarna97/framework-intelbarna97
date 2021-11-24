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
public class ChaptersEntity {
    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "Act")
    private int Act;

    @Column(name = "Scene")
    private int Scene;

    @Column(name = "Description")
    private String Description;

    @Column(name = "work_id")
    private int workId;
}
