package group.spring;

import static org.mockito.ArgumentMatchers.any;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.client.RestTemplate;

import group.spring.services.login.controller.LoginController;
import group.spring.services.login.model.Roles;
import group.spring.services.login.model.Users;
import group.spring.services.person.controller.PersonController;
import group.spring.services.person.model.Location;
import group.spring.services.person.model.Person;
import group.spring.services.person.model.Telephone;

@AutoConfigureMockMvc
@AutoConfigureRestDocs(outputDir = "target/snippets")
@SpringBootTest
class ApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private LoginController loginController;

	@MockBean
	private PersonController personController;

	public Users getUser(){
		List<Roles> roles = new ArrayList<Roles>();
		roles.add(new Roles(1l, "ROLE_USER"));

		Users user = new Users(1l, "user", "user", roles);

		return user;
	}

	public List<Person> getListPeople(){
		List<Person> persons = new ArrayList<Person>();
		
		List<Telephone> telephones1 = new ArrayList<Telephone>();
		List<Telephone> telephones2 = new ArrayList<Telephone>();
		
		Person person1 = new Person(1l, "Gabriel", "Silva", 17, "gabrielsilva@gmail.com", "01001000", null, null, 'M', "46.759.097-7", "564.185.080-25");
		Person person2 = new Person(2l, "Carol", "Dias", 20, "caroldias@gmail.com", "76812-374", null, null, 'W', "29.999.094-1", "072.118.070-16");
		
		Location loc1 = new RestTemplate().getForEntity("https://viacep.com.br/ws/" + person1.getCep() + "/json/", Location.class).getBody();
		Location loc2 = new RestTemplate().getForEntity("https://viacep.com.br/ws/" + person2.getCep() + "/json/", Location.class).getBody();

		person1.setLocation(loc1);
		person2.setLocation(loc2);

		Telephone telephone1 = new Telephone(1l, "Cellphone", "00 0000-0000", person1);
		Telephone telephone2 = new Telephone(2l, "Telephone", "11 1111-1111", person2);

		telephones1.add(telephone1);
		telephones2.add(telephone2);

		person1.setTelephone(telephones1);
		person2.setTelephone(telephones2);
		
		persons.add(person1);
		persons.add(person2);

		for (Person person : persons) {
			Link link = WebMvcLinkBuilder.linkTo(PersonController.class).slash(person.getId()).withSelfRel();
            person.add(link);
		}

		return persons;
	} 

	@Test
	public void testPostUser() throws Exception{
		Users user = getUser();
		Mockito.when(loginController.saveUser(any())).thenReturn(ResponseEntity.ok(user));
		mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/login-service/user").content(new ObjectMapper().writeValueAsString(user)).contentType(MediaType.APPLICATION_JSON))
			   .andExpect(MockMvcResultMatchers.status().isOk())
			   .andDo(MockMvcRestDocumentation.document("post-user",
			   		Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
					Preprocessors.preprocessResponse(Preprocessors.prettyPrint())));		
	}
	
	@Test
	public void testLogin() throws Exception{
		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/login-service").param("username", "user").param("password", "user"))
			   .andExpect(MockMvcResultMatchers.status().isOk())
			   .andDo(MockMvcRestDocumentation.document("get-token",
			   		Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
					Preprocessors.preprocessResponse(Preprocessors.prettyPrint())));		
	} 

	@Test
	public void testGetPeople() throws Exception{
		List<Person> people = getListPeople();
		Mockito.when(personController.getPeople()).thenReturn(ResponseEntity.ok(people));
		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/person-service/").header(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImF1ZCI6ImZyb250ZW5kIiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDgwL2FwaS92MS9sb2dpbi1zZXJ2aWNlIiwiZXhwIjoxNjUzOTI2OTc1fQ.akZ-P6GWfCSVxT99EkLYsHa7WOokzLaX3_fgV1A_qsQ"))
			   .andExpect(MockMvcResultMatchers.status().isOk())
			   .andDo(MockMvcRestDocumentation.document("get-people",
			   		Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
					Preprocessors.preprocessResponse(Preprocessors.prettyPrint())));		
	}

	@Test
	public void testGetPerson() throws Exception{
		Person person = getListPeople().get(1);
		Mockito.when(personController.getPerson(any())).thenReturn(ResponseEntity.ok(person));
		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/person-service/" +  person.getId()).header(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImF1ZCI6ImZyb250ZW5kIiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDgwL2FwaS92MS9sb2dpbi1zZXJ2aWNlIiwiZXhwIjoxNjUzOTI2OTc1fQ.akZ-P6GWfCSVxT99EkLYsHa7WOokzLaX3_fgV1A_qsQ"))
			   .andExpect(MockMvcResultMatchers.status().isOk())
			   .andDo(MockMvcRestDocumentation.document("get-person",
			   		Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
					Preprocessors.preprocessResponse(Preprocessors.prettyPrint())));		
	}

	@Test
	public void testPostPerson() throws Exception{
		Person person = getListPeople().get(1);
		Mockito.when(personController.savePerson(any())).thenReturn(ResponseEntity.ok(person));
		mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/person-service/").content(new ObjectMapper().writeValueAsString(person)).contentType(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImF1ZCI6ImZyb250ZW5kIiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDgwL2FwaS92MS9sb2dpbi1zZXJ2aWNlIiwiZXhwIjoxNjUzOTI2OTc1fQ.akZ-P6GWfCSVxT99EkLYsHa7WOokzLaX3_fgV1A_qsQ"))
			   .andExpect(MockMvcResultMatchers.status().isOk())
			   .andDo(MockMvcRestDocumentation.document("post-person",
			   		Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
					Preprocessors.preprocessResponse(Preprocessors.prettyPrint())));		
	}

	@Test
	public void testDeletePerson() throws Exception{
		Person person = getListPeople().get(1);
		Mockito.when(personController.removePerson(any())).thenReturn(ResponseEntity.ok().build());
		mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/person-service/" + person.getId()).header(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImF1ZCI6ImZyb250ZW5kIiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDgwL2FwaS92MS9sb2dpbi1zZXJ2aWNlIiwiZXhwIjoxNjUzOTI2OTc1fQ.akZ-P6GWfCSVxT99EkLYsHa7WOokzLaX3_fgV1A_qsQ"))
			   .andExpect(MockMvcResultMatchers.status().isOk())
			   .andDo(MockMvcRestDocumentation.document("delete-person",
			   		Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
					Preprocessors.preprocessResponse(Preprocessors.prettyPrint())));		
	}



}
