package com.pds.web.service;


import com.pds.common.api.UserApiDataCollection;
import com.pds.common.config.GenModelMapper;
import com.pds.common.config.ModelMapperConfig;
import com.pds.common.dto.UsersDto;
import com.pds.common.entity.Users;
import com.pds.common.enums.Ranks;
import com.pds.common.repository.UsersRepository;
import com.pds.web.exception.ErrorInfo;
import com.pds.web.exception.UserRequestException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {UserSearchService.class, ModelMapperConfig.class, GenModelMapper.class})
class UserServiceTest {

    @Autowired
    private UserSearchService userSearchService;

    @MockBean
    private UsersRepository usersRepository;

    @MockBean
    private UserApiDataCollection userApi;

    private final Users users = new Users("NAME","ID",250,2000);

    @Test
    void getUserInfoTest(){
        given(usersRepository.findByUserId("ID")).willReturn(Optional.of(users));
        UsersDto.Info result = userSearchService.getUserInfo("ID");
        assertNotNull(result);
        assertEquals("월드클래스1",Ranks.getRanks(result.getHighRank()).getRankInfo());
    }

    @Test
    void getUserInfoExceptionTest(){
        UserRequestException e = assertThrows(UserRequestException.class,()->
                userSearchService.getUserInfo("ID"));
        assertEquals(ErrorInfo.USER_BAD_ACCESS.getErrorCode(),e.getCode());
        assertEquals(ErrorInfo.USER_BAD_ACCESS.getErrorMsg(),e.getMessage());
    }

    private final UsersDto.UserApiResponse userDto = new UsersDto.UserApiResponse(
            new JSONObject().put("nickname","NAME").put("accessId","ID").put("level",250));

    @Test
    void findUserTest(){
        given(userApi.getUserInfo("NAME")).willReturn(userDto);
        given(userApi.getUserRank("ID")).willReturn(2000);
        UsersDto.Info resultDto = userSearchService.findUser("NAME");
        assertEquals(userDto.getUserName(),resultDto.getUserName());
        assertEquals(userDto.getUserId(),resultDto.getUserId());
    }
    @Test
    void findUserExceptionTest(){
        given(userApi.getUserInfo("NAME")).willReturn(null);
        UserRequestException e = assertThrows(UserRequestException.class,()->
                userSearchService.findUser("NAME"));
        assertEquals(ErrorInfo.USER_DATA_NOT_EXIST.getErrorCode(),e.getCode());
        assertEquals(ErrorInfo.USER_DATA_NOT_EXIST.getErrorMsg(),e.getMessage());
    }
    @Test
    void findUserExceptionInvalidNameTest(){
        UserRequestException e= assertThrows(UserRequestException.class,()->
                userSearchService.findUser("N"));
        assertEquals(ErrorInfo.PARAMETER_INVALID.getErrorCode(),e.getCode());
        assertEquals("닉네임을 2글자 이상 입력해주세요",e.getMessage());
    }
    @Test
    void createUserTest(){
        UsersDto.Info userInfoDto = new UsersDto.Info(userDto,2000);
        userSearchService.createUser(userInfoDto);
        verify(usersRepository,times(1)).save(any());
    }
    @Test
    void isValidNickNameTest(){
        assertTrue(userSearchService.isValidNickName("ABCD"));
        assertFalse(userSearchService.isValidNickName("A"));
        assertFalse(userSearchService.isValidNickName(null));
    }

    @Test
    void getMatchListFirstTimeTest(){
        List<String> matchCodeList = new ArrayList<>();
        matchCodeList.add("A");
        matchCodeList.add("B");
        given(userApi.getUserMatchesFromApi(userDto.getUserId(),30))
                .willReturn(matchCodeList);
        assertEquals(matchCodeList,userSearchService.getMatchListFirstTime(userDto.getUserId()));
    }
    @Test
    void getMatchListNoMatchFirstTimeTest(){
        List<String> matchCodeList = new ArrayList<>();
        String userId = userDto.getUserId();
        given(userApi.getUserMatchesFromApi(userId,30))
                .willReturn(matchCodeList);
        assertThrows(UserRequestException.class,()->
                userSearchService.getMatchListFirstTime(userId));
    }
}
