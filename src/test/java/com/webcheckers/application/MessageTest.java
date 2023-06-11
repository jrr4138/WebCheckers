package com.webcheckers.application;

import com.webcheckers.model.states.MessageType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

class MessageTest{
    private final String TEXT = "Test Text!";
    private final MessageType TYPE = MessageType.INFO;
    private Message msg;


    @BeforeEach
    void setup(){
        msg = new Message(TEXT, TYPE);
    }

    @Test
    void getText(){
        String tmp = msg.getText();
        assertEquals(TEXT, tmp);
    }

    @Test
    void getType(){
        MessageType tmp = msg.getType();
        assertEquals(TYPE,tmp);
    }



}