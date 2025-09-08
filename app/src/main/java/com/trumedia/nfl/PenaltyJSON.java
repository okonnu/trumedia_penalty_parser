package com.trumedia.nfl;

import org.json.JSONObject;
import com.trumedia.nfl.PenaltyParser.PenaltyResult;

public class PenaltyJSON
{
    private PenaltyDetail penaltyDetail;

    public PenaltyJSON(PenaltyDetail penaltyDetail)
    {
        this.penaltyDetail = penaltyDetail;
    }

    public JSONObject toJSONObject()
    {
        JSONObject jsonObject = new JSONObject();

        if (penaltyDetail.getResult() != null) {
            jsonObject.put("result", penaltyDetail.getResult().toString());
        }

        if (penaltyDetail.getTeam() != null) {
            jsonObject.put("team", penaltyDetail.getTeam());
        }

        if (penaltyDetail.getType() != null) {
            jsonObject.put("type", penaltyDetail.getType());
        }

        if (penaltyDetail.getPlayer() != null) {
            jsonObject.put("player", penaltyDetail.getPlayer());
        }

        // Only include yards if it was successfully parsed and is greater than 0
        if (penaltyDetail.hasYards()) {
            jsonObject.put("yards", penaltyDetail.getYards());
        }

        return jsonObject;
    }
}
