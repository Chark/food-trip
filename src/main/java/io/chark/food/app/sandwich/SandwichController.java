package io.chark.food.app.sandwich;

import io.chark.food.domain.sandwich.Basket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/baskets")
public class SandwichController {

    private final SandwichService sandwichService;

    @Autowired
    public SandwichController(SandwichService sandwichService) {
        this.sandwichService = sandwichService;
    }

    /**
     * GET /baskets/1
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Basket getBasket(@PathVariable long id) {
        return sandwichService.getBasket(id);
    }

    /**
     * DELETE /baskets/1
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public List<Basket> deleteBasket(@PathVariable long id) {
        return sandwichService.deleteBasket(id);
    }

    /**
     * GET /baskets
     * GET /baskets?sandwichName=Ohh
     */
    @RequestMapping(method = RequestMethod.GET)
    public List<Basket> getBaskets(@RequestParam(required = false) String sandwichName) {
        return sandwichService.getBaskets(sandwichName);
    }

    /**
     * POST /baskets
     * {
     *     "sandwiches": [
     *         {
     *             "name": "Top kek",
     *             "weight": 123
     *         },
     *         {
     *             "name": "Other",
     *             "weight": 312
     *         }
     *     ]
     * }
     */
    @RequestMapping(method = RequestMethod.POST)
    public Basket createBasket(@RequestBody Basket basket) {
        return sandwichService.createBasket(basket);
    }
}