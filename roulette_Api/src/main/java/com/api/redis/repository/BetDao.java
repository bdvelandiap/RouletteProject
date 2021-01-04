package com.api.redis.repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import com.api.redis.entity.Bet;
import java.util.List;
@Repository
public class BetDao {
    public static final String HASH_KEY = "Bet";
    @Autowired
    private RedisTemplate template;
    public Bet save(Bet bet){
        template.opsForHash().put(HASH_KEY,bet.getId(),bet);
        return bet;
    }
    public List<Bet> findAll(){
        return template.opsForHash().values(HASH_KEY);
    }
    public Bet findBetById(String id){
        return (Bet) template.opsForHash().get(HASH_KEY,id);
    }
}