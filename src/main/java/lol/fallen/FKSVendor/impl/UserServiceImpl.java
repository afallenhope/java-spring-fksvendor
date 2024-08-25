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

package lol.fallen.FKSVendor.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lol.fallen.FKSVendor.models.UserRole;
import lol.fallen.FKSVendor.models.VendorUser;
import lol.fallen.FKSVendor.repositories.UserRoleRepository;
import lol.fallen.FKSVendor.repositories.VendorUserRepository;
import lol.fallen.FKSVendor.services.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {
    private final VendorUserRepository vendorUserRepository;
    private final UserRoleRepository roleRepository;

    /**
     * @param {VendorUser} user
     * @return
     */
    @Override
    public VendorUser saveUser(VendorUser user) {
        log.info("Saving new user {} to database", user.getUsername());
        return vendorUserRepository.save(user);
    }

    /**
     * @param {UserRole} role
     * @return
     */
    @Override
    public UserRole saveRole(UserRole role) {
        log.info("Saving new role {} to database", role.getName());
        return roleRepository.save(role);
    }

    /**
     * @param {String} username
     * @param {String} role
     */
    @Override
    public void addRoleToUser(String username, String role) {
        log.info("Adding role {} to user {}", role, username);
        VendorUser user = vendorUserRepository.findByUsername(username);
        UserRole userRole = roleRepository.findByName(role);

        user.getRoles().add(userRole);
    }

    /**
     * @param {String} username
     * @return {VendorUser} vendorUser
     */
    @Override
    public VendorUser loadUserByUsername(String username) {
        log.info("Loading user {}", username);
        return vendorUserRepository.findByUsername(username);
    }

    /**
     * @return {List<VendorUser>} vendorUser
     */
    @Override
    public List<VendorUser> getAllUsers() {
        log.info("Loading all users");
        return vendorUserRepository.findAll();
    }
}
