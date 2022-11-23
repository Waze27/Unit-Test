package com.develhope.Unit_Test_01.controller;

import com.develhope.Unit_Test_01.entity.Player;
import com.develhope.Unit_Test_01.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public List<Player> readAll()                                                                               {
        return playerRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Player> readOne(@PathVariable long id){
        Optional<Player> player = playerRepository.findById(id);
        if(player.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok().build();
        }


    @PutMapping("/{id}")
    public ResponseEntity<Player> editUser(@PathVariable long id, @RequestBody Player player){
        Optional<Player> findPlayer= playerRepository.findById(id);
        if (findPlayer.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable long id){
        playerRepository.deleteById(id);
    }

}
