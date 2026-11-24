package ca.humber.stockbridge;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@SpringBootTest(properties="spring.datasource.url=jdbc:h2:mem:securitytest;DB_CLOSE_DELAY=-1")
@AutoConfigureMockMvc class SecurityTests {
 @Autowired MockMvc mvc;
 @Test void publicHome() throws Exception {mvc.perform(get("/")).andExpect(status().isOk());}
 @Test void anonymousAdmin() throws Exception {mvc.perform(get("/admin")).andExpect(status().is3xxRedirection());}
 @Test @WithMockUser(roles="STAFF") void staffCannotEdit() throws Exception {mvc.perform(get("/admin/materials/1/edit")).andExpect(status().isForbidden());}
 @Test @WithMockUser(roles="MANAGER") void managerCannotAdmin() throws Exception {mvc.perform(get("/admin")).andExpect(status().isForbidden());}
 @Test @WithMockUser(roles="ADMIN") void csrfRequired() throws Exception {mvc.perform(post("/admin/materials/1/delete")).andExpect(status().isForbidden());}
}
