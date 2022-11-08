package com.develhope.Unit_Test_01;

import com.develhope.Unit_Test_01.controller.PlayerController;
import com.develhope.Unit_Test_01.entity.Player;
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

    @Test
    void contextLoads() {
        assertThat(playerController).isNotNull();
    }

    private Player getPlayerFromId(Long id) throws Exception {
        MvcResult result = this.mockMvc.perform(get("/player/" + id))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        try {
            String playerJSON = result.getResponse().getContentAsString();
            Player player = objectMapper.readValue(playerJSON, Player.class);

            assertThat(player).isNotNull();
            assertThat(player.getId()).isNotNull();

            return player;
        } catch (Exception e) {
            return null;
        }
    }

    private Player createAPlayer() throws Exception {
        Player player = new Player();
        player.setName("Mirko");
        player.setSurname("Di Cristina");
        player.setAge(24);

        return createAPlayer(player);
    }

    private Player createAPlayer(Player player) throws Exception {
        MvcResult result = createAPlayerRequest(player);
        Player userFromResponse = objectMapper.readValue(result.getResponse().getContentAsString(), Player.class);

        assertThat(userFromResponse).isNotNull();
        assertThat(userFromResponse.getId()).isNotNull();

        return userFromResponse;
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
        createAPlayer();
    }

    @Test
    void readPlayerList() throws Exception {
        createAPlayerTest();

        MvcResult result = this.mockMvc.perform(get("/player/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        List<Player> playerResponse = objectMapper.readValue(result.getResponse().getContentAsString(), List.class);
        assertThat(playerResponse.size()).isNotNull();
    }

    @Test
    void readSinglePlayer() throws Exception {
        createAPlayer();
    }

    @Test
    void updatePlayer() throws Exception {
        Player player = createAPlayer();

        int newAge = 25;
        player.setAge(newAge);

        String playerJSON = objectMapper.writeValueAsString(player);

        MvcResult result = this.mockMvc.perform(put("/player/" + player.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(playerJSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        Player playerResponse = objectMapper.readValue(result.getResponse().getContentAsString(), Player.class);

        assertThat(playerResponse.getId()).isEqualTo(player.getId());
        assertThat(playerResponse.getAge()).isEqualTo(newAge);

    }

    @Test
    void deleteUser() throws Exception {
        Player player = createAPlayer();
        assertThat(player.getId()).isNotNull();

        this.mockMvc.perform(delete("/player/" + player.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        Player playerResponse = getPlayerFromId(player.getId());
        assertThat(playerResponse).isNull();
    }


}
