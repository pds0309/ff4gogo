package com.pds.web.service;


import com.pds.web.api.MatchDeliver;
import com.pds.web.api.MatchDto;
import com.pds.web.api.MatchFinder;
import com.pds.web.exception.ErrorInfo;
import com.pds.web.exception.UserRequestException;
import lombok.RequiredArgsConstructor;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserMatchService {

    private final MatchFinder matchFinder;

    public MatchDto.Info getDetailMatchList(String matchCode, String id) {
        try {
            List<JSONObject> objects = matchFinder.getDetailMatchJsonListFromCodeList(matchCode, id);
            return new MatchDto.Info(
                    MatchDeliver.basicInfo(objects),
                    MatchDeliver.summaryInfo(objects),
                    MatchDeliver.passInfo(objects),
                    MatchDeliver.playerInfo(objects),
                    MatchDeliver.shootInfo(objects));
        } catch (JSONException | NullPointerException e) {
            return null;
        }
    }
}
