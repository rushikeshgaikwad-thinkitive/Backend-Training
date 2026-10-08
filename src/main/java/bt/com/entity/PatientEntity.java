package bt.com.entity;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import bt.com.dto.constants.Literals;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity 
@Table(name = Literals.PATIENTS)
@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PatientEntity extends Auditable {
   @Id
   @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
   private String name;
   private String gender;
   private  int age;
   private boolean active;
   private String phone;
   private String email;
 @Column(name = "date_of_birth")
   private LocalDate dateOfBirth;
  
 @OneToMany(mappedBy = "patient")
 private List<AppointmentEntity> appointments = new ArrayList<>();
}
