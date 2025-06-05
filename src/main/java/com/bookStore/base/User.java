
package com.bookStore.base;

public class User {
	    private int id;
	    private String email;
	    private String password;

	    
	    public User() {
	        this.id = generateUniqueId();
	    }

	   
	    public User(int id, String email, String password) {
	        this.id = id;
	        this.email = email;
	        this.password = password;
	    }

	   
	    public User(String email, String password) {
	        this.id = generateUniqueId();
	        this.email = email;
	        this.password = password;
	    }

	 
	    public int getId() {
	        return id;
	    }

	    public void setId(int id) {
	        this.id = id;
	    }

	    public String getEmail() {
	        return email;
	    }

	    public void setEmail(String email) {
	        this.email = email;
	    }

	    public String getPassword() {
	        return password;
	    }

	    public void setPassword(String password) {
	        this.password = password;
	    }

	   
	    private int generateUniqueId() {
	        return (int) (System.currentTimeMillis() % 100000);
	    }

	    
	    @Override
	    public String toString() {
	        return "User{" +
	                "id=" + id +
	                ", email='" + email + '\'' +
	                ", password='******'}";
	    }
	}


