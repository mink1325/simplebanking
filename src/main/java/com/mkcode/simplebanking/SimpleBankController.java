package com.mkcode.simplebanking;


import com.mkcode.simplebanking.model.Account;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;

@RestController("/")
public class SimpleBankController {

    @GetMapping(path = "/accounts/{accountNo}")
    public Account getAccountBalance(@PathParam("accountNo") String accountNo){
        return new Account(accountNo, Math.random());
    }
}
