package com.trumedia.nfl;

import java.util.LinkedList;
import java.util.List;

public class PenaltyParser {

    // would be better to have all enums in their own files on one folder.
    public enum PenaltyResult { ACCEPTED, DECLINED, OFFSETTING, SUPERSEDED }

    public static List<PenaltyDetail> parsePenalties(String playDesc) throws PenaltyParsingException
    {
        // TODO - Implement this to parse the playDesc string and return
        // an array of PenaltyDetail objects that represent the penalties on that play.

        /*
        example 1:
        (13:20) (Shotgun) 10-C.Rush pass short middle to 9-K.Turpin to DAL 49 for 12 yards (9-J.Tryon) [0-Y.Diaby].
        Penalty on DAL-67-B.Hoffman, Offensive Holding, offsetting, enforced at DAL 37 - No Play.
        Penalty on TB-23-T.Smith, Illegal Use of Hands, offsetting.

        example 2:
        (9:28) (No Huddle) PENALTY on LA-73-D.Edwards, False Start, 5 yards, enforced at LA 22 - No Play.

        based on the text above, penalties start with the word "Penalty on"
        so we will split the playDesc string on that phrase, ignoring the first part
        then pass each penalty part to the PenaltyDetail constructor

         */

        // this can be transferred to the properties file later for easier configuration
        String penaltyPrefix = "(?i)Penalty on";
        List<PenaltyDetail> penaltyDetails = new LinkedList<PenaltyDetail>();

        String[] parts = playDesc.split(penaltyPrefix);
        for (int i = 1; i < parts.length ; i++) {
            penaltyDetails.add(new PenaltyDetail(parts[i].trim()));
        }

        return penaltyDetails;
    }
}
