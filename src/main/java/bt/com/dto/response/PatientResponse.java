package bt.com.dto.response;

public class PatientResponse {

	    private Long id;
	    private String name;
	    private int age;
	    private String gender;
	    private String phone;
	    private String email;
	    private boolean active;

	    public PatientResponse(Long id, String name, int age,
	                            String gender, String phone,
	                            String email, boolean active) {
	        this.id = id;
	        this.name = name;
	        this.age = age;
	        this.gender = gender;
	        this.phone = phone;
	        this.email = email;
	        this.active = active;
	    }

	    public Long getId() {
	        return id;
	    }

	    public String getName() {
	        return name;
	    }

	    public int getAge() {
	        return age;
	    }

	    public String getGender() {
	        return gender;
	    }

	    public String getPhone() {
	        return phone;
	    }

	    public String getEmail() {
	        return email;
	    }

	    public boolean isActive() {
	        return active;
	    }
	}