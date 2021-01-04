package com.api.redis.controller;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.api.redis.entity.Roulette;
import com.api.redis.repository.RouletteDao;
@RestController
@RequestMapping("/roulette")
public class RouletteController {
	@Autowired
	private RouletteDao dao;
	@PostMapping("/save")
	public String save(){
		Roulette roulette = new Roulette();
		UUID id = UUID.randomUUID(); 
		roulette.setId(id.toString());
		roulette.setState("cerrada");
		dao.save(roulette);
		return id.toString();
	}
	@GetMapping("/all")
	public List<Roulette> getAllRoulettes(){
		return dao.findAll();
	}
	@GetMapping("/find/{id}")
	public Roulette findRouletteById(@PathVariable String id) {
		return dao.findRouletteById(id);
	}
	@PostMapping("/open/{id}")
	public ResponseEntity<?> openRouletteById(@PathVariable String id) {
		try {
			Roulette roulette = dao.findRouletteById(id);
			roulette.setState("abierta");
			dao.save(roulette);
			return new ResponseEntity<>( "OK", HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<>( "Ruleta No Encontrada", HttpStatus.NOT_FOUND);
		}
	}
}
