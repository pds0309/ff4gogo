package com.pds.common.api;

import java.util.HashMap;
import java.util.List;

public interface WhoseMatchDetail<T> {
    T fromJSONToDetailMatch(HashMap<String , List<String>> matchList);
}
