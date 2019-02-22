package com.codenjoy.dojo.services.dao;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
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


import com.codenjoy.dojo.services.ContextPathGetter;
import com.codenjoy.dojo.services.hash.Hash;
import com.codenjoy.dojo.services.jdbc.SqliteConnectionThreadPoolFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.*;

public class RegistrationTest {

    public static final String HASH = "someHash";
    private static Registration service;

    @Before
    public void setup() {
        String dbFile = "target/users.db" + new Random().nextInt();
        service = new Registration(
                new SqliteConnectionThreadPoolFactory(dbFile,
                        new ContextPathGetter() {
                            @Override
                            public String getContext() {
                                return "context";
                            }
                        }));
    }

    @After
    public void tearDown() {
        service.removeDatabase();
    }

    @Test
    public void shouldNotExistsUser() {
        assertFalse(service.registered("not_exists"));
    }

    @Test
    public void shouldRegister() {
        // given
        String code = service.register("user", "email", "name", "pass", "data");

        // then
        assertTrue(service.registered("user"));
        assertFalse(service.approved("user"));

        // when
        service.approve(code);

        // then
        assertTrue(service.registered("user"));
        assertTrue(service.approved("user"));
    }

    @Test
    public void shouldRegisterWithData() {
        // when
        String code = service.register("user", "email", "name", "pass", "someData");

        // then
        assertEquals("3514017434644657823", code);

        List<Registration.User> users = service.getUsers();

        assertEquals("[User{id='user', " +
                "email=email, " +
                "readable_name=name, " +
                "email_approved=0, " +
                "password='pass', " +
                "code='3514017434644657823', " +
                "data='someData'}]",
                users.toString());
    }

    @Test
    public void shouldUnRegisteredUserIsNotApproved() {
        // when then
        assertFalse(service.approved("user"));
    }

    @Test
    public void shouldSuccessLogin() {
        // given
        service.approve(service.register("user", "email", "name", "pass", "data"));

        // when
        String code = service.login("user", "pass");

        // then
        assertEquals("3514017434644657823", code);
    }

    @Test
    public void shouldUnSuccessLogin_whenNoApproveEmail() {
        // given
        service.register("user", "email", "name", "pass", "data");

        // when
        String code = service.login("user", "pass");

        // then
        assertEquals(null, code);
    }

    @Test
    public void shouldUnSuccessLogin() {
        // given
        service.register("user", "email", "name", "pass", "data");

        // when
        String code = service.login("user", "bad_pass");

        // then
        assertEquals(null, code);
    }

    @Test
    public void shouldGetCodeByName() {
        // given
        service.register("user", "email", "name", "pass", "data");

        // when
        String code = service.getCode("user");

        // then
        assertEquals("3514017434644657823", code);
    }

    @Test
    public void shouldGetCodeByName_ifNotFound() {
        // given
        service.register("user", "email", "name", "pass", "data");

        // when
        String code = service.getCode("other_user");

        // then
        assertEquals(null, code);
    }

    @Test
    public void shouldGetEmailByCode() {
        // given
        String code = service.register("user", "email", "name", "pass", "data");

        // when
        String email = service.getId(code);

        // then
        assertEquals("user", email);
    }

    @Test
    public void shouldGetReadableNameByEmail() {
        // given
        service.register("user", "email", "name", "pass", "data");

        // when
        String name = service.getReadableName("user");

        // then
        assertEquals("name", name);
    }

    @Test
    public void shouldGetEmailByCode_ifNotFound() {
        // given
        service.register("user", "email", "name", "pass", "data");

        // when
        String email = service.getId("bad_code");

        // then
        assertNull(email);
    }

    @Test
    public void shouldChangePasswordsToMD5() {
        // given
        service.approve(service.register("user", "email", "name", "pass", "data"));
        service.approve(service.register("user2", "email2", "name2", "pass2", "data2"));

        assertEquals("3514017434644657823", service.login("user", "pass"));
        assertEquals("5959534295416704333", service.login("user2", "pass2"));

        // when
        service.changePasswordsToMD5();

        // then
        assertEquals("3514017434644657823", service.login("user", "1a1dc91c907325c69271ddf0c944bc72"));
        assertEquals("5959534295416704333", service.login("user2", "c1572d05424d0ecb2a65ec6a82aeacbf"));
    }

