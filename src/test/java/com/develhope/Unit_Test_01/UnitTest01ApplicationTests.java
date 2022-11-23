package com.develhope.Unit_Test_01;

import com.develhope.Unit_Test_01.controller.PlayerController;
import com.develhope.Unit_Test_01.entity.Player;
import com.develhope.Unit_Test_01.repository.PlayerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles(value = "test")
@AutoConfigureMockMvc
class UnitTest01ApplicationTests {

    @Autowired
    PlayerController playerController;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PlayerRepository playerRepository;

    @Test
    void contextLoads() {
        assertThat(playerController).isNotNull();
    }

    private Player getPlayerFromId(Long id) throws Exception {

        MvcResult result = this.mockMvc.perform(get("/player" + id))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        try {
            String playerJSON = result.getResponse().getContentAsString();

            return objectMapper.readValue(playerJSON, Player.class);
        } catch (Exception e) {
            return null;
        }
    }

    private Player createAPlayer() throws Exception {
        Player player = new Player();
        player.setId(1);
        player.setName("Mirko");
        player.setSurname("Di Cristina");
        player.setAge(24);

        return createAPlayer(player);
    }

    private Player createAPlayer(Player player) throws Exception {

        MvcResult result = createAPlayerRequest(player);
        return objectMapper.readValue(result.getResponse().getContentAsString(), Player.class);

    }

    private MvcResult createAPlayerRequest(Player player) throws Exception {
        if (player == null) return null;
        String playerJSON = objectMapper.writeValueAsString(player);

        return this.mockMvc.perform(post("/player")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(playerJSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void createAPlayerTest() throws Exception {
        Player player = createAPlayer();
        assertThat(player.getId()).isNotNull();
    }

    @Test
    void readPlayerList() throws Exception {
        createAPlayerTest();

        MvcResult result = this.mockMvc.perform(get("/player/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        List<Player> playerResponse = objectMapper.readValue(result.getResponse().getContentAsString(), List.class);
        assertThat(playerResponse.size()).isNotZero();
    }

    @Test
    void readSinglePlayer() throws Exception {
        Player player = createAPlayer();
        assertThat(player.getId()).isNotNull();

        Optional<Player> playerFormResponse = playerRepository.findById(player.getId());
        assertThat(playerFormResponse.isEmpty());

    }

    @Test
    void updatePlayer() throws Exception {
        Player player = createAPlayer();

        int newAge = 25;
        player.setAge(newAge);

        String playerJSON = objectMapper.writeValueAsString(player);
        mockMvc.perform(put("/player/" + player.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(playerJSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        Optional<Player> playerResponse = playerRepository.findById(player.getId());

        assertThat(playerResponse.isPresent());
        assertThat(playerResponse.get().equals(player));

    }

    @Test
    void deleteUser() throws Exception {
        Player player = createAPlayer();
        assertThat(player.getId()).isNotNull();

        mockMvc.perform(delete("/player/" + player.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        Optional<Player> playerResponse = playerRepository.findById(player.getId());

        assertThat(playerResponse.isEmpty());
    }


}
