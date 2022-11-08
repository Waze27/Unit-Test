package com.develhope.Unit_Test_01.repository;

import com.develhope.Unit_Test_01.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player, Long> {
}
