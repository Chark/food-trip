package io.chark.food.app.restaurant.details;

import io.chark.food.app.administrate.audit.AuditService;
import io.chark.food.domain.authentication.account.Account;
import io.chark.food.domain.restaurant.Bank;
import io.chark.food.domain.restaurant.BankRepository;
import io.chark.food.domain.restaurant.RestaurantDetailsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static io.chark.food.domain.authentication.permission.Permission.Authority.ROLE_USER;

/**
 * Created by SuperPC on 5/21/2016.
 */
@Service
public class BankService {

    private final AuditService auditService;
    private static final Logger LOGGER = LoggerFactory.getLogger(BankService.class);
    private final BankRepository bankRepository;

    @Autowired
    BankService(BankRepository bankRepository,AuditService auditService)
    {
        this.bankRepository = bankRepository;
        this.auditService = auditService;
    }

    public Optional<Bank> register(String name) {
        Bank bank = new Bank(
                name);





        try {
            bank = bankRepository.save(bank);
            LOGGER.debug("Created new bank{name='{}'}",
                    name);

            auditService.info("Created a new Bank using name: %s ",name);
        } catch (DataIntegrityViolationException e) {
            LOGGER.error("Failed while creating new bank{name='{}'}",
                    name);

            auditService.error("Failed to create a new bank using name: %s",name);
            return Optional.empty();
        }
        return Optional.of(bank);
    }


    public List<Bank> getBanks()
    {
        return bankRepository.findAll();
    }


}
