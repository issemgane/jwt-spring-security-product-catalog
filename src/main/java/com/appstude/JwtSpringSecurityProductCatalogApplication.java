package com.appstude;

import com.appstude.entities.AppRole;
import com.appstude.service.AccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.stream.Stream;

@SpringBootApplication
public class JwtSpringSecurityProductCatalogApplication {

	public static void main(String[] args) {
		SpringApplication.run(JwtSpringSecurityProductCatalogApplication.class, args);
	}

	@Bean
	public BCryptPasswordEncoder getBCPE  (){
		return   new BCryptPasswordEncoder();
	}

	@Bean
	CommandLineRunner start(AccountService accountService){
		return args ->{
			/*accountService.addRoleToUser("user1","USER");
			accountService.addRoleToUser("user2","USER");
*/
			//categoryRepository.deleteAll();
			/*Stream.of("USER","ADMIN","MANAGER").forEach(role->{
				accountService.addRole(new AppRole(null,role));
			});

			Stream.of("user1","user2","admin","manager").forEach(user->{
				accountService.saveUser(user,"1234","1234");
			});*/
           /*


			productRepository.findAll().forEach(System.out::println);*/
		};
	}
}

