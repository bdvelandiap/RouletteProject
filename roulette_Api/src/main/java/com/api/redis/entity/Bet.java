package com.api.redis.entity;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
@RedisHash("Bet")
public class Bet implements Serializable{
	@Id
	private String id;
	private String rouletteId;
	private String userId;
	private double betAmount;
	private Integer betRouletteNumber;
	private String betRouletteColor;
	private String state;
	private double amountAfterBet;
}
