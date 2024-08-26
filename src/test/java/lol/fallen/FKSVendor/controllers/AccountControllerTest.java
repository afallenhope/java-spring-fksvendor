/*
 * Copyright (c) 2024 Pierre C
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

package lol.fallen.FKSVendor.controllers;

import lol.fallen.FKSVendor.models.VendorUser;
import lol.fallen.FKSVendor.repositories.UserRoleRepository;
import lol.fallen.FKSVendor.repositories.VendorUserRepository;
import lol.fallen.FKSVendor.services.JwtService;
import lol.fallen.FKSVendor.services.UserService;
import lol.fallen.FKSVendor.services.VendorStoreService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AccountController.class)
class AccountControllerTest {

    @Autowired
    private MockMvc mvc;

    @Value("${security.jwt.secret-key}")
    private String jwtSecretKey;

    @Value("${security.jwt.issuer}")
    private String jwtIssuer;

    @MockBean
    private VendorUserRepository vendorUserRepository;

    @MockBean
    private UserRoleRepository userRoleRepository;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private Authentication authentication;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserService userService;

    @MockBean
    private VendorStoreService vendorStoreService;

    @Test
    @WithMockUser(username = "testUser", roles = {"USER"})
    void testProfile_Success() throws Exception {
        // Arrange
        VendorUser mockUser = new VendorUser();
        mockUser.setUsername("testUser");

        when(authentication.getName()).thenReturn("testUser");
        when(authentication.getAuthorities()).thenReturn(Collections.emptyList());
        when(vendorUserRepository.findByUsername("testUser")).thenReturn(mockUser);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/account/profile").principal(authentication);

        // Act
        mvc
                .perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user.username").value("testUser"))
                .andExpect(jsonPath("$.authorities").isArray())
                .andExpect(jsonPath("$.error").doesNotExist()).andReturn();

        /// Assert
        verify(vendorUserRepository, times(1)).findByUsername("testUser");
    }

    @Test
    void testProfile_ReturnsUnauthorizedWhenNotAuthenticated() throws Exception {
        // Arrange
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/account/profile");

        // Act
        mvc.perform(requestBuilder).andExpect(status().isUnauthorized());

        /// Assert
        verify(vendorUserRepository, times(0)).findByUsername(anyString());
    }
}