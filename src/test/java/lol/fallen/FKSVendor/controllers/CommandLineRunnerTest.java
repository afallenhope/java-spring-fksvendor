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

package lol.fallen.FKSVendor;

import lol.fallen.FKSVendor.models.UserRole;
import lol.fallen.FKSVendor.models.VendorUser;
import lol.fallen.FKSVendor.services.UserService;
import lol.fallen.FKSVendor.services.VendorStoreService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CommandLineRunnerTest {
    @Mock
    private UserService userService;

    @Mock
    private VendorStoreService vendorStoreService;

    @InjectMocks
    private FksVendorApplication fksVendorApplication;

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @BeforeEach
    void setUp() {
        bCryptPasswordEncoder = new BCryptPasswordEncoder();
    }

    @Test
    void testCommandlineRunner() throws Exception {
        // Arrange
        ArgumentCaptor<VendorUser> vendorUserArgumentCaptor = ArgumentCaptor.forClass(VendorUser.class);

        // Act
        fksVendorApplication.run(userService, vendorStoreService).run();

        // Assert
        verify(userService, times(1)).saveRole(new UserRole(null, "ROLE_USER"));
        verify(userService, times(1)).saveRole(new UserRole(null, "ROLE_MANAGER"));
        verify(userService, times(1)).saveRole(new UserRole(null, "ROLE_BLOGGER"));
        verify(userService, times(1)).saveRole(new UserRole(null, "ROLE_OWNER"));
        verify(userService, times(1)).saveRole(new UserRole(null, "ROLE_TRUSTED"));

        verify(userService, times(2)).saveUser(vendorUserArgumentCaptor.capture()); // this is like a spy?

        List<VendorUser> capturedUsers = vendorUserArgumentCaptor.getAllValues();

        assertEquals("fallen.shadow", capturedUsers.get(0).getUsername());
        assertEquals("sullen.nebula", capturedUsers.get(1).getUsername());

        verify(userService, times(1)).addRoleToUser("fallen.shadow", "ROLE_OWNER");
        verify(userService, times(1)).addRoleToUser("fallen.shadow", "ROLE_MANAGER");
        verify(userService, times(1)).addRoleToUser("sullen.nebula", "ROLE_TRUSTED");
        verify(userService, times(1)).addRoleToUser("sullen.nebula", "ROLE_MANAGER");
    }
}
