package com.bachir9.app.role;

import com.bachir9.app.common.BaseEntity;
import com.bachir9.app.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name = "ROLES")
public class Role extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "NAME")
    private String name;

    @ManyToMany(mappedBy = "roles")
    private List<User> users;

}