    @Test
    public void shouldUpdateReadableName() {
        // given
        String code1 = service.register("user1", "email1", "name1", "pass1", "someData1");
        String code2 = service.register("user2", "email2", "name2", "pass2", "someData2");

        assertEquals("[User{id='user1', " +
                        "email=email1, " +
                        "readable_name=name1, " +
                        "email_approved=0, " +
                        "password='pass1', " +
                        "code='1997914414654684689', " +
                        "data='someData1'}, " +
                      "User{id='user2', " +
                        "email=email2, " +
                        "readable_name=name2, " +
                        "email_approved=0, " +
                        "password='pass2', " +
                        "code='5959534295416704333', " +
                        "data='someData2'}]",
                service.getUsers().toString());

        // when
        service.updateReadableName("user1", "updatedName1");

        // then
        assertEquals("[User{id='user1', " +
                        "email=email1, " +
                        "readable_name=updatedName1, " +
                        "email_approved=0, " +
                        "password='pass1', " +
                        "code='1997914414654684689', " +
                        "data='someData1'}, " +
                      "User{id='user2', " +
                        "email=email2, " +
                        "readable_name=name2, " +
                        "email_approved=0, " +
                        "password='pass2', " +
                        "code='5959534295416704333', " +
                        "data='someData2'}]",
                service.getUsers().toString());
    }

    @Test
    public void shouldReplaceExistingUser() {
        // given
        String code1 = service.register("user1", "email1", "name1", "pass1", "someData1");
        String code2 = service.register("user2", "email2", "name2", "pass2", "someData2");

        assertEquals("[User{id='user1', " +
                        "email=email1, " +
                        "readable_name=name1, " +
                        "email_approved=0, " +
                        "password='pass1', " +
                        "code='1997914414654684689', " +
                        "data='someData1'}, " +
                      "User{id='user2', " +
                        "email=email2, " +
                        "readable_name=name2, " +
                        "email_approved=0, " +
                        "password='pass2', " +
                        "code='5959534295416704333', " +
                        "data='someData2'}]",
                service.getUsers().toString());

        // when
        service.replace(new Registration.User("user1", "email1", "name1", 1, "newPassword1", "newCode1", "newData1"));

        // then
        assertEquals("[User{id='user1', " +
                        "email=email1, " +
                        "readable_name=name1, " +
                        "email_approved=1, " +
                        "password='newPassword1', " +
                        "code='newCode1', " +
                        "data='newData1'}, " +
                      "User{id='user2', " +
                        "email=email2, " +
                        "readable_name=name2, " +
                        "email_approved=0, " +
                        "password='pass2', " +
                        "code='5959534295416704333', " +
                        "data='someData2'}]",
                service.getUsers().toString());
    }

    @Test
    public void shouldReplaceExistingUser_withoutCode() {
        // given
        String code1 = service.register("user1", "email1", "name1", "pass1", "someData1");
        String code2 = service.register("user2", "email2", "name2", "pass2", "someData2");

        assertEquals("[User{id='user1', " +
                        "email=email1, " +
                        "readable_name=name1, " +
                        "email_approved=0, " +
                        "password='pass1', " +
                        "code='1997914414654684689', " +
                        "data='someData1'}, " +
                      "User{id='user2', " +
                        "email=email2, " +
                        "readable_name=name2, " +
                        "email_approved=0, " +
                        "password='pass2', " +
                        "code='5959534295416704333', " +
                        "data='someData2'}]",
                service.getUsers().toString());

        // when
        String noCode = null;
        service.replace(new Registration.User("user1", "email1", "name1", 1, "newPassword1", noCode, "newData1"));

        // then
        assertEquals("[User{id='user1', " +
                        "email=email1, " +
                        "readable_name=name1, " +
                        "email_approved=1, " +
                        "password='newPassword1', " +
                        "code='62952809866083378', " +
                        "data='newData1'}, " +
                      "User{id='user2', " +
                        "email=email2, " +
                        "readable_name=name2, " +
                        "email_approved=0, " +
                        "password='pass2', " +
                        "code='5959534295416704333', " +
                        "data='someData2'}]",
                service.getUsers().toString());
    }

