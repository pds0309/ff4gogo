package com.pds.web.service;



import com.pds.web.api.MatchDto;
import com.pds.web.exception.ErrorInfo;
import com.pds.web.exception.UserRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
// 전달된 유저 순경기록을 바탕으로 부가적인 정보를 제공해주는 서비스
public class UserDetailService {

    public List<MatchDto.BestDto> getMyMvpList(List<MatchDto.MatchPlayerDto> matchPlayerDtoList){
        //유저 최근경기가 모두 조기 몰수 처리되었다면 선수 데이터가 없을 수도 있다. 예외처리가 필요하다.
        if(matchPlayerDtoList.isEmpty()){
            throw new UserRequestException(ErrorInfo.FF4_API_ERROR);
        }
        Map<Integer , MatchDto.BestDto> playerMap = new HashMap<>();
        for (MatchDto.MatchPlayerDto matchPlayerDto : matchPlayerDtoList) {
            int id = matchPlayerDto.getSpId();
            if (!playerMap.containsKey(id)) {
                playerMap.put(id, MatchDto.BestDto.builder(matchPlayerDto).build());
            } else {
                playerMap.get(id).setAll(matchPlayerDto);
            }
        }
        return new ArrayList<>(playerMap.values());
    }
}
