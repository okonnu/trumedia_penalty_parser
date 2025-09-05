package com.trumedia.nfl;

import java.util.LinkedList;
import java.util.List;

public class PenaltyParser
{
    public enum PenaltyResult { ACCEPTED, DECLINED, OFFSETTING, SUPERSEDED }

    public static List<PenaltyDetail> parsePenalties(String playDesc) throws PenaltyParsingException
    {
        // TODO - Implement this to parse the playDesc string and return
        // an array of PenaltyDetail objects that represent the penalties on that play.
        return new LinkedList<PenaltyDetail>();
    }
}
