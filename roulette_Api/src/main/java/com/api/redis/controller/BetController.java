package com.api.redis.controller;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.api.redis.entity.Roulette;
import com.api.redis.repository.RouletteDao;
import com.api.redis.entity.Bet;
import com.api.redis.repository.BetDao;
@RestController
@RequestMapping("/bet")
public class BetController {
	@Autowired
	private BetDao betDao;
	@Autowired
	private RouletteDao rouletteDao;
	@PostMapping("/save")
	public ResponseEntity<?> save(@RequestHeader("userId") String userId, @RequestBody Bet bet){
		Roulette roulette = rouletteDao.findRouletteById(bet.getRouletteId());
		UUID id = UUID.randomUUID(); 
		bet.setId(id.toString());
		bet.setUserId(userId);
		bet.setState("abierta");
		if (roulette == null) {
			return new ResponseEntity<>( "Ruleta No Encontrada", HttpStatus.NOT_FOUND);
		}else if (!roulette.getState().equals("abierta")) {
			return new ResponseEntity<>( "La Ruleta Esta Cerrada", HttpStatus.BAD_REQUEST);
		}
		if(bet.getBetAmount() <= 0 || bet.getBetAmount() > 10000) {
			return new ResponseEntity<>( "Monto no aceptado", HttpStatus.BAD_REQUEST);
		}
		if((bet.getBetRouletteColor() == null && bet.getBetRouletteNumber() == null) || 
				(bet.getBetRouletteColor() != null && bet.getBetRouletteNumber() != null)) {
			return new ResponseEntity<>( "Apuesta invalida", HttpStatus.BAD_REQUEST);
		}
		if((bet.getBetRouletteColor() != null) && (!bet.getBetRouletteColor().equals("rojo") 
				&& !bet.getBetRouletteColor().equals("negro"))){
			return new ResponseEntity<>( "color invalido", HttpStatus.BAD_REQUEST);
		}
		if((bet.getBetRouletteNumber() != null) && (bet.getBetRouletteNumber() < 0 || bet.getBetRouletteNumber() > 36)){
			return new ResponseEntity<>( "numero de apuesta invalido", HttpStatus.BAD_REQUEST);
		}
		try {
			betDao.save(bet);
			return new ResponseEntity<>( "Apuesta exitosa", HttpStatus.CREATED);
		}catch (Exception e){
			return new ResponseEntity<>( "No se pudo completar la operacion", HttpStatus.BAD_REQUEST);
		}
	}
	@GetMapping("/all")
	public List<Bet> getAllBets(){
		return betDao.findAll();
	}
	@GetMapping("/closeBet/{rouletteId}")
	public ResponseEntity<?> getBetsResults(@PathVariable String rouletteId){
		List<Bet> allBets = betDao.findAll();
		List<Bet> betsToCheck = new ArrayList<Bet>();
		Roulette roulette = rouletteDao.findRouletteById(rouletteId);
		if(!roulette.getState().equals("abierta")) {
			return new ResponseEntity<>( "La Ruleta No Está Abierta", HttpStatus.BAD_REQUEST);
		}
		for (int i = 0; i < allBets.size(); i++) {
			if(allBets.get(i).getRouletteId().equals(rouletteId) && allBets.get(i).getState().equals("abierta")) {
				betsToCheck.add(betsToCheck.size(), allBets.get(i));
			}
		}
		checkBets(betsToCheck);
		return new ResponseEntity<>(betsToCheck, HttpStatus.OK);
	}
	public void checkBets (List<Bet> betsToCheck) {
		Random rn = new Random();
		String winningColor;
		Bet bet = new Bet();
		int winningNumbrer = rn.nextInt(37);
		System.out.println("Num Ganador: "+winningNumbrer);
		if (winningNumbrer%2==0) {
			winningColor = "rojo";
		}else {
			winningColor = "negro";
		}
		for (int i = 0; i < betsToCheck.size(); i++) {
			if(betsToCheck.get(i).getBetRouletteColor() != null) {
				if(betsToCheck.get(i).getBetRouletteColor().equals(winningColor)) {
					betsToCheck.get(i).setAmountAfterBet(betsToCheck.get(i).getBetAmount()*1.8);
					bet = betDao.findBetById(betsToCheck.get(i).getId());
					bet.setState("cerrada");
					bet.setAmountAfterBet(betsToCheck.get(i).getBetAmount()*1.8);
					betDao.save(bet);
				}
			}else {
				if(betsToCheck.get(i).getBetRouletteNumber() == winningNumbrer) {
					betsToCheck.get(i).setAmountAfterBet(betsToCheck.get(i).getBetAmount()*5);
					bet = betDao.findBetById(betsToCheck.get(i).getId());
					bet.setState("cerrada");
					bet.setAmountAfterBet(betsToCheck.get(i).getBetAmount()*5);
					betDao.save(bet);
				}			
			}
		}
	}
}
