/*
 * Copyright (c) 2024
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package lol.fallen.FKSVendor.commands;

import lol.fallen.FKSVendor.models.UserRole;
import lol.fallen.FKSVendor.models.VendorUser;
import lol.fallen.FKSVendor.repositories.UserRoleRepository;
import lol.fallen.FKSVendor.repositories.VendorUserRepository;
import lol.fallen.FKSVendor.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DatabaseSeederRunnerTest {
    @Mock
    private UserService userService;

    @Mock
    private VendorUserRepository vendorUserRepository;

    @Mock
    private UserRoleRepository userRoleRepository;

    @InjectMocks
    private DatabaseSeederRunner databaseSeederRunner;

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRun() throws Exception {
        // Arrange
        bCryptPasswordEncoder = new BCryptPasswordEncoder();

        // Act
        databaseSeederRunner.run();

        // Assert
        verify(userRoleRepository, times(5)).save(any(UserRole.class));
        verify(vendorUserRepository, times(3)).save(any(VendorUser.class));

        ArgumentCaptor<String> userArgumentCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> roleArgumentCaptor = ArgumentCaptor.forClass(String.class);

        verify(userService, times(5))
                .addRoleToUser(
                        userArgumentCaptor.capture(),
                        roleArgumentCaptor.capture()
                );

        assertEquals("fallen.shadow", userArgumentCaptor.getAllValues().get(0));
        assertEquals("ROLE_OWNER", roleArgumentCaptor.getAllValues().get(0));
        assertEquals("sullen.nebula", userArgumentCaptor.getAllValues().get(2));
        assertEquals("ROLE_TRUSTED", roleArgumentCaptor.getAllValues().get(2));
        assertEquals("test.user", userArgumentCaptor.getAllValues().get(4));
        assertEquals("ROLE_USER", roleArgumentCaptor.getAllValues().get(4));
    }
}