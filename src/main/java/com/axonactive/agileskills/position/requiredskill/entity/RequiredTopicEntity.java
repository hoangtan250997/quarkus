package com.axonactive.agileskills.position.requiredskill.entity;

import com.axonactive.agileskills.skill.topic.entity.TopicEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "required_topic")
public class RequiredTopicEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "topic_id", nullable = false)
    private TopicEntity topic;

    @ManyToOne
    @JoinColumn(name = "required_skill_id", nullable = false)
    private RequiredSkillEntity requiredSkill;

    @Enumerated(EnumType.STRING)
    private RequireEnum require;

    @Enumerated(EnumType.STRING)
    private LevelEnum level;

    @Column(name = "required_topic_note", length = 2000)
    private String note;
}
