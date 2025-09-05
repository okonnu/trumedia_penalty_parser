package com.trumedia.nfl;

public class PenaltyParsingException extends Exception
{
    private static final long serialVersionUID = 1L;

    public PenaltyParsingException(String penaltySentence)
    {
        super(penaltySentence);
    }
}
