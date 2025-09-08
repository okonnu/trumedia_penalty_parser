package com.trumedia.nfl;

import java.util.List;

import com.trumedia.core.StringUtil;
import com.trumedia.nfl.PenaltyParser.PenaltyResult;

public class PenaltyDetail
{
    /*
     * TODO Implement the PenaltyDetail class
     * Hint - you will need to add at least one argument to the default constructor,
     * but it is up to you to define the constructor argument(s) and instance methods.
     */

    PenaltyResult result;
    String team;
    String type;
    String player;
    int yards;

    public PenaltyDetail(String playDesc ) throws PenaltyParsingException
    {
        // TODO Implement the constructor to extract the penalty details for one of the penalties on a play
//        System.out.println("Penalty team: " + extractTeam(playDesc));
//        System.out.println("Penalty result: " + extractResult(playDesc));
//        System.out.println("Penalty type: " + extractType(playDesc));
//        System.out.println("Penalty player: " + extractPlayer(playDesc));
//        System.out.println("Penalty yards: " + extractYards(playDesc));

        if (StringUtil.isEmpty(playDesc))
            throw new PenaltyParsingException("Play description is empty");
        this.team = extractTeam(playDesc);
        this.result = extractResult(playDesc);
        this.type = extractType(playDesc);
        this.player = extractPlayer(playDesc);
        String yardsStr = extractYards(playDesc);
        if (yardsStr != null)
            try {
                this.yards = Integer.parseInt(yardsStr);
            }
            catch (NumberFormatException nfe)
            {
                throw new PenaltyParsingException("#### Could not parse yards, integer conversion error: " + yardsStr);
            }


    }

    public PenaltyResult extractResult(String desc) {

        if (desc.contains("declined")) {
            return PenaltyResult.DECLINED;
        }
        // Then check for offsetting
        else if (desc.contains("offsetting")) {
            return PenaltyResult.OFFSETTING;
        }
        // Then check for superseded (correct spelling)
        else if (desc.contains("superseded")) {
            return PenaltyResult.SUPERSEDED;
        }
        // If none of the above, but has enforcement info, it's accepted
        else if (desc.contains("enforced") || desc.contains("placed at")) {
            return PenaltyResult.ACCEPTED;
        } else {
            System.out.println("Could not determine penalty result from description: " + desc);
            return null; // or throw an exception if preferred
        }
    }

    String extractTeam(String desc) {
        /*
            Example:
            ATL-67-D.Dalman, Offensive Holding, declined.
            ATL, Delay of Game, 5 yards, enforced at 50 - No Play.

            based on the example, we split on the first comma, then split on the first dash if size > 3
         */
        String[] parts = desc.split(",");
        if (parts[0].length() > 3) {
            return parts[0].split("-")[0].trim();
        }
        return parts[0].trim();
    }

    String extractType(String desc) {
        /*
            Example:
            ATL-67-D.Dalman, Offensive Holding, declined.
            ATL, Delay of Game, 5 yards, enforced at 50 - No Play.

            based on the example, we split commas, then return index 1
        */
        String[] parts = desc.split(",");
        if (parts.length > 1) {
            return parts[1].trim();
        }
        return null;
    }

    String extractPlayer(String desc) {
        /*
            Example:
            ATL-67-D.Dalman, Offensive Holding, declined.
            ATL, Delay of Game, 5 yards, enforced at 50 - No Play.

            based on the example, we fiers check if "yards" is in the description, if so, we split on commas, then split on dashes and return index 1 and 2
         */
        String[] parts = desc.split(",");
        if (parts[0].length() > 3) {
            String[] subParts = parts[0].split("-");
            if (subParts.length > 2) {
                return subParts[1].trim() + "-" + subParts[2].trim();
            }
        }
//        System.out.println("Could not extract player from description: " + desc);
        return null;
    }

    String extractYards(String desc) {
        /*
            Example:
            ATL-67-D.Dalman, Offensive Holding, declined.
            ATL, Delay of Game, 5 yards, enforced at 50 - No Play.

            Based on the exmple, we split on the word "yards", then split on spaces, then return the last part trimmed
             */

        String[] parts = desc.split("yards");
        if (parts.length > 1) {
            String[] subParts = parts[0].split(" ");
            if (subParts.length > 1) {
                return subParts[subParts.length - 1].trim();
            }
        }
        System.out.println("Could not extract yards from description: " + desc);
        return null;
    }

}
