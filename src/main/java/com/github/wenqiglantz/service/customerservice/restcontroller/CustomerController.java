package com.github.wenqiglantz.service.customerservice.restcontroller;

import com.github.wenqiglantz.service.customerservice.data.CustomerVO;
import com.github.wenqiglantz.service.customerservice.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/customers",
        produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "customer", description = "Operations pertaining to customer service")
public class CustomerController {

    private static final String JSON = MediaType.APPLICATION_JSON_VALUE;

    private final CustomerService customerService;

    @Operation(summary = "Create customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully created a customer"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "401", description = "Authorization denied"),
            @ApiResponse(responseCode = "500", description = "Unexpected system exception"),
            @ApiResponse(responseCode = "502", description = "An error has occurred with an upstream service")
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createCustomer(@Valid @RequestBody CustomerVO customerVO, UriComponentsBuilder uriBuilder)
            throws Exception {
        CustomerVO newCustomerVO = customerService.saveCustomer(customerVO);
        URI location = uriBuilder
                .path("/customers/{customerId}")
                .buildAndExpand(newCustomerVO.getCustomerId())
                .toUri();
        return ResponseEntity.created(location)
                .contentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE))
                .body(CustomerVO.builder()
                        .customerId(newCustomerVO.getCustomerId())
                        .firstName(newCustomerVO.getFirstName())
                        .lastName(newCustomerVO.getLastName())
                        .build());
    }

    @Operation(summary = "Retrieve all customers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all customers"),
            @ApiResponse(responseCode = "401", description = "Authorization denied"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Unexpected system exception")
    })
    @GetMapping(produces = JSON)
    public ResponseEntity<Map> getCustomers() {
        Map data = new HashMap();
        data.put("data", customerService.getCustomers());
        return ResponseEntity.ok(data);
    }

    @Operation(summary = "Retrieve the customer details given the customer Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved a customer"),
            @ApiResponse(responseCode = "401", description = "Authorization denied"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Unexpected system exception")
    })
    @GetMapping(value = "/{customerId}")
    public ResponseEntity getCustomer(@PathVariable String customerId) {
        return ResponseEntity.ok(customerService.getCustomer(customerId));
    }

    @Operation(summary = "Update Customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully updated a customer"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "401", description = "Authorization denied"),
            @ApiResponse(responseCode = "500", description = "Unexpected system exception"),
            @ApiResponse(responseCode = "502", description = "An error has occurred with an upstream service")
    })
    @PutMapping(value = "/{customerId}", consumes = JSON)
    public ResponseEntity updateCustomer(@PathVariable String customerId, @RequestBody CustomerVO customerVO)
            throws Exception {
        customerService.updateCustomer(customerId, customerVO);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Delete Customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully deleted a customer"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "401", description = "Authorization denied"),
            @ApiResponse(responseCode = "500", description = "Unexpected system exception"),
            @ApiResponse(responseCode = "502", description = "An error has occurred with an upstream service")
    })
    @DeleteMapping(value = "/{customerId}")
    public ResponseEntity deleteCustomer(@PathVariable String customerId) throws Exception {
        customerService.deleteCustomer(customerId);
        return ResponseEntity.noContent().build();
    }
}
