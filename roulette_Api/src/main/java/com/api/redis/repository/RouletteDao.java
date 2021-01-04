package com.api.redis.repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import com.api.redis.entity.Roulette;
import java.util.List;
@Repository
public class RouletteDao {
    public static final String HASH_KEY = "Roulette";
    @Autowired
    private RedisTemplate template;
    public Roulette save(Roulette roulette){
        template.opsForHash().put(HASH_KEY,roulette.getId(),roulette);
        return roulette;
    }
    public List<Roulette> findAll(){
        return template.opsForHash().values(HASH_KEY);
    }
    public Roulette findRouletteById(String id){
        return (Roulette) template.opsForHash().get(HASH_KEY,id);
    }
}





