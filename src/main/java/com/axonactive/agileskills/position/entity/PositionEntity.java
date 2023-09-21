package com.axonactive.agileskills.position.entity;

import com.axonactive.agileskills.department.team.entity.TeamEntity;
import com.axonactive.agileskills.position.requiredskill.entity.RequiredSkillEntity;
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

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "position")
public class PositionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "position", cascade = CascadeType.ALL)
    private List<RequiredSkillEntity> requiredSkillList;

    @Column(name = "position_name", nullable = false)
    private String name;

    @Column(length = 2000)
    private String note;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private PositionStatusEnum status;

    @Column(nullable = false)
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "team_id", nullable = false)
    private TeamEntity team;

    private LocalDateTime openedDate;
    private LocalDateTime createdDate;
    private LocalDateTime closedDate;
}
