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


import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lol.fallen.FKSVendor.dtos.RoleToUserDto;
import lol.fallen.FKSVendor.models.UserRole;
import lol.fallen.FKSVendor.models.VendorUser;
import lol.fallen.FKSVendor.services.UserService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class VendorUserResource {
    private final UserService userService;

    @RolesAllowed({"ROLE_MANAGER","ROLE_OWNER"})
    @GetMapping("/users")
    public ResponseEntity<List<VendorUser>> findAllUsers() {
        return ResponseEntity.ok().body(userService.getAllUsers());
    }

    @RolesAllowed({"ROLE_OWNER", "ROLE_MANAGER"})
    @PostMapping("/user/save")
    public ResponseEntity<VendorUser> saveUser(@Valid @RequestBody VendorUser user) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/user/save").toUriString());
        return ResponseEntity.created(uri).body(userService.saveUser(user));
    }

    @RolesAllowed({"ROLE_OWNER", "ROLE_MANAGER"})
    @PostMapping("/role/save")
    public ResponseEntity<UserRole> saveRole(@Valid @RequestBody UserRole role) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/role/save").toUriString());

        return ResponseEntity.created(uri).body(userService.saveRole(role));
    }

    @RolesAllowed({"ROLE_OWNER", "ROLE_MANAGER"})
    @PostMapping("/user/addrole")
    public ResponseEntity<?>addRole(@RequestBody RoleToUserDto roleToUserDto) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/role/save").toUriString());
        userService.addRoleToUser(roleToUserDto.getUsername(), roleToUserDto.getRoleName());
        return ResponseEntity.ok().build();
    }
}