    @Test
    public void shouldReplaceNonExistingUser() {
        // given
        String code1 = service.register("user1", "email1", "name1", "pass1", "someData1");
        String code2 = service.register("user2", "email2", "name2", "pass2", "someData2");

        assertEquals("[User{id='user1', " +
                        "email=email1, " +
                        "readable_name=name1, " +
                        "email_approved=0, " +
                        "password='pass1', " +
                        "code='1997914414654684689', " +
                        "data='someData1'}, " +
                      "User{id='user2', " +
                        "email=email2, " +
                        "readable_name=name2, " +
                        "email_approved=0, " +
                        "password='pass2', " +
                        "code='5959534295416704333', " +
                        "data='someData2'}]",
                service.getUsers().toString());

        // when
        service.replace(new Registration.User("user3", "email3", "name3", 1, "newPassword3", "newCode3", "newData3"));

        // then
        assertEquals("[User{id='user1', " +
                        "email=email1, " +
                        "readable_name=name1, " +
                        "email_approved=0, " +
                        "password='pass1', " +
                        "code='1997914414654684689', " +
                        "data='someData1'}, " +
                      "User{id='user2', " +
                        "email=email2, " +
                        "readable_name=name2, " +
                        "email_approved=0, " +
                        "password='pass2', " +
                        "code='5959534295416704333', " +
                        "data='someData2'}, " +
                      "User{id='user3', " +
                        "email=email3, " +
                        "readable_name=name3, " +
                        "email_approved=1, " +
                        "password='newPassword3', " +
                        "code='newCode3', " +
                        "data='newData3'}]",
                service.getUsers().toString());
    }

    @Test
    public void shouldRemoveUser() {
        // given
        String code1 = service.register("user1", "email1", "name1", "pass1", "someData1");
        String code2 = service.register("user2", "email2", "name2", "pass2", "someData2");

        assertEquals("[User{id='user1', " +
                        "email=email1, " +
                        "readable_name=name1, " +
                        "email_approved=0, " +
                        "password='pass1', " +
                        "code='1997914414654684689', " +
                        "data='someData1'}, " +
                      "User{id='user2', " +
                        "email=email2, " +
                        "readable_name=name2, " +
                        "email_approved=0, " +
                        "password='pass2', " +
                        "code='5959534295416704333', " +
                        "data='someData2'}]",
                service.getUsers().toString());

        // when
        service.remove("user1");

        // then
        assertEquals("[User{id='user2', " +
                        "email=email2, " +
                        "readable_name=name2, " +
                        "email_approved=0, " +
                        "password='pass2', " +
                        "code='5959534295416704333', " +
                        "data='someData2'}]",
                service.getUsers().toString());
    }

    @Test
    public void shouldRemoveAllUsers() {
        // given
        String code1 = service.register("user1", "email1", "name1", "pass1", "someData1");
        String code2 = service.register("user2", "email2", "name2", "pass2", "someData2");

        assertEquals("[User{id='user1', " +
                        "email=email1, " +
                        "readable_name=name1, " +
                        "email_approved=0, " +
                        "password='pass1', " +
                        "code='1997914414654684689', " +
                        "data='someData1'}, " +
                      "User{id='user2', " +
                        "email=email2, " +
                        "readable_name=name2, " +
                        "email_approved=0, " +
                        "password='pass2', " +
                        "code='5959534295416704333', " +
                        "data='someData2'}]",
                service.getUsers().toString());

        // when
        service.removeAll();

        // then
        assertEquals("[]",
                service.getUsers().toString());
    }

    @Test
    public void shouldCheckUser_whenOnlyEmails() {
        String email = "user@email.com";

        String code = service.register(email, "email", "name", "pass", "someData");

        assertEquals(email, service.checkUser(email, code));
    }

    @Test
    public void shouldCheckUser_whenIdStoredOnDb_askWithEmail() {
        String email = "user@email.com";
        String id = Hash.getId(email, HASH);

        String code = service.register(id, "email", "name", "pass", "someData");

        assertEquals(null, service.checkUser(email, code));
        assertEquals(id, service.checkUser(id, code));
    }

    @Test
    public void shouldCheckUser_whenEmailStoredOnDb_askWithId() {
        String email = "user@email.com";
        String id = Hash.getId(email, HASH);

        String code = service.register(email, "email", "name", "pass", "someData");

        assertEquals(null, service.checkUser(id, code));
        assertEquals(email, service.checkUser(email, code));
    }

    @Test
    public void shouldCheckUser_whenOnlyIds() {
        String email = "user@email.com";
        String id = Hash.getId(email, HASH);

        String code = service.register(id, "email", "name", "pass", "someData");

        assertEquals(id, service.checkUser(id, code));
        assertEquals(null, service.checkUser(email, code));
    }
}
