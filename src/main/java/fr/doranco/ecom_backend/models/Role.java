package fr.doranco.ecom_backend.models;

import fr.doranco.ecom_backend.enums.RoleName;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Enumerated(EnumType.STRING)
    private fr.doranco.ecom_backend.enums.RoleName roleName;

    public Role(RoleName roleName){
        this.roleName=roleName;
    }

}
