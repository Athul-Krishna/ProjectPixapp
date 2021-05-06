package com.athul.pixapp.api.users.services;

import com.athul.pixapp.api.users.data.AlbumsServiceClient;
import com.athul.pixapp.api.users.data.UserEntity;
import com.athul.pixapp.api.users.data.UsersRepository;
import com.athul.pixapp.api.users.shared.UserDto;
import com.athul.pixapp.api.users.ui.models.AlbumResponseModel;
import feign.FeignException;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UsersServiceImpl implements UsersService{

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

//    @Autowired
//    RestTemplate restTemplate;

    @Autowired
    @Qualifier("com.athul.pixapp.api.users.data.AlbumsServiceClient")
    AlbumsServiceClient albumsServiceClient;

    @Autowired
    Environment env;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public UserDto createUser(UserDto userDetails) {
        userDetails.setUserId(UUID.randomUUID().toString());
        userDetails.setEncryptedPassword(bCryptPasswordEncoder.encode(userDetails.getPassword()));
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserEntity userEntity = modelMapper.map(userDetails, UserEntity.class);
        usersRepository.save(userEntity);
        UserDto returnValue = modelMapper.map(userEntity, UserDto.class);
        return returnValue;
    }

    @Override
    public UserDto getUserDetailsByEmail(String email) {
        UserEntity userEntity = usersRepository.findByEmail(email);
        if(userEntity == null) throw new UsernameNotFoundException(email);
        return new ModelMapper().map(userEntity, UserDto.class);
    }

    @Override
    public UserDto getUserByUserId(String userId) {
        UserEntity userEntity = usersRepository.findByUserId(userId);
        if(userEntity == null) throw new UsernameNotFoundException("User not found!");
        UserDto userDto = new ModelMapper().map(userEntity, UserDto.class);
        /* Using RestTemplate
        String albumUrl = String.format(env.getProperty("albums.url"), userId);
        ResponseEntity<List<AlbumResponseModel>> albumsListResponse = restTemplate.exchange(albumUrl, HttpMethod.GET, null, new ParameterizedTypeReference<List<AlbumResponseModel>>() {});
        List<AlbumResponseModel> albumsList = albumsListResponse.getBody();
        */
        List<AlbumResponseModel> albumsList = albumsServiceClient.getAlbums(userId);
        userDto.setAlbums(albumsList);
        return userDto;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = usersRepository.findByEmail(username);
        if(userEntity == null) throw new UsernameNotFoundException(username);
        return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), true, true, true, true, new ArrayList<>());
    }
}
