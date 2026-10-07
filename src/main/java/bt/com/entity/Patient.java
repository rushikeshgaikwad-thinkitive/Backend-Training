package bt.com.entity;


import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity 
@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Patient extends Auditable {
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
 private List<Appointment> appointments;
}
