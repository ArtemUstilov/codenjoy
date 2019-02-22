package com.codenjoy.dojo.web.controller;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2019 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import com.codenjoy.dojo.services.ConfigProperties;
import com.codenjoy.dojo.services.dao.Registration;
import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.Callable;

import static com.codenjoy.dojo.web.controller.Validator.CANT_BE_NULL;
import static com.codenjoy.dojo.web.controller.Validator.CAN_BE_NULL;
import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class ValidatorTest {

    private ConfigProperties properties;
    private Registration registration;
    private Validator validator;

    @Before
    public void setUp() {
        validator = new Validator(){{
            ValidatorTest.this.registration = this.registration = mock(Registration.class);
            ValidatorTest.this.properties = this.properties = mock(ConfigProperties.class);
        }};
    }

    @Test
    public void validateCode() {
        shouldError("Player code is invalid: 'null'",
                () -> validator.checkCode(null, CANT_BE_NULL));

        shouldOk(() -> validator.checkCode(null, CAN_BE_NULL));

        shouldError("Player code is invalid: ''",
                () -> validator.checkCode("", CANT_BE_NULL));

        shouldOk(() -> validator.checkCode("", CAN_BE_NULL));

        shouldError("Player code is invalid: 'NuLL'",
                () -> validator.checkCode("NuLL", CANT_BE_NULL));

        shouldOk(() -> validator.checkCode("NuLL", CAN_BE_NULL));

        shouldError("Player code is invalid: 'null'",
                () -> validator.checkCode("null", CANT_BE_NULL));

        shouldOk(() -> validator.checkCode("null", CAN_BE_NULL));

        shouldError("Player code is invalid: 'NULL'",
                () -> validator.checkCode("NULL", CANT_BE_NULL));

        shouldOk(() -> validator.checkCode("NULL", CAN_BE_NULL));

        shouldError("Player code is invalid: '*F(@DF^@(&@DF(@^'",
                () -> validator.checkCode("*F(@DF^@(&@DF(@^", CANT_BE_NULL));

        shouldError("Player code is invalid: 'too large aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa'",
                () -> validator.checkCode("too large aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", CANT_BE_NULL));

        shouldOk(() -> validator.checkCode("1", CANT_BE_NULL));

        shouldOk(() -> validator.checkCode("0", CAN_BE_NULL));

        shouldOk(() -> validator.checkCode("434589345613405760956134056340596345903465", CANT_BE_NULL));

        shouldError("Player code is invalid: 'someId'",
                () -> validator.checkCode("someId", CANT_BE_NULL));

        shouldError("Player code is invalid: 'some@email.com'",
                () -> validator.checkCode("some@email.com", CANT_BE_NULL));
    }

    @Test
    public void validateGameName() {
        shouldError("Game name is invalid: 'null'",
                () -> validator.checkGameName(null, CANT_BE_NULL));

        shouldOk(() -> validator.checkGameName(null, CAN_BE_NULL));

        shouldError("Game name is invalid: ''",
                () -> validator.checkGameName("", CANT_BE_NULL));

        shouldOk(() -> validator.checkGameName("", CAN_BE_NULL));

        shouldError("Game name is invalid: 'NuLL'",
                () -> validator.checkGameName("NuLL", CANT_BE_NULL));

        shouldOk(() -> validator.checkGameName("NuLL", CAN_BE_NULL));

        shouldError("Game name is invalid: 'null'",
                () -> validator.checkGameName("null", CANT_BE_NULL));

        shouldOk(() -> validator.checkGameName("null", CAN_BE_NULL));

        shouldError("Game name is invalid: 'NULL'",
                () -> validator.checkGameName("NULL", CANT_BE_NULL));

        shouldOk(() -> validator.checkGameName("NULL", CAN_BE_NULL));

        shouldError("Game name is invalid: '*F(@DF^@(&@DF(@^'",
                () -> validator.checkGameName("*F(@DF^@(&@DF(@^", CANT_BE_NULL));

        shouldError("Game name is invalid: 'too large aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa'",
                () -> validator.checkGameName("too large aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", CANT_BE_NULL));

        shouldError("Game name is invalid: '-game'",
                () -> validator.checkGameName("-game", CANT_BE_NULL));

        shouldError("Game name is invalid: 'game-'",
                () -> validator.checkGameName("game-", CANT_BE_NULL));

        shouldOk(() -> validator.checkGameName("a-game", CANT_BE_NULL));

        shouldError("Game name is invalid: '_game'",
                () -> validator.checkGameName("_game", CANT_BE_NULL));

        shouldError("Game name is invalid: 'game_'",
                () -> validator.checkGameName("game_", CANT_BE_NULL));

        shouldOk(() -> validator.checkGameName("a_game", CANT_BE_NULL));

        shouldError("Game name is invalid: '.game'",
                () -> validator.checkGameName(".game", CANT_BE_NULL));

        shouldError("Game name is invalid: 'game.'",
                () -> validator.checkGameName("game.", CANT_BE_NULL));

        shouldOk(() -> validator.checkGameName("a.game", CANT_BE_NULL));

        shouldError("Game name is invalid: '1'",
                () -> validator.checkGameName("1", CANT_BE_NULL));

        shouldOk(() -> validator.checkGameName("a1", CANT_BE_NULL));

        shouldOk(() -> validator.checkGameName("a1", CANT_BE_NULL));

        shouldError("Game name is invalid: '0'",
                () -> validator.checkGameName("0", CAN_BE_NULL));

        shouldError("Game name is invalid: '434589345613405760956134056340596345903465'",
                () -> validator.checkGameName("434589345613405760956134056340596345903465", CANT_BE_NULL));

        shouldOk(() -> validator.checkGameName("someGame", CANT_BE_NULL));

        shouldError("Game name is invalid: 'some@email.com'",
                () -> validator.checkGameName("some@email.com", CANT_BE_NULL));
    }

    @Test
    public void validateMd5() {
        shouldError("Hash is invalid: 'null'",
                () -> validator.checkMD5(null));

        shouldError("Hash is invalid: ''",
                () -> validator.checkMD5(""));

        shouldError("Hash is invalid: 'NuLL'",
                () -> validator.checkMD5("NuLL"));

        shouldError("Hash is invalid: 'null'",
                () -> validator.checkMD5("null"));

        shouldError("Hash is invalid: 'NULL'",
                () -> validator.checkMD5("NULL"));

        shouldError("Hash is invalid: '*F(@DF^@(&@DF(@^'",
                () -> validator.checkMD5("*F(@DF^@(&@DF(@^"));

        shouldError("Hash is invalid: 'too large aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa'",
                () -> validator.checkMD5("too large aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"));

        shouldError("Hash is invalid: '3-13dc7cb57f02b9c7c066b9e34b6fe72'",
                () -> validator.checkMD5("3-13dc7cb57f02b9c7c066b9e34b6fe72"));

        shouldError("Hash is invalid: '3_13dc7cb57f02b9c7c066b9e34b6fe72'",
                () -> validator.checkMD5("3_13dc7cb57f02b9c7c066b9e34b6fe72"));

        shouldError("Hash is invalid: '3.13dc7cb57f02b9c7c066b9e34b6fe72'",
                () -> validator.checkMD5("3.13dc7cb57f02b9c7c066b9e34b6fe72"));

        shouldError("Hash is invalid: '1'",
                () -> validator.checkMD5("1"));

        shouldError("Hash is invalid: '0'",
                () -> validator.checkMD5("0"));

        shouldError("Hash is invalid: '434589345613405760956134056340596345903465'",
                () -> validator.checkMD5("434589345613405760956134056340596345903465"));

        shouldOk(() -> validator.checkMD5("313dc7cb57f02b9c7c066b9e34b6fe72"));

        shouldError("Hash is invalid: 'some@email.com'",
                () -> validator.checkMD5("some@email.com"));
    }

    @Test
    public void validateCommand() {
        shouldError("Command is invalid: 'null'",
                () -> validator.checkCommand(null));

        shouldError("Command is invalid: ''",
                () -> validator.checkCommand(""));

        shouldError("Command is invalid: 'NuLL'",
                () -> validator.checkCommand("NuLL"));

        shouldError("Command is invalid: 'null'",
                () -> validator.checkCommand("null"));

        shouldError("Command is invalid: 'NULL'",
                () -> validator.checkCommand("NULL"));

        shouldError("Command is invalid: '*F(@DF^@(&@DF(@^'",
                () -> validator.checkCommand("*F(@DF^@(&@DF(@^"));

        shouldError("Command is invalid: '3-13dc7cb57f02b9c7c066b9e34b6fe72'",
                () -> validator.checkCommand("3-13dc7cb57f02b9c7c066b9e34b6fe72"));

        shouldError("Command is invalid: '3_13dc7cb57f02b9c7c066b9e34b6fe72'",
                () -> validator.checkCommand("3_13dc7cb57f02b9c7c066b9e34b6fe72"));

        shouldError("Command is invalid: '3.13dc7cb57f02b9c7c066b9e34b6fe72'",
                () -> validator.checkCommand("3.13dc7cb57f02b9c7c066b9e34b6fe72"));

        shouldError("Command is invalid: '1'",
                () -> validator.checkCommand("1"));

        shouldError("Command is invalid: '0'",
                () -> validator.checkCommand("0"));

        shouldError("Command is invalid: '434589345613405760956134056340596345903465'",
                () -> validator.checkCommand("434589345613405760956134056340596345903465"));

        shouldOk(() -> validator.checkCommand("act(1,2,3),Left,messAGE('SOME TEXT'),RIGHT,ACT(),down,uP,act(1, 2 , 3)  ,DOWN"));

        shouldOk(() -> validator.checkCommand("act()"));

        shouldOk(() -> validator.checkCommand("DOWN"));

        shouldOk(() -> validator.checkCommand("leFT"));

        shouldOk(() -> validator.checkCommand("right,up"));

        shouldOk(() -> validator.checkCommand("left()"));

        shouldOk(() -> validator.checkCommand("act()()"));

        shouldOk(() -> validator.checkCommand("act(1,       2)"));

        shouldOk(() -> validator.checkCommand("act"));

        shouldOk(() -> validator.checkCommand("act(1, 34"));

        // TODO вот тут как-то не совсем верно
        shouldOk(() -> validator.checkCommand("right?up"));
        shouldOk(() -> validator.checkCommand("-right,up"));
        shouldOk(() -> validator.checkCommand("&^@#%&^right@#$&*up@$"));

        shouldError("Command is invalid: 'qwe'",
                () -> validator.checkCommand("qwe"));

        shouldError("Command is invalid: 'qwe,asd'",
                () -> validator.checkCommand("qwe,asd"));

        shouldError("Command is invalid: 'qwe(1,3)'",
                () -> validator.checkCommand("qwe(1,3)"));

        shouldOk(() -> validator.checkCommand("message('кириллица')"));

        shouldOk(() -> validator.checkCommand("message('latin')"));

        shouldOk(() -> validator.checkCommand("message(''')"));

        shouldOk(() -> validator.checkCommand("message(''''''''')"));

        shouldError("Command is invalid: 'messAGE('TOO LARGE'),message('aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa')'",
                () -> validator.checkCommand("messAGE('TOO LARGE'),message('aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa')"));

        shouldError("Command is invalid: 'some@email.com'",
                () -> validator.checkCommand("some@email.com"));

        shouldOk(() -> validator.checkCommand("message('some@email.com')"));
    }

    @Test
    public void validateCheckIsAdmin() {
        when(properties.getAdminPassword()).thenReturn("admin");

        shouldError("Unauthorized admin access",
                () -> validator.checkIsAdmin(null));

        shouldError("Unauthorized admin access",
                () -> validator.checkIsAdmin(""));

        shouldError("Unauthorized admin access",
                () -> validator.checkIsAdmin("admin"));

        shouldError("Unauthorized admin access",
                () -> validator.checkIsAdmin("BAD_21232f297a57a5a743894a0e4a801fc3"));

        shouldOk(() -> validator.checkIsAdmin("21232f297a57a5a743894a0e4a801fc3"));
    }

    @Test
    public void validateCheckPlayerCode() {
        when(registration.checkUser(anyString(), anyString())).thenAnswer(inv -> inv.getArgument(0));

        shouldReturn("email@gmail.com",
                () -> validator.checkPlayerCode("email@gmail.com", "12345678901234567890"));

        shouldReturn("codePlayerId",
                () -> validator.checkPlayerCode("codePlayerId", "12345678901234567890"));

        shouldReturn("Player name/id is invalid: 'aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa@aaa.aaa'",
                () -> validator.checkPlayerCode("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa@aaa.aaa", "12345678901234567890"));

        shouldReturn("Player name/id is invalid: 'aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa'",
                () -> validator.checkPlayerCode("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", "12345678901234567890"));

        shouldReturn("Player code is invalid: '000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000'",
                () -> validator.checkPlayerCode("codePlayerId", "000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"));

        shouldReturn("Player name/id is invalid: 'email#&*^#gmail%#&^*com'",
                () -> validator.checkPlayerCode("email#&*^#gmail%#&^*com", "12345678901234567890"));

        shouldReturn("Player code is invalid: '12dehgfsgfsdlfidfj90'",
                () -> validator.checkPlayerCode("email@gmail.com", "12dehgfsgfsdlfidfj90"));

        shouldReturn("Player name/id is invalid: 'null'",
                () -> validator.checkPlayerCode(null, "12345678901234567890"));

        shouldReturn("Player code is invalid: 'null'",
                () -> validator.checkPlayerCode("email@gmail.com", null));
    }

    @Test
    public void validateCheckPlayerName() {
        shouldOk(() -> validator.checkPlayerId("email@gmail.com", CANT_BE_NULL));

        shouldOk(() -> validator.checkPlayerId("codePlayerId", CANT_BE_NULL));

        shouldError("Player name/id is invalid: 'aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa@aaa.aaa'",
                () -> validator.checkPlayerId("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa@aaa.aaa", CANT_BE_NULL));

        shouldError("Player name/id is invalid: 'aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa'",
                () -> validator.checkPlayerId("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", CANT_BE_NULL));

        shouldError("Player name/id is invalid: 'email#&*^#gmail%#&^*com'",
                () -> validator.checkPlayerId("email#&*^#gmail%#&^*com", CANT_BE_NULL));

        shouldError("Player name/id is invalid: 'null'",
                () -> validator.checkPlayerId(null, CANT_BE_NULL));

        shouldError("Player name/id is invalid: 'NuLL'",
                () -> validator.checkPlayerId("NuLL", CANT_BE_NULL));

        shouldError("Player name/id is invalid: ''",
                () -> validator.checkPlayerId("", CANT_BE_NULL));

        shouldError("Player name/id is invalid: 'null'",
                () -> validator.checkPlayerId(null, CANT_BE_NULL));

        shouldOk(() -> validator.checkPlayerId(null, CAN_BE_NULL));

        shouldOk(() -> validator.checkPlayerId("null", CAN_BE_NULL));

        shouldOk(() -> validator.checkPlayerId("", CAN_BE_NULL));

        shouldOk(() -> validator.checkPlayerId("nULL", CAN_BE_NULL));
    }

    @Test
    public void validateCheckReadablePlayerName() {
        shouldOk(() -> validator.checkReadableName("Стивен Пупкин"));

        shouldOk(() -> validator.checkReadableName("Oleksandr Baglay"));

        shouldOk(() -> validator.checkReadableName("Stiven Pupkin"));

        shouldOk(() -> validator.checkReadableName("стивен пупкин"));

        shouldOk(() -> validator.checkReadableName("stiven pupkin"));

        shouldOk(() -> validator.checkReadableName("ABCDEFGHIJKLMNOPQRSTUVQXYZ abcdefghijklmnopqrstuvqxyz"));

        shouldOk(() -> validator.checkReadableName("abcdefghijklmnopqrstuvqxyz ABCDEFGHIJKLMNOPQRSTUVQXYZ"));

        shouldOk(() -> validator.checkReadableName("абвгдеёжзийклмо НПРСТУФХЧЦЬЫЪЭЮЯ"));

        shouldOk(() -> validator.checkReadableName("нпрстуфхчцьыъэюя АБВГДЕЁЖЗИЙКЛМО"));

        shouldOk(() -> validator.checkReadableName("АБВГДЕЁЖЗИЙКЛМО нпрстуфхчцьыъэюя"));

        shouldOk(() -> validator.checkReadableName("НПРСТУФХЧЦЬЫЪЭЮЯ абвгдеёжзийклмо"));

        shouldOk(() -> validator.checkReadableName("ҐґІіІіЄє ҐґІіІіЄє"));

        shouldError("Readable player name is invalid: 'Стивен'",
                () -> validator.checkReadableName("Стивен"));

        shouldError("Readable player name is invalid: 'Я Д'Артаньян'",
                () -> validator.checkReadableName("Я Д'Артаньян"));

        shouldError("Readable player name is invalid: 'Дефис-нельзя'",
                () -> validator.checkReadableName("Дефис-нельзя"));

        shouldError("Readable player name is invalid: 'Двапробела  нельзя'",
                () -> validator.checkReadableName("Двапробела  нельзя"));

        shouldError("Readable player name is invalid: 'aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa aaaa'",
                () -> validator.checkReadableName("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa aaaa"));

        shouldError("Readable player name is invalid: 'email#&*^#gmail%#&^*com'",
                () -> validator.checkReadableName("email#&*^#gmail%#&^*com"));

        shouldError("Readable player name is invalid: 'null'",
                () -> validator.checkReadableName(null));

        shouldError("Readable player name is invalid: 'NuLL'",
                () -> validator.checkReadableName("NuLL"));

        shouldError("Readable player name is invalid: ''",
                () -> validator.checkReadableName(""));

        shouldError("Readable player name is invalid: 'null'",
                () -> validator.checkReadableName(null));
    }

    private void shouldOk(Runnable toRun) {
        shouldError("", toRun);
    }

    private void shouldError(String expected, Runnable toRun) {
        try {
            if (toRun != null) {
                toRun.run();
            }
            if (StringUtils.isNotEmpty(expected)) {
                fail();
            }
        } catch (Exception e) {
            assertEquals(expected, e.getMessage());
        }
    }

    private void shouldReturn(String expected, Callable toRun) {
        try {
            Object result = null;
            if (toRun != null) {
                result = toRun.call();
            }
            assertEquals(expected, result);
        } catch (Exception e) {
            assertEquals(expected, e.getMessage());
        }
    }

}
