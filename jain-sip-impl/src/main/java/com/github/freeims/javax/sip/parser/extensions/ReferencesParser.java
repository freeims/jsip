package com.github.freeims.javax.sip.parser.extensions;

import java.text.ParseException;

import com.github.freeims.core.Token;
import com.github.freeims.javax.sip.header.Reason;
import com.github.freeims.javax.sip.header.ReasonList;
import com.github.freeims.javax.sip.header.SIPHeader;
import com.github.freeims.javax.sip.header.extensions.References;
import com.github.freeims.javax.sip.parser.Lexer;
import com.github.freeims.javax.sip.parser.ParametersParser;
import com.github.freeims.javax.sip.parser.TokenTypes;

public class ReferencesParser extends ParametersParser {
    /**
     * Creates a new instance of ReferencesParser
     * 
     * @param references the header to parse
     */
    public ReferencesParser(String references) {
        super(references);
    }

    /**
     * Constructor
     * 
     * @param lexer the lexer to use to parse the header
     */
    protected ReferencesParser(Lexer lexer) {
        super(lexer);
    }

    /**
     * parse the String message
     * 
     * @return SIPHeader (ReasonParserList object)
     * @throws SIPParseException if the message does not respect the spec.
     */
    public SIPHeader parse() throws ParseException {

        if (debug)
            dbg_enter("ReasonParser.parse");

        try {
            headerName(TokenTypes.REFERENCES);
            References references = new References();
            this.lexer.SPorHT();

            String callId = lexer.byteStringNoSemicolon();

            references.setCallId(callId);
            super.parse(references);
            return references;
        } finally {
            if (debug)
                dbg_leave("ReferencesParser.parse");
        }

    }

}
