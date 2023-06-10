package com.guessnumbergame.playerservice.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.guessnumbergame.playerservice.Application;

@SpringBootTest(classes = Application.class)
@DisplayName("User")
@Tag("dto")
@Tag("security")
class UserTest {

  @Test
  @DisplayName("equals(Object) - matching objects")
  final void equals_matchingObjects() throws Exception {
    User user1 = new User("username", "password1");
    User user2 = new User("username", "password2");
    assertEquals(user1, user2);
  }

  @Test
  @DisplayName("equals(Object) - not matching objects")
  final void equals_notMatchingObjects() throws Exception {
    User user1 = new User("username1", "password");
    User user2 = new User("username2", "password");
    assertNotEquals(user1, user2);
  }

  @Test
  @DisplayName("hashCode(Object) - matching objects")
  final void hashCode_matchingObjects() throws Exception {
    User user1 = new User("username", "password1");
    User user2 = new User("username", "password2");
    assertEquals(user1.hashCode(), user2.hashCode());
  }

}
