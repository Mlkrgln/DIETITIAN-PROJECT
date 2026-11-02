package com.diyetisyen.user.entity;

import com.diyetisyen.common.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class Role extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String name; // e.g. ROLE_CUSTOMER, ROLE_DIETITIAN, ROLE_ADMIN
}
