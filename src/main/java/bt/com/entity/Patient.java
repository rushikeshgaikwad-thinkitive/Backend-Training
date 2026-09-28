package bt.com.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity 
public class Patient {
   @Id
   @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
   private String name;
   private String gender;
   private  int age;
   private boolean active;
   private String phone;
   private String email;
   
   public Patient() {
this.name = name;
this.age = age;
this.gender = gender;
this.phone = phone;
this.email = email;
this.active = active;
}


   public String getPhone() {
	return phone;
}


   public void setPhone(String phone) {
	this.phone = phone;
   }


   public String getEmail() {
	return email;
   }


   public void setEmail(String email) {
	this.email = email;
   }


   public Long getId() {
	return id;
   }


   public void setId(Long id) {
	this.id = id;
   }


   public String getName() {
	return name;
   }


   public void setName(String name) {
	this.name = name;
   }


   public String getGender() {
	return gender;
   }


   public void setGender(String gender) {
	this.gender = gender;
   }


   public int getAge() {
	return age;
   }


   public void setAge(int age) {
	this.age = age;
   }


   public boolean isActive() {
	return active;
   }


   public void setActive(boolean active) {
	this.active = active;
   }
   
   
   
   
   
}
