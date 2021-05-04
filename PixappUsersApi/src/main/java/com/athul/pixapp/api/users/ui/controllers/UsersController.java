package com.athul.pixapp.api.users.ui.controllers;

import com.athul.pixapp.api.users.services.UsersService;
import com.athul.pixapp.api.users.shared.UserDto;
import com.athul.pixapp.api.users.ui.models.CreateUserRequestModel;
import com.athul.pixapp.api.users.ui.models.CreateUserResponseModel;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
public class UsersController {

    @Autowired
    Environment env;

    @Autowired
    UsersService usersService;

    @GetMapping("/status")
    public String status() {
        return "Working on port " + env.getProperty("local.server.port");
    }

    @PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE },
            produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public ResponseEntity<CreateUserResponseModel> createUser(@Valid @RequestBody CreateUserRequestModel userDetails) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserDto userDto = modelMapper.map(userDetails, UserDto.class);
        UserDto createdUser = usersService.createUser(userDto);
        CreateUserResponseModel returnValue = modelMapper.map(createdUser, CreateUserResponseModel.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(returnValue);
    }
}
