package com.blog.blogApp;

import com.blog.blogApp.config.AppConstants;
import com.blog.blogApp.dao.RoleRepo;
import com.blog.blogApp.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class BlogAppApplication implements CommandLineRunner {

	@Autowired
	RoleRepo roleRepo;

	public static void main(String[] args) {
		SpringApplication.run(BlogAppApplication.class, args);

	}

	@Override
	public void run(String... args) throws Exception {
		try {
			Role role= new Role();
			role.setName("USER");
			role.setRoleId(AppConstants.NORMAL_USER);

			Role role2 = new Role();
			role2.setName("ADMIN");
			role2.setRoleId(AppConstants.ADMIN_USER);

			List<Role> roles = List.of(role,role2);
		List<Role> result =	this.roleRepo.saveAll(roles);
           result.forEach(r -> System.out.println(r));

		}
		catch (Exception e){
			System.out.println(e);
		}
	}
}
