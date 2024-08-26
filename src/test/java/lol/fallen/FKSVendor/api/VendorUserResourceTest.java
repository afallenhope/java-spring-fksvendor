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

package lol.fallen.FKSVendor.api;

import lol.fallen.FKSVendor.models.UserRole;
import lol.fallen.FKSVendor.models.VendorUser;
import lol.fallen.FKSVendor.repositories.UserRoleRepository;
import lol.fallen.FKSVendor.repositories.VendorUserRepository;
import lol.fallen.FKSVendor.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VendorUserResource.class) // Adjust this to your specific controller
class VendorUserResourceTest {
    @Autowired
    private MockMvc mockMvc;

    @Value("${security.jwt.secret-key}")
    private String jwtSecretKey;

    @Value("${security.jwt.issuer}")
    private String jwtIssuer;

    @MockBean
    private VendorUserRepository vendorUserRepository;

    @MockBean
    private UserRoleRepository userRoleRepository;

    @MockBean
    private UserService userService;

    @InjectMocks
    private VendorUserResource vendorUserResource;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @WithMockUser(username = "mockAuthenticatedUser", roles = {"USER"})
    void testFindAllUsers_Success() throws Exception {
        // Arrange
        VendorUser vendorUser = new VendorUser();
        vendorUser.setUsername("mockAuthenticatedUser");
        when(userService.getAllUsers()).thenReturn(List.of(new VendorUser()));

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/users");

        // Act
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        // Assert
        verify(userService, times(1)).getAllUsers();
    }

    @Test
    @WithMockUser(username = "mockUserWithoutRole", roles = {"USER"})
    void testFindAllUsers_Forbidden() throws Exception {
        // Arrange
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/users");

        // Act
        mockMvc.perform(requestBuilder)
                .andExpect(status().isForbidden());

        // Assert
        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void testSaveUser() {
        // Act
        VendorUser vendorUser = new VendorUser();
        when(userService.saveUser(any(VendorUser.class))).thenReturn(vendorUser);

        // Arrange
        ResponseEntity<VendorUser> responseEntity = vendorUserResource.saveUser(vendorUser);

        // Assert
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(vendorUser, responseEntity.getBody());
        verify(userService, times(1)).saveUser(vendorUser);
    }

    @Test
    void testSaveRole() {
        // Act
        UserRole userRole = new UserRole();
        when(userService.saveRole(any(UserRole.class))).thenReturn(userRole);

        // Arrange
        ResponseEntity<UserRole> responseEntity = vendorUserResource.saveRole(userRole);

        // Assert
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(userRole, responseEntity.getBody());
        verify(userService, times(1)).saveRole(userRole);
    }

    @Test
    void testAddRole() {
        // Act

        // Arrange

        // Assert
    }
}