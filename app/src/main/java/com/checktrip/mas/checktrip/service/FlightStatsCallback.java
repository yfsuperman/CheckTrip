package com.checktrip.mas.checktrip.service;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by FanYang on 10/19/16.
 */

public interface FlightStatsCallback {
    void serviceSuccess(JSONObject flightStatuses);

    void serviceFailure(Exception exception);

}
