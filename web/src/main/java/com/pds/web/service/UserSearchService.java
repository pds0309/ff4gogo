package com.pds.web.service;


import com.pds.common.api.UserApiDataCollection;
import com.pds.common.config.GenModelMapper;
import com.pds.common.dto.UsersDto;
import com.pds.common.entity.Users;
import com.pds.common.repository.UsersRepository;
import com.pds.web.exception.ErrorInfo;
import com.pds.web.exception.UserRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserSearchService {

    private final UserApiDataCollection userApi;
    private final UsersRepository usersRepository;
    private final GenModelMapper<UsersDto.Info> modelMapper;


    public UsersDto.Info getUserInfo(String id){
        return modelMapper.entityToDto(usersRepository.findByUserId(id)
                .orElseThrow(()->
                        new UserRequestException(ErrorInfo.USER_BAD_ACCESS)),UsersDto.Info.class);
    }

    public UsersDto.Info findUser(String nickName){
        if(!isValidNickName(nickName)){
            throw new UserRequestException("닉네임을 2글자 이상 입력해주세요",ErrorInfo.PARAMETER_INVALID.getErrorCode());
        }
        UsersDto.UserApiResponse apiDto = userApi.getUserInfo(nickName);
        if(apiDto==null){
            throw new UserRequestException(ErrorInfo.USER_DATA_NOT_EXIST);
        }
        return new UsersDto.Info(apiDto,userApi.getUserRank(apiDto.getUserId()));
    }

    public UsersDto.Info createUser(UsersDto.Info userDto){
        usersRepository.save(Users.builder(userDto).build());
        log.info(userDto.getUserName() + " Saved");
        return userDto;
    }
    public boolean isValidNickName(String nickName){
        return nickName != null && nickName.length() >= 2;
    }
}
