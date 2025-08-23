package com.github.mahdim1000.exception_handing.dto;

import jakarta.validation.constraints.*;

/**
 * DTO for user creation/update requests.
 * Demonstrates validation annotations that will trigger validation exceptions.
 * 
 * Uses class instead of record to properly support Bean Validation annotations.
 */
public class UserRequest {
    
    @NotBlank(message = "error.validation.required")
    private String name;
    
    @NotBlank(message = "error.validation.required")
    @Email(message = "error.validation.email.invalid")
    private String email;
    
    @NotNull(message = "error.validation.required")
    @Min(value = 18, message = "error.validation.age.invalid")
    @Max(value = 100, message = "error.validation.age.invalid")
    private Integer age;
    
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "error.validation.phone.invalid")
    private String phone;

    // Constructors
    public UserRequest() {}

    public UserRequest(String name, String email, Integer age, String phone) {
        this.name = name;
        this.email = email;
        this.age = age;
        this.phone = phone;
    }

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
}
