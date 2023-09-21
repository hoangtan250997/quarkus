package com.axonactive.agileskills.position.requiredskill.entity;

import com.axonactive.agileskills.position.entity.PositionEntity;
import com.axonactive.agileskills.skill.entity.SkillEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "required_skill")
public class RequiredSkillEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "skill_id", nullable = false)
    private SkillEntity skill;

    @ManyToOne
    @JoinColumn(name = "position_id", nullable = false)
    private PositionEntity position;

    @OneToMany(mappedBy = "requiredSkill", cascade = CascadeType.ALL)
    private List<RequiredTopicEntity> requiredTopicList;

    @Enumerated(EnumType.STRING)
    private RequireEnum require;

    @Enumerated(EnumType.STRING)
    private LevelEnum level;

    @Column(name = "required_skill_note", length = 2000)
    private String note;
}
