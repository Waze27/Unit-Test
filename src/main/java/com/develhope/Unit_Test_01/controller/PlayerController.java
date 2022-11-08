package com.develhope.Unit_Test_01.controller;

import com.develhope.Unit_Test_01.entity.Player;
import com.develhope.Unit_Test_01.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/player")
public class PlayerController {

    @Autowired
    PlayerRepository playerRepository;

    @PostMapping("")
    public Player createUser(@RequestBody Player player){
        return playerRepository.save(player);
    }

    @GetMapping("")
    public List<Player> readAll(){
        return playerRepository.findAll();
    }

    @GetMapping("/{id}")
    public Player readOne(@PathVariable long id){
        Optional<Player> user = playerRepository.findById(id);
        if(user.isPresent()){
            return user.get();
        } else {
            return null;
        }
    }

    @PutMapping("/{id}")
    public Player editUser(@PathVariable long id, @RequestBody Player player){
        player.setId(id);
        return playerRepository.save(player);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable long id){
        playerRepository.deleteById(id);
    }

}
