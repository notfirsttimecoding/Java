package de.hs_kl.staab.playground;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class RestSandboxTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void contextLoads() {
	}

	@Test
	public void whenGetName_thenOk() throws Exception {
		//@formatter:off
		this.mockMvc
			.perform(get("/sandbox/test/").param("name", "Frank"))
			.andExpect(status().isOk());
		//@formatter:on
	}

	@Test
	public void whenPing_thenCorrectString() {
		String body = this.restTemplate.getForObject("/sandbox/ping", String.class);
		assertThat(body).isEqualTo("Heute ist ein wirklich toller Tag!");
	}

}
