package io.chark.food.app.sandwich;

import io.chark.food.domain.sandwich.Basket;
import io.chark.food.domain.sandwich.BasketRepository;
import io.chark.food.domain.sandwich.Sandwich;
import io.chark.food.domain.sandwich.SandwichRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class SandwichService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SandwichService.class);

    private final SandwichRepository sandwichRepository;
    private final BasketRepository basketRepository;

    @Autowired
    public SandwichService(SandwichRepository sandwichRepository, BasketRepository basketRepository) {
        this.sandwichRepository = sandwichRepository;
        this.basketRepository = basketRepository;
    }

    public List<Basket> getBaskets(String sandwichName) {
        if (sandwichName == null) {
            return basketRepository.findAll();
        } else {
            return basketRepository.findBySandwichName(sandwichName);
        }
    }

    public Basket getBasket(long id) {
        return basketRepository.findOne(id);
    }

    public List<Basket> deleteBasket(long id) {
        basketRepository.delete(id);
        return basketRepository.findAll();
    }

    @PostConstruct
    public void init() {
        Basket basket = createBasket();
        basket.addSandwich(createSandwich(1, "Nom nom"));
        basket.addSandwich(createSandwich(2, "Ohh ye"));
        basket.addSandwich(createSandwich(6, "Best sandwich"));
        basketRepository.save(basket);

        basket = createBasket();
        basket.addSandwich(createSandwich(100, "Whoa"));
        basketRepository.save(basket);
    }

    public Basket createBasket() {
        Basket basket = basketRepository.save(new Basket());
        LOGGER.debug("Created new Basket{id={}}", basket.getId());
        return basket;
    }

    public Basket createBasket(Basket data) {
        Basket basket = basketRepository.save(new Basket());
        for (Sandwich sandwich : data.getSandwiches()) {
            basket.addSandwich(createSandwich(sandwich.getWeight(), sandwich.getName()));
        }
        return basketRepository.save(basket);
    }

    public Sandwich createSandwich(int weight, String name) {
        Sandwich sandwich = sandwichRepository.save(new Sandwich(weight, name));
        LOGGER.debug("Created new Sandwich{id={}, weight={}, name='{}'}", sandwich.getId(), weight, name);
        return sandwich;
    }
}