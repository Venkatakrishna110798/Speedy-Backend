package com.msmavas.speedy;



import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class SpeedyApplication {

	private static boolean accountCreationInProgress = false;

    public static synchronized boolean isAccountCreationInProgress() {
        return accountCreationInProgress;
    }

    public static synchronized void setAccountCreationInProgress(boolean value) {
        accountCreationInProgress = value;
    }
 
    public static void main(String[] args) {
		SpringApplication.run(SpeedyApplication.class, args);
	}

}
